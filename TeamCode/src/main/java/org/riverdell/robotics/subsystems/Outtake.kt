package org.riverdell.robotics.subsystems

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import io.liftgate.robotics.mono.konfig.konfig
import io.liftgate.robotics.mono.states.StateResult
import io.liftgate.robotics.mono.subsystem.AbstractSubsystem
import kotlinx.serialization.Serializable
import org.riverdell.robotics.utilities.motionprofile.Constraint
import org.riverdell.robotics.utilities.motionprofile.ProfileConstraints
import java.util.concurrent.CompletableFuture

class Outtake(opMode: LinearOpMode) : AbstractSubsystem()
{
//    @Serializable
//    data class OuttakeConfig(
//        val openPosition: Double = 0.5,
//        val closedPosition: Double = 0.0,
//
//        val frontPosition: Double = 0.0,
//        val backPosition: Double = 0.5,
//
//        val transferPosition: Double = 0.4,
//        val outtakePositon: Double = 0.0,
//    )

    enum class ClawState
    {
        Closed, Open, Idle
    }
    enum class WristState
    {
        Front, Back , Idle
    }

//    private val outtakeConfig = konfig<OuttakeConfig>()
//        .apply {
//            println("HORS ${get().openPosition}")
//        }

//    fun wristRotateTo(position: Double) = wrist.setMotionProfileTarget(position)
    fun wristRotateTo(position: Double): CompletableFuture<Void>{
        wrist.forcefullySetTarget(position)
        return CompletableFuture.completedFuture(null)
    }
    fun gripRotateTo(position: Double): CompletableFuture<Void>{
        actuator.forcefullySetTarget(position)
        return CompletableFuture.completedFuture(null)
    }

    private val wrist = motionProfiledServo("outtake_wrist", Constraint.HALF.scale(5.0))
    private val actuator = motionProfiledServo("outtake_grip", Constraint.HALF.scale(5.0)) // change to outtakegrip

    private var currentClawState = ClawState.Idle
    private var currentWristState = WristState.Idle

    fun setOuttakeGrip(newState: ClawState): CompletableFuture<Void>
    {
        if (currentClawState == newState)
        {
            return CompletableFuture.completedFuture(null)
        }

        return if (newState == ClawState.Open)
        {
            gripRotateTo(OutakeConfig.openPosition)
                .thenAccept {
                    println(it)
                }
        } else
        {
            gripRotateTo(OutakeConfig.closePositon)
                .thenAccept {
                    println(it)
                }
        }
    }
    fun setWrist(newState: WristState): CompletableFuture<Void>
    {
        if (currentWristState == newState)
        {
            return CompletableFuture.completedFuture(null)
        }

        return if (newState == WristState.Front)
        {
            wristRotateTo(OutakeConfig.frontPosition)
                .thenAccept {
                    println(it)
                }
        } else {
            wristRotateTo(OutakeConfig.backPosition)
                .thenAccept {
                    println(it)
                }
        }

    }
    fun toggleOuttakeGrip(): CompletableFuture<Void>
    {
        return if (currentClawState == ClawState.Closed)
        {
            gripRotateTo(OutakeConfig.openPosition).apply {
                currentClawState = ClawState.Open
            }
        } else
        {
            gripRotateTo(OutakeConfig.closePositon).apply {
                currentClawState = ClawState.Closed
            }
        }
    }

    fun toggleOuttakeRotation(state: WristState = WristState.Front): CompletableFuture<Void>
    {
        if (currentWristState == state)
        {
            return CompletableFuture.completedFuture(null)
        }

        return if (state == WristState.Front)
        {
            wristRotateTo(OutakeConfig.frontPosition)
        } else
        {
            wristRotateTo(OutakeConfig.backPosition)
        }
    }

    override fun start()
    {

    }

    override fun doInitialize()
    {
//        setWrist(WristState.Front)
//        setOuttakeGrip(ClawState.Open)
    }
}