package org.riverdell.robotics.subsystems

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import io.liftgate.robotics.mono.konfig.konfig
import io.liftgate.robotics.mono.states.StateResult
import io.liftgate.robotics.mono.subsystem.AbstractSubsystem
import kotlinx.serialization.Serializable
import org.riverdell.robotics.HypnoticRobot
import org.riverdell.robotics.utilities.motionprofile.ProfileConstraints
import java.util.concurrent.CompletableFuture

class CompositeIntake(val robot: HypnoticRobot) : AbstractSubsystem() {
    enum class IntakeState {
        Intake, Idle, Transfer, Init, SlideIn, SlideOut
    }

    var currentIntakeState = IntakeState.Init


    fun setIntake(newState: IntakeState): CompletableFuture<Void> {
        if (currentIntakeState == newState) {
            return CompletableFuture.completedFuture(null)
        }

        return if (newState == IntakeState.Idle) {
            //idle intake stuff here
            robot.intake.setIntakeGrip(Intake.ClawState.Open)
            robot.iv4b.setV4B(IV4B.V4BState.Idle)
            robot.intake.setRotationPulley(Intake.RotationState.Idle)
            robot.extension.extendToAndStayAt(SlideConfig.extendoClosed)
            robot.intake.wristRotateTo(IntakeConfig.frontPosition).apply {

                println("updated to idle")
                currentIntakeState = IntakeState.Idle
            }
        } else if (newState == IntakeState.SlideIn) {
            CompletableFuture.allOf(
                robot.extension.extendToAndStayAt(SlideConfig.extendoIntake)
            ).apply { currentIntakeState = IntakeState.SlideIn }
        } else if (newState == IntakeState.SlideOut) {
            CompletableFuture.allOf(
                robot.extension.extendToAndStayAt(SlideConfig.extendoIdle)
            ).apply { currentIntakeState = IntakeState.SlideIn }
        }
        else if (newState == IntakeState.Transfer) {
            CompletableFuture.allOf(
                robot.iv4b.setV4B(IV4B.V4BState.Transfer),
                robot.intake.setRotationPulley(Intake.RotationState.Transfer),
                robot.outtake.setOuttakeGrip(Outtake.ClawState.Open)
            )
                .thenComposeAsync {
                    CompletableFuture.allOf(
                        robot.intake.setWrist(Intake.WristState.Front)
                    ).join()
                    CompletableFuture.allOf(
                        robot.extension.extendToAndStayAt(SlideConfig.extendoTransfer)
                    )

                }.apply {
                    println("updated to transfer")
                    currentIntakeState = IntakeState.Transfer
                } //works idk i am him

        } else {
            //this is for observing/intaking
            robot.iv4b.setV4B(IV4B.V4BState.Observe) //set it to observe later
            robot.intake.setRotationPulley(Intake.RotationState.Observe) //set it to observe later
            robot.extension.extendToAndStayAt(SlideConfig.extendoIntake).thenRun {
                robot.extension.idle()
            }.apply {
                println("updated to intake")
                currentIntakeState = IntakeState.Intake
            }
        }
    }

    fun toggleIntake(): CompletableFuture<Void> {
        return if (currentIntakeState != IntakeState.Transfer && currentIntakeState != IntakeState.SlideIn) {
            setIntake(IntakeState.Transfer)
        } else {
            setIntake(IntakeState.Intake)
        }
    }

    //logic may be fucked, try it
    fun toggleSlides(): CompletableFuture<Void> {
        return if (currentIntakeState != IntakeState.SlideOut) {
            setIntake(IntakeState.SlideOut)
        } else {
            setIntake(IntakeState.SlideIn)
        }
    }

    override fun doInitialize() {
//        setIntake(IntakeState.Idle)
    }

    override fun start() {

    }
}