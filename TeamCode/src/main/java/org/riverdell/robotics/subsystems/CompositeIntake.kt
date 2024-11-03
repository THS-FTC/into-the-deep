package org.riverdell.robotics.subsystems

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import io.liftgate.robotics.mono.konfig.konfig
import io.liftgate.robotics.mono.states.StateResult
import io.liftgate.robotics.mono.subsystem.AbstractSubsystem
import kotlinx.serialization.Serializable
import org.riverdell.robotics.HypnoticRobot
import org.riverdell.robotics.utilities.motionprofile.ProfileConstraints
import java.util.concurrent.CompletableFuture

class CompositeIntake(val robot : HypnoticRobot) : AbstractSubsystem()
{
    enum class IntakeState
    {
        Intake,Idle,Transfer,Init
    }

    private var currentIntakeState = IntakeState.Init


    fun setIntake(newState: IntakeState): CompletableFuture<Void>
    {
        if (currentIntakeState == newState)
        {
            return CompletableFuture.completedFuture(null)
        }

        return if (newState == IntakeState.Idle)
        {
            //idle intake stuff here
            robot.intake.setIntakeGrip(Intake.ClawState.Open)
            robot.iv4b.setV4B(IV4B.V4BState.Idle)
            robot.intake.setRotationPulley(Intake.RotationState.Idle)
            robot.extension.extendToAndStayAt(SlideConfig.extendoClosed)
            robot.intake.setWrist(Intake.WristState.Front).apply { currentIntakeState = IntakeState.Idle }
        } else if (newState == IntakeState.Transfer)
        {
           //this is for transfer
            robot.iv4b.setV4B(IV4B.V4BState.Transfer)
            robot.intake.setRotationPulley(Intake.RotationState.Transfer).thenCompose {  robot.extension.extendToAndStayAt(SlideConfig.extendoTransfer) } //works idk i am him
            robot.intake.setWrist(Intake.WristState.Front).apply { currentIntakeState = IntakeState.Transfer }
        }
        else {
            //this is for observing/intaking
            robot.iv4b.setV4B(IV4B.V4BState.Grab) //set it to observe later
            robot.intake.setRotationPulley(Intake.RotationState.Grab) //set it to observe later
            robot.extension.extendToAndStayAt(SlideConfig.extendoIntake)
            robot.intake.setWrist(Intake.WristState.Front).apply { currentIntakeState = IntakeState.Intake }
        }
    }
    fun toggle(): CompletableFuture<Void>
    {
        return if (currentIntakeState == IntakeState.Transfer || currentIntakeState == IntakeState.Init )
        {
            setIntake(IntakeState.Intake)
        } else
        {
           setIntake(IntakeState.Transfer)
        }
    }

    override fun doInitialize() {
//        setIntake(IntakeState.Idle)
    }

    override fun start() {

    }
}