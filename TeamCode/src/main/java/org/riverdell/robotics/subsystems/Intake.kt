package org.riverdell.robotics.subsystems

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import io.liftgate.robotics.mono.konfig.konfig
import io.liftgate.robotics.mono.states.StateResult
import io.liftgate.robotics.mono.subsystem.AbstractSubsystem
import kotlinx.serialization.Serializable
import org.riverdell.robotics.utilities.motionprofile.Constraint
import org.riverdell.robotics.utilities.motionprofile.ProfileConstraints
import java.util.concurrent.CompletableFuture

class Intake(opMode: LinearOpMode) : AbstractSubsystem()
{
//    @Serializable
//    data class IntakeConfig(
//        //claw
//        val openPosition: Double = 0.4,
//        val closedPosition: Double = 1.0,
//
//        //rotation
//        val transferPosition: Double = 1.0,
//        val observePosition: Double = 0.0,
//        val grabPosition: Double = 0.0,
//
//        // wrist
//        val frontPosition: Double = 0.0,
//        val backPosition: Double = 1.0,
//    )

    enum class ClawState
    {
        Closed, Open,Idle
    }
    enum class WristState
    {
        Front, Back, Idle
    }
    enum class RotationState
    {
        Transfer, Observe, Grab,Idle
    }


    private var currentClawState = ClawState.Idle
    private var currentwristState = WristState.Idle
    private var currentrotationState = RotationState.Idle

    private val wrist = motionProfiledServo("intake_wrist", Constraint.HALF.scale(5.0))
    private val pulley = motionProfiledServo("intake_pulley", Constraint.HALF.scale(5.0))
    private val grip = motionProfiledServo("intake_grip", Constraint.HALF.scale(5.0))

    fun wristRotateTo(position: Double): CompletableFuture<Void>{
        wrist.forcefullySetTarget(position)
        return CompletableFuture.completedFuture(null)
    }
    fun gripRotateTo(position: Double) = grip.setMotionProfileTarget(position)
    fun pulleyRotateTo(position: Double): CompletableFuture<Void>{
        pulley.forcefullySetTarget(position)
        return CompletableFuture.completedFuture(null)
    }

    //toggles the intake grip
    fun toggleIntakeGrip(): CompletableFuture<StateResult>
    {
        return if (currentClawState == ClawState.Closed)
        {
            gripRotateTo(IntakeConfig.openPositon).apply {
                currentClawState = ClawState.Open
            }
        } else
        {
            gripRotateTo(IntakeConfig.closePosition).apply {
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
            gripRotateTo(IntakeConfig.openPositon)
                .thenAccept {
                    println(it)
                }
        } else
        {
            gripRotateTo(IntakeConfig.closePosition)
                .thenAccept {
                    println(it)
                }
        }
    }
    fun setWrist(newState: WristState): CompletableFuture<Void>
    {
        if (currentwristState == newState)
        {
            return CompletableFuture.completedFuture(null)
        }

        return if (newState == WristState.Front)
        {
            wristRotateTo(IntakeConfig.frontPosition)
                .thenAccept {
                    println(it)
                }
        } else
        {
            wristRotateTo(IntakeConfig.backPosition)
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
        setIntakeGrip(ClawState.Open)
        setRotationPulley(RotationState.Observe)
        setWrist(WristState.Front)
    }
}