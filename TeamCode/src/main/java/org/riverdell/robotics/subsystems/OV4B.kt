package org.riverdell.robotics.subsystems

import io.liftgate.robotics.mono.konfig.konfig
import io.liftgate.robotics.mono.states.StateResult
import io.liftgate.robotics.mono.subsystem.AbstractSubsystem
import kotlinx.serialization.Serializable
import org.riverdell.robotics.HypnoticRobot
import org.riverdell.robotics.subsystems.IV4B.V4BState
import org.riverdell.robotics.subsystems.Intake.ClawState
import org.riverdell.robotics.utilities.motionprofile.MotionProfileConstraints
import java.util.concurrent.CompletableFuture

class OV4B(opMode: HypnoticRobot) : AbstractSubsystem()
{
    @Serializable
    data class V4BConfig(
        val leftIsReversed: Boolean = false,
        val idlePosition: Double = 0.0,

        val transferPosition: Double = 0.2,
        val outtakePosition: Double = 0.5,
    )
    enum class OV4BState
    {
        Transfer,Outtake,Idle
    }
    enum class PulleyState
    {
        Outtake,Intake,Idle
    }
    private val ov4bConfig = konfig<V4BConfig>()
    private val currentV4BState = OV4BState.Idle
    private var currentRotateState = PulleyState.Idle


    private val rotationConstraints = konfig<MotionProfileConstraints> { withCustomFileID("v4b_rotation_motionprofile") }
    private val leftRotation = motionProfiledServo("ov4b_rotation_left", rotationConstraints)
    private val rightRotation = motionProfiledServo("ov4b_rotation_right", rotationConstraints)

    private val clawConstraints = konfig<MotionProfileConstraints>()
    private val clawPulley = motionProfiledServo("ov4b_pulley", clawConstraints)

    fun pulleyRotateTo(position: Double) = clawPulley.setMotionProfileTarget(position)
    fun v4bRotateTo(position: Double) = CompletableFuture.allOf(
        leftRotation.setMotionProfileTarget(
            if (ov4bConfig.get().leftIsReversed)
                (1.0 - position) else position
        ),
        rightRotation.setMotionProfileTarget(
            if (!ov4bConfig.get().leftIsReversed)
                (1.0 - position) else position
        )
    )
    fun toggleOuttakeRotate(): CompletableFuture<StateResult>
    {
        return if (currentRotateState == PulleyState.Outtake)
        {
            pulleyRotateTo(ov4bConfig.get().transferPosition).apply {
                currentRotateState = PulleyState.Intake
            }
        } else
        {
            pulleyRotateTo(ov4bConfig.get().outtakePosition).apply {
                currentRotateState = PulleyState.Outtake
            }
        }
    }
    fun setV4B(newState: OV4BState): CompletableFuture<Void>
    {
        if (currentV4BState == newState)
        {
            return CompletableFuture.completedFuture(null)
        }

        return if (newState == OV4BState.Transfer)
        {
            v4bRotateTo(ov4bConfig.get().transferPosition)
                .thenAccept {
                    println(it)
                }
        } else if (newState == OV4BState.Outtake){
            v4bRotateTo(ov4bConfig.get().outtakePosition)
                .thenAccept {
                    println(it)
                }
        }
        else
        {
            v4bRotateTo(ov4bConfig.get().idlePosition)
                .thenAccept {
                    println(it)
                }
        }
    }
    /**
     * CompletableFuture.allOf(
     *  extendoMotorGroup.goTo(250).thenCompose { CompletableFuture.allOf(clawPivot.pivotOut(), extendoMotorGroup.goTo(500)) },
     *  v4b.setMotionProfileTarget(1.0)
     * ).thenCompose {
     *    claw.setMotionProfileTarget(0.5)
     * }
     */



    /**
     * CompletableFuture.allOf(
     *  extendoMotorGroup.goTo(500),
     *  v4b.setMotionProfileTarget(1.0)
     * ).thenCompose {
     *    claw.setMotionProfileTarget(0.5)
     * }
     */


    /**
     * tele op
     * - extendo
     *     - extension
     *     (while this happens)
     *          v4 starts rotation
     *
     */

    override fun start()
    {

    }

    override fun doInitialize()
    {
        currentRotateState = PulleyState.Intake
        pulleyRotateTo(ov4bConfig.get().transferPosition)
        setV4B(OV4BState.Transfer)
    }
}