package org.riverdell.robotics.teleop

import com.arcrobotics.ftclib.gamepad.GamepadEx
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import io.liftgate.robotics.mono.Mono.commands
import io.liftgate.robotics.mono.gamepad.ButtonType
import org.riverdell.robotics.HypnoticOpMode
import org.riverdell.robotics.HypnoticRobot
import org.riverdell.robotics.subsystems.CompositeIntake
import org.riverdell.robotics.subsystems.CompositeOuttake
import org.riverdell.robotics.subsystems.IV4B
import org.riverdell.robotics.subsystems.Intake
import org.riverdell.robotics.subsystems.OV4B
import org.riverdell.robotics.subsystems.Outtake
import org.riverdell.robotics.subsystems.Outtake.ClawState
import org.riverdell.robotics.subsystems.SlideConfig
import kotlin.math.absoluteValue

@TeleOp(
    group = "Drive"
)
class DuoZoom : HypnoticOpMode() {
    override fun buildRobot() = TeleOpRobot()

    inner class TeleOpRobot : HypnoticRobot(this@DuoZoom) {
        private val gp1Commands by lazy { commands(gamepad1) }
        private val gp2Commands by lazy { commands(gamepad2) }
        //val visionPipeline by lazy { VisionPipeline(this@SoloZoom) }
//    val wrist by lazy { hardware<Servo>("intake_wrist") }

        override fun additionalSubSystems() = listOf(gp1Commands, gp2Commands)
        override fun initialize() {
//        wrist.position = 0.5
//        visionPipeline.sampleDetection.supplyCurrentWristPosition { wrist.position }
//        visionPipeline.sampleDetection.setDetectionType(SampleType.Blue)

            while (!isStarted) {
                multipleTelemetry.addLine("Configured all subsystems. Waiting for start...")
                multipleTelemetry.update()
                runPeriodics()
            }
        }

        override fun opModeStart() {
            val robotDriver = GamepadEx(gamepad1)
            buildCommands()
            while (opModeIsActive()) {
                if ((compositein.currentIntakeState == CompositeIntake.IntakeState.Intake)) {
                    val multiplier = 0.62
                    drivetrain.driveRobotCentric(robotDriver, multiplier)
                    val wantedPower = -opMode.gamepad1.left_trigger + opMode.gamepad1.right_trigger
                    if (wantedPower.absoluteValue > 0.1 && !extension.slides.isTravelling()) {
                        if (wantedPower < 0) {
                            if (extension.slides.currentPosition() >= -10) {
                                extension.slides.supplyPowerToAll(0.0)
                            } else {
                                extension.slides.supplyPowerToAll(-wantedPower.toDouble() / 2.0)
                            }
                        } else {
                            if (extension.slides.currentPosition() < -435) {
                                extension.slides.supplyPowerToAll(0.0)
                            } else {
                                extension.slides.supplyPowerToAll(-wantedPower.toDouble() / 2.0)
                            }
                        }
                    } else if (!extension.slides.isTravelling()) {
                        extension.slides.supplyPowerToAll(0.0)
                    }
                }
                else{
                    val multiplier = 0.6 + gamepad1.right_trigger * 0.4
                    drivetrain.driveRobotCentric(robotDriver, multiplier)
                }

                telemetry.addLine("Composite State: ${compositein.currentIntakeState}")
                telemetry.addLine("Extendo Left Position: ${hardware.extensionMotorLeft.currentPosition}")
                telemetry.addLine("Extendo Right Position: ${hardware.extensionMotorRight.currentPosition}")
                telemetry.update()

                gp1Commands.run()
                gp2Commands.run()
                runPeriodics()
            }
        }

        private fun buildCommands() {

            //grab positions
            gp1Commands.apply {
                where(ButtonType.ButtonA)
                    .triggers {
                        intake.setIntakeGrip(Intake.ClawState.Open)
                        intake.setRotationPulley(Intake.RotationState.Grab)
                        iv4b.setV4B(IV4B.V4BState.Grab).thenCompose { intake.setIntakeGrip(Intake.ClawState.Closed) }.thenCompose { iv4b.setV4B(IV4B.V4BState.Observe) }
                        intake.setRotationPulley(Intake.RotationState.Observe)
                        // intake.toggleIntakeGrip()
                    }
                    .whenPressedOnce()

                where(ButtonType.BumperLeft)
                    .triggers {
                        compositein.toggle()
                    }
                    .whenPressedOnce()

                where(ButtonType.DPadDown)
                    .triggers {
                        intake.setWrist(Intake.WristState.Vertical)
                    }
                    .whenPressedOnce()

                where(ButtonType.DPadLeft)
                    .triggers {
                        intake.setWrist(Intake.WristState.Left)
                    }
                    .whenPressedOnce()

                where(ButtonType.DPadRight)
                    .triggers {
                        intake.setWrist(Intake.WristState.Right)
                    }
                    .whenPressedOnce()

                where(ButtonType.DPadUp)
                    .triggers {
                        intake.setWrist(Intake.WristState.Front)

                    }
                    .whenPressedOnce()

            }

            gp2Commands.apply {
                where(ButtonType.BumperRight)
                    .triggers {
                        compositeout.toggleBucket()
                    }
                    .whenPressedOnce()

                where(ButtonType.ButtonB)
                    .triggers {
                        outtake.toggleOuttakeGrip()
                    }
                    .whenPressedOnce()
                where(ButtonType.ButtonY)
                    .triggers {
                        compositeout.toggleSpecimen()
                    }
                    .whenPressedOnce()
                where(ButtonType.ButtonX)
                    .triggers {
                        compositeout.setOuttake(CompositeOuttake.OuttakeState.Transfer)
                    }
                    .whenPressedOnce()
            }

            gp1Commands.doButtonUpdatesManually()
            gp2Commands.doButtonUpdatesManually()
        }
    }
}