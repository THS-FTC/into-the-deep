package org.riverdell.robotics.subsystems

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import io.liftgate.robotics.mono.konfig.konfig
import io.liftgate.robotics.mono.states.StateResult
import io.liftgate.robotics.mono.subsystem.AbstractSubsystem
import kotlinx.serialization.Serializable
import org.riverdell.robotics.utilities.motionprofile.MotionProfileConstraints
import java.util.concurrent.CompletableFuture

class CompositeOuttake(opMode: LinearOpMode) : AbstractSubsystem()
{

    enum class OuttakeState
    {
        Idle,Placing
    }

    fun toggleOuttake



    //toggles the intake grip
    fun toggleIntakeGrip(): CompletableFuture<StateResult>
    {
        return if (currentClawState == ClawState.Closed)
        {
            gripRotateTo(IntakeConfig.openPosition).apply {
                currentClawState = ClawState.Open
            }
        } else
        {
            gripRotateTo(IntakeConfig.closePositon).apply {
                currentClawState = ClawState.Closed
            }
        }
    }
    //sets intake grip to either open or close
    fun setIntakeGrip(newState: ClawState): CompletableFuture<Void>
    {
        if (currentClawState == newState)
        {
            return CompletableFuture.completedFuture(null)
        }

        return if (newState == ClawState.Open)
        {
            gripRotateTo(IntakeConfig.openPosition)
                .thenAccept {
                    println(it)
                }
        } else
        {
            gripRotateTo(IntakeConfig.closePositon)
                .thenAccept {
                    println(it)
                }
        }
    }

    fun setRotationPulley(newState: RotationState): CompletableFuture<Void>
    {
        if (currentrotationState == newState)
        {
            return CompletableFuture.completedFuture(null)
        }

        return if (newState == RotationState.Transfer)
        {
            pulleyRotateTo(IntakeConfig.transferPosition)
                .thenAccept {
                    println(it)
                }
        } else if (newState == RotationState.Grab){
            pulleyRotateTo(IntakeConfig.grabPosition)
                .thenAccept {
                    println(it)
                }
        }
        else
        {
            pulleyRotateTo(IntakeConfig.observePosition)
                .thenAccept {
                    println(it)
                }
        }
    }


    override fun start()
    {

    }

    override fun doInitialize()
    {
//        gripRotateTo(intakeConfig.get().openPosition)
//        wristRotateTo(intakeConfig.get().frontPosition)
//        setRotationPulley(RotationState.Observe)
    }
}