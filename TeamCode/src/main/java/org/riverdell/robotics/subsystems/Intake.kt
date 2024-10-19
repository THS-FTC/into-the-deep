package org.riverdell.robotics.subsystems

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import io.liftgate.robotics.mono.konfig.konfig
import io.liftgate.robotics.mono.states.StateResult
import io.liftgate.robotics.mono.subsystem.AbstractSubsystem
import kotlinx.serialization.Serializable
import org.riverdell.robotics.subsystems.Outtake.OuttakeState
import org.riverdell.robotics.utilities.motionprofile.MotionProfileConstraints
import java.util.concurrent.CompletableFuture

class Intake(opMode: LinearOpMode) : AbstractSubsystem()
{
    @Serializable
    data class IntakeConfig(
        //claw
        val openPosition: Double = 1.0,
        val closedPosition: Double = 0.5,

        //rotation
        val transferPosition: Double = 0.0,
        val observePosition: Double = 0.6,
        val grabPosition: Double = 0.4,

        // wrist
        val frontPosition: Double = 0.0,
        val backPosition: Double = 1.0,
    )

    enum class ClawState
    {
        Closed, Open
    }
    enum class WristState
    {
        Front, Back
    }
    enum class RotationState
    {
        Transfer, Observe, Grab
    }

    private var clawState = ClawState.Closed
    private var wristState = WristState.Front
    private var rotationState = RotationState.Grab

    private val wristConstraints = konfig<MotionProfileConstraints> { withCustomFileID("intake_wrist_motionprofile") }
    private val wrist = motionProfiledServo("intake_wrist", wristConstraints)

    private val pulleyConstraints = konfig<MotionProfileConstraints> { withCustomFileID("intake_pulley_motionprofile") }
    private val pulley = motionProfiledServo("intake_pulley", pulleyConstraints)

    private val rotationConstraints = konfig<MotionProfileConstraints> { withCustomFileID("intake_grip_motionprofile") }
    private val grip = motionProfiledServo("intake_grip", rotationConstraints)

    fun wristRotateTo(position: Double) = wrist.setMotionProfileTarget(position)
    fun gripRotateTo(position: Double) = grip.setMotionProfileTarget(position)

    fun toggleOuttakeGrip(state: OuttakeState = OuttakeState.Open): CompletableFuture<StateResult>
    {
        TODO()
    }

    override fun start()
    {

    }

    override fun doInitialize()
    {

    }
}