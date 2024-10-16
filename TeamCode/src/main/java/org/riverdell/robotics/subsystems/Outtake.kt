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
        val openPosition: Double = 1.0,
        val closedPosition: Double = 0.5,
        val frontPosition: Double = 0.0,
        val backPosition: Double = 1.0,
    )

    enum class OuttakeState
    {
        Closed, Open
    }
    enum class WristState
    {
        Front, Back
    }

    private val outtakeConfig = konfig<OuttakeConfig>()

    private val wristConstraints = konfig<MotionProfileConstraints> { withCustomFileID("outtake_wrist_motionprofile") }
    private val wrist = motionProfiledServo("outtake_wrist", wristConstraints)

    private val rotationConstraints = konfig<MotionProfileConstraints> { withCustomFileID("outtake_grip_motionprofile") }
    private val actuator = motionProfiledServo("outtake_grip", rotationConstraints)

    private var currentOuttakeState = OuttakeState.Open
    private var currentWristState = WristState.Front

    // extendoRetract
    // intakeTransfer
    // outtake active

    fun idle() = toggleOuttakeRotation(WristState.Front)
        .thenAccept {
            println("something happened")
        }
        .thenCompose {
            toggleOuttakeGrip(OuttakeState.Open)
        }


    fun active() = toggleOuttakeGrip(OuttakeState.Closed)
        // TODO: release intake grip
        /*.thenCompose {
            toggleIntakeGrip(IntakeState.Open)
        }*/
        .thenCompose {
            toggleOuttakeRotation(WristState.Back)
        }

    fun toggleOuttakeGrip(state: OuttakeState = OuttakeState.Open): CompletableFuture<StateResult>
    {
        if (currentOuttakeState == state)
        {
            return CompletableFuture.completedFuture(null)
        }

        return if (state == OuttakeState.Open)
        {
            gripRotateTo(outtakeConfig.get().openPosition)
        } else
        {
            gripRotateTo(outtakeConfig.get().closedPosition)
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


    fun wristRotateTo(position: Double) = wrist.setMotionProfileTarget(position)
    fun gripRotateTo(position: Double) = actuator.setMotionProfileTarget(position)

    override fun start()
    {

    }

    override fun doInitialize()
    {

    }
}