package org.riverdell.robotics.xdk.opmodes

import com.arcrobotics.ftclib.gamepad.GamepadEx
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.Gamepad
import com.qualcomm.robotcore.hardware.Gamepad.LedEffect
import io.liftgate.robotics.mono.Mono.commands
import io.liftgate.robotics.mono.gamepad.ButtonType
import io.liftgate.robotics.mono.gamepad.GamepadCommands
import io.liftgate.robotics.mono.subsystem.Subsystem
import io.liftgate.robotics.mono.subsystem.System
import org.riverdell.robotics.xdk.opmodes.pipeline.scheduleAsyncExecution
import org.riverdell.robotics.xdk.opmodes.subsystem.AirplaneLauncher
import org.riverdell.robotics.xdk.opmodes.subsystem.Drivebase
import org.riverdell.robotics.xdk.opmodes.subsystem.Elevator
import org.riverdell.robotics.xdk.opmodes.subsystem.claw.ExtendableClaw

/**
 * Configures Mono gamepad commands and FTCLib
 * drive systems for TeleOp.
 *
 * @author Subham
 * @since 9/5/2023
 */
abstract class AbstractTeleOp : LinearOpMode(), System
{
    override val subsystems: MutableSet<Subsystem> = mutableSetOf()

    private val gp1Commands by lazy { commands(gamepad1) }
    private val gp2Commands by lazy { commands(gamepad2) }

    private val drivebase by lazy { Drivebase(this) }
    private val paperPlaneLauncher by lazy { AirplaneLauncher(this) }
    private val elevator by lazy { Elevator(this) }
    private val extendableClaw by lazy { ExtendableClaw(this) }

    abstract fun driveRobot(
        drivebase: Drivebase,
        driverOp: GamepadEx,
        multiplier: Double
    )

    override fun runOpMode()
    {
        register(
            drivebase, paperPlaneLauncher, elevator,
            extendableClaw, gp1Commands, gp2Commands
        )

        val driverOp = GamepadEx(gamepad1)
        buildCommands()

        telemetry.addLine("Configured all subsystems. Waiting for start...")

        if (
            gamepad1.type != Gamepad.Type.SONY_PS4 &&
            gamepad1.type != Gamepad.Type.SONY_PS4_SUPPORTED_BY_KERNEL
        )
        {
            telemetry.addLine("WARNING! We require a Sony PS4 controller to be used as GAMEPAD 1. Please fix this to ensure everything works as intended!")
        }

        telemetry.update()

        initializeAll()
        waitForStart()

        telemetry.addLine("Initialized all subsystems. We're ready to go!")
        telemetry.update()

        extendableClaw.toggleExtender(
            ExtendableClaw.ExtenderState.Deposit
        )

        while (opModeIsActive())
        {
            val multiplier = 0.6 + gamepad1.right_trigger * 0.4
            driveRobot(drivebase, driverOp, multiplier)

            if (!bundleExecutionInProgress)
            {
                elevator.configureElevator(gamepad2.right_stick_y.toDouble())
            }

            if (extendableClaw.extenderState == ExtendableClaw.ExtenderState.Intake)
            {
                // physical feedback towards the driver when driving with the extender down
                if (gamepad1.nextRumbleApproxFinishTime > java.lang.System.currentTimeMillis())
                {
                    continue
                }

                gamepad1.rumble(250)
            }
        }

        disposeOfAll()
    }

    private var bundleExecutionInProgress = false

    private fun buildCommands()
    {
        gp1Commands
            .where(ButtonType.PlayStationTouchpad)
            .onlyWhen {
                gamepad1.touchpad_finger_1_x <= 0.0 &&
                        gamepad1.touchpad_finger_2_x >= 0.0
            }
            .triggers {
                paperPlaneLauncher.launch()
            }
            .andIsHeldUntilReleasedWhere {
                paperPlaneLauncher.reset()
            }

        gp1Commands
            .where(ButtonType.BumperRight)
            .triggers {
                extendableClaw.toggleExtender(
                    ExtendableClaw.ExtenderState.Intermediate
                )
            }
            .andIsHeldUntilReleasedWhere {
                extendableClaw.toggleExtender(
                    ExtendableClaw.ExtenderState.Deposit
                )
            }

        // extender expansion ranges
        gp1Commands
            .where(ButtonType.PlayStationTouchpad)
            .onlyWhen { gamepad1.touchpad_finger_1_y <= 0.0 }
            .triggers {
                extendableClaw.decrementAddition()
            }
            .whenPressedOnce()

        gp1Commands
            .where(ButtonType.PlayStationTouchpad)
            .onlyWhen { gamepad1.touchpad_finger_1_y >= 0.0 }
            .triggers {
                extendableClaw.incrementAddition()
            }
            .whenPressedOnce()

        // claw expansion ranges
        gp1Commands
            .where(ButtonType.PlayStationTouchpad)
            .onlyWhenNot { bundleExecutionInProgress }
            .onlyWhen { !gamepad1.touchpad_finger_2 && gamepad1.touchpad_finger_1_x <= 0.0 }
            .triggers {
                extendableClaw.decrementClawAddition()
                extendableClaw.updateClawState(
                    ExtendableClaw.ClawStateUpdate.Both,
                    ExtendableClaw.ClawState.Closed
                )
            }
            .whenPressedOnce()

        gp1Commands
            .where(ButtonType.PlayStationTouchpad)
            .onlyWhenNot { bundleExecutionInProgress }
            .onlyWhen { !gamepad1.touchpad_finger_2 && gamepad1.touchpad_finger_1_x >= 0.0 }
            .triggers {
                extendableClaw.incrementClawAddition()
                extendableClaw.updateClawState(
                    ExtendableClaw.ClawStateUpdate.Both,
                    ExtendableClaw.ClawState.Closed
                )
            }
            .whenPressedOnce()

        // bumper commands for opening closing claw fingers individually
        gp2Commands
            .where(ButtonType.BumperLeft)
            .onlyWhen { !bundleExecutionInProgress }
            .triggers {
                extendableClaw.updateClawState(
                    ExtendableClaw.ClawStateUpdate.Left,
                    ExtendableClaw.ClawState.Open
                )
            }
            .andIsHeldUntilReleasedWhere {
                extendableClaw.updateClawState(
                    ExtendableClaw.ClawStateUpdate.Left,
                    ExtendableClaw.ClawState.Closed
                )
            }

        gp2Commands
            .where(ButtonType.BumperRight)
            .onlyWhen { !bundleExecutionInProgress }
            .triggers {
                extendableClaw.updateClawState(
                    ExtendableClaw.ClawStateUpdate.Right,
                    ExtendableClaw.ClawState.Open
                )
            }
            .andIsHeldUntilReleasedWhere {
                extendableClaw.updateClawState(
                    ExtendableClaw.ClawStateUpdate.Right,
                    ExtendableClaw.ClawState.Closed
                )
            }

        // elevator preset (low backboard)
        gp2Commands
            .where(ButtonType.ButtonX)
            .triggers {
                elevator.configureElevatorManually(0.7)
            }
            .whenPressedOnce()

        gp2Commands
            .where(ButtonType.ButtonB)
            .triggers {
                extendableClaw.updateClawState(
                    ExtendableClaw.ClawStateUpdate.Both,
                    ExtendableClaw.ClawState.Open
                )
            }
            .andIsHeldUntilReleasedWhere {
                extendableClaw.updateClawState(
                    ExtendableClaw.ClawStateUpdate.Both,
                    ExtendableClaw.ClawState.Closed
                )
            }

        // extender to intake
        gp2Commands
            .where(ButtonType.ButtonY)
            .onlyWhen { !bundleExecutionInProgress }
            .triggers {
                extendableClaw.toggleExtender(
                    ExtendableClaw.ExtenderState.Intake
                )
            }
            .andIsHeldUntilReleasedWhere {
                extendableClaw.toggleExtender(
                    ExtendableClaw.ExtenderState.Deposit
                )
            }

        gp2Commands.where(ButtonType.ButtonA)
            .triggers {
                if (bundleExecutionInProgress)
                {
                    return@triggers
                }

                bundleExecutionInProgress = true
                extendableClaw.toggleExtender(
                    ExtendableClaw.ExtenderState.Intake
                )

                extendableClaw.updateClawState(
                    ExtendableClaw.ClawStateUpdate.Both,
                    ExtendableClaw.ClawState.Open
                )
            }
            .andIsHeldUntilReleasedWhere {
                if (!bundleExecutionInProgress)
                {
                    return@andIsHeldUntilReleasedWhere
                }

                extendableClaw.updateClawState(
                    ExtendableClaw.ClawStateUpdate.Both,
                    ExtendableClaw.ClawState.Closed
                )

                scheduleAsyncExecution(250L) {
                    extendableClaw.toggleExtender(
                        ExtendableClaw.ExtenderState.Deposit
                    )
                    bundleExecutionInProgress = false
                }
            }

        fun GamepadCommands.ButtonMappingBuilder.depositPresetReleaseOnElevatorHeight(position: Int)
        {
            triggers {
                if (bundleExecutionInProgress)
                {
                    return@triggers
                }

                bundleExecutionInProgress = true
                elevator.configureElevatorManuallyRaw(position)
            }.andIsHeldUntilReleasedWhere {
                if (!bundleExecutionInProgress)
                {
                    return@andIsHeldUntilReleasedWhere
                }

                val applyUpdatesTo = when (true)
                {
                    gamepad2.left_bumper -> ExtendableClaw.ClawStateUpdate.Left
                    gamepad2.right_bumper -> ExtendableClaw.ClawStateUpdate.Right
                    (gamepad2.right_bumper && gamepad2.left_bumper) -> ExtendableClaw.ClawStateUpdate.Both
                    else -> ExtendableClaw.ClawStateUpdate.Both
                }

                if (gamepad2.right_trigger <= 0.5)
                {
                    extendableClaw.updateClawState(
                        applyUpdatesTo,
                        ExtendableClaw.ClawState.Open
                    )
                }

                scheduleAsyncExecution(150L) {
                    elevator.configureElevatorManuallyRaw(0)

                    Thread.sleep(500L)
                    bundleExecutionInProgress = false
                }
            }
        }

        gp2Commands
            .where(ButtonType.DPadLeft)
            .depositPresetReleaseOnElevatorHeight(-630)

        gp2Commands
            .where(ButtonType.DPadUp)
            .depositPresetReleaseOnElevatorHeight(-1130)

        gp2Commands
            .where(ButtonType.DPadDown)
            .triggers {
                elevator.configureElevatorManually(0.0)
            }
            .whenPressedOnce()
    }
}