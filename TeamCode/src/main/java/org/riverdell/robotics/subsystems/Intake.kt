package org.riverdell.robotics.subsystems

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import io.liftgate.robotics.mono.konfig.konfig
import io.liftgate.robotics.mono.states.StateResult
import io.liftgate.robotics.mono.subsystem.AbstractSubsystem
import kotlinx.serialization.Serializable
import org.riverdell.robotics.subsystems.Outtake.OuttakeConfig
import org.riverdell.robotics.subsystems.Outtake.OuttakeState
import org.riverdell.robotics.utilities.motionprofile.MotionProfileConstraints
import java.util.concurrent.CompletableFuture

class Intake(opMode: LinearOpMode) : AbstractSubsystem()
{
    @Serializable
    data class IntakeConfig(
        //claw
        val openPosition: Double = 0.4,
        val closedPosition: Double = 1.0,

        //rotation
        val transferPosition: Double = 0.4,
        val observePosition: Double = 0.0,
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
        Transfer, Observe, Grab,Idle
    }

    private val intakeConfig = konfig<IntakeConfig>()
        .apply {
            println("HORS ${get().openPosition}")
        }
    private var currentClawState = ClawState.Closed
    private var currentwristState = WristState.Front
    private var currentrotationState = RotationState.Idle

    private val wristConstraints = konfig<MotionProfileConstraints> { withCustomFileID("intake_wrist_motionprofile") }
    private val wrist = motionProfiledServo("intake_wrist", wristConstraints)

    private val pulleyConstraints = konfig<MotionProfileConstraints> { withCustomFileID("intake_pulley_motionprofile") }
    private val pulley = motionProfiledServo("intake_pulley", pulleyConstraints)

    private val rotationConstraints = konfig<MotionProfileConstraints> { withCustomFileID("intake_grip_motionprofile") }
    private val grip = motionProfiledServo("intake_grip", rotationConstraints)

    fun wristRotateTo(position: Double) = wrist.setMotionProfileTarget(position)
    fun gripRotateTo(position: Double) = grip.setMotionProfileTarget(position)
    fun pulleyRotateTo(position: Double) = pulley.setMotionProfileTarget(position)

    //toggles the intake grip
    fun toggleIntakeGrip(): CompletableFuture<StateResult>
    {
        return if (currentClawState == ClawState.Closed)
        {
            gripRotateTo(intakeConfig.get().openPosition).apply {
                currentClawState = ClawState.Open
            }
        } else
        {
            gripRotateTo(intakeConfig.get().closedPosition).apply {
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
            gripRotateTo(intakeConfig.get().openPosition)
                .thenAccept {
                    println(it)
                }
        } else
        {
            gripRotateTo(intakeConfig.get().closedPosition)
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
            pulleyRotateTo(intakeConfig.get().transferPosition)
                .thenAccept {
                    println(it)
                }
        } else if (newState == RotationState.Grab){
            pulleyRotateTo(intakeConfig.get().grabPosition)
                .thenAccept {
                    println(it)
                }
        }
        else
        {
            pulleyRotateTo(intakeConfig.get().observePosition)
                .thenAccept {
                    println(it)
                }
        }
    }


    override fun start()
    {
        gripRotateTo(intakeConfig.get().openPosition)
    }

    override fun doInitialize()
    {
        gripRotateTo(intakeConfig.get().openPosition)
        wristRotateTo(intakeConfig.get().frontPosition)
    }
}