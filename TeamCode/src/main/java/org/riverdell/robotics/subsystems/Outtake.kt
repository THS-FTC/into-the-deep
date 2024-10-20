package org.riverdell.robotics.subsystems

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import io.liftgate.robotics.mono.konfig.konfig
import io.liftgate.robotics.mono.states.StateResult
import io.liftgate.robotics.mono.subsystem.AbstractSubsystem
import kotlinx.serialization.Serializable
import org.riverdell.robotics.utilities.motionprofile.MotionProfileConstraints
import java.util.concurrent.CompletableFuture

class Outtake(opMode: LinearOpMode) : AbstractSubsystem()
{
    @Serializable
    data class OuttakeConfig(
        val openPosition: Double = 0.5,
        val closedPosition: Double = 0.0,

        val frontPosition: Double = 0.0,
        val backPosition: Double = 0.5,

        val transferPosition: Double = 0.4,
        val outtakePositon: Double = 0.0,
    )

    enum class ClawState
    {
        Closed, Open
    }
    enum class WristState
    {
        Front, Back
    }

    private val outtakeConfig = konfig<OuttakeConfig>()
        .apply {
            println("HORS ${get().openPosition}")
        }

    fun wristRotateTo(position: Double) = wrist.setMotionProfileTarget(position)
    fun gripRotateTo(position: Double) = actuator.setMotionProfileTarget(position)

    private val wristConstraints = konfig<MotionProfileConstraints> { withCustomFileID("outtake_wrist_motionprofile") }
    private val wrist = motionProfiledServo("outtake_wrist", wristConstraints)

    private val rotationConstraints = konfig<MotionProfileConstraints> { withCustomFileID("outtake_grip_motionprofile") }
    private val actuator = motionProfiledServo("outtake_grip", rotationConstraints) // change to outtakegrip

    private var currentClawState = ClawState.Open
    private var currentWristState = WristState.Front

    fun setOuttakeGrip(newState: ClawState): CompletableFuture<Void>
    {
        if (currentClawState == newState)
        {
            return CompletableFuture.completedFuture(null)
        }

        return if (newState == ClawState.Open)
        {
            gripRotateTo(outtakeConfig.get().openPosition)
                .thenAccept {
                    println(it)
                }
        } else
        {
            gripRotateTo(outtakeConfig.get().closedPosition)
                .thenAccept {
                    println(it)
                }
        }
    }

    fun toggleOuttakeGrip(): CompletableFuture<StateResult>
    {
        return if (currentClawState == ClawState.Closed)
        {
            gripRotateTo(outtakeConfig.get().openPosition).apply {
                currentClawState = ClawState.Open
            }
        } else
        {
            gripRotateTo(outtakeConfig.get().closedPosition).apply {
                currentClawState = ClawState.Closed
            }
        }
    }

    fun toggleOuttakeRotation(state: WristState = WristState.Front): CompletableFuture<StateResult>
    {
        if (currentWristState == state)
        {
            return CompletableFuture.completedFuture(null)
        }

        return if (state == WristState.Front)
        {
            wristRotateTo(outtakeConfig.get().frontPosition)
        } else
        {
            wristRotateTo(outtakeConfig.get().backPosition)
        }
    }

    override fun start()
    {

    }

    override fun doInitialize()
    {
        gripRotateTo(outtakeConfig.get().openPosition)
        wristRotateTo(outtakeConfig.get().frontPosition)
    }
}