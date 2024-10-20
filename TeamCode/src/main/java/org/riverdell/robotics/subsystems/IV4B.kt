package org.riverdell.robotics.subsystems

import io.liftgate.robotics.mono.konfig.konfig
import io.liftgate.robotics.mono.states.StateResult
import io.liftgate.robotics.mono.subsystem.AbstractSubsystem
import kotlinx.serialization.Serializable
import org.riverdell.robotics.HypnoticRobot
import org.riverdell.robotics.subsystems.Intake.ClawState
import org.riverdell.robotics.subsystems.Intake.RotationState
import org.riverdell.robotics.utilities.motionprofile.MotionProfileConstraints
import java.util.concurrent.CompletableFuture

class IV4B(opMode: HypnoticRobot) : AbstractSubsystem()
{
    @Serializable
    data class V4BConfig(
        val leftIsReversed: Boolean = false,
        val idlePosition: Double = 0.0,
        val transferPosition: Double = 0.0,
        val observePosition: Double = 0.8,
        val grabPosition: Double = 0.4,
        val moveAwayPosition: Double = 0.5,
    )
    enum class V4BState
    {
        Observe,Transfer,Grab,Idle,MoveAway
    }
    private val v4bConfig = konfig<V4BConfig>()
    private val currentV4BState = V4BState.Idle

    private val rotationConstraints = konfig<MotionProfileConstraints> { withCustomFileID("v4b_rotation_motionprofile") }
    private val leftRotation = motionProfiledServo("iv4b_rotation_left", rotationConstraints)
    private val rightRotation = motionProfiledServo("iv4b_rotation_right", rotationConstraints)

    fun v4bRotateTo(position: Double) = CompletableFuture.allOf(
        leftRotation.setMotionProfileTarget(
            if (v4bConfig.get().leftIsReversed)
                (1.0 - position) else position
        ),
        rightRotation.setMotionProfileTarget(
            if (!v4bConfig.get().leftIsReversed)
                (1.0 - position) else position
        )
    )
    fun setV4B(newState: V4BState): CompletableFuture<Void>
    {
        if (currentV4BState == newState)
        {
            return CompletableFuture.completedFuture(null)
        }

        return if (newState == V4BState.Transfer)
        {
            v4bRotateTo(v4bConfig.get().transferPosition)
                .thenAccept {
                    println(it)
                }
        } else if (newState == V4BState.Grab){
            v4bRotateTo(v4bConfig.get().grabPosition)
                .thenAccept {
                    println(it)
                }
        }
        else if (newState == V4BState.Idle){
            v4bRotateTo(v4bConfig.get().idlePosition)
                .thenAccept {
                    println(it)
                }
        }
        else if (newState == V4BState.MoveAway){
            v4bRotateTo(v4bConfig.get().moveAwayPosition)
                .thenAccept {
                    println(it)
                }
        }
        else
        {
            v4bRotateTo(v4bConfig.get().observePosition)
                .thenAccept {
                    println(it)
                }
        }
    }

    /*fun toggleOuttakeRotate(): CompletableFuture<StateResult>
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

     */
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
        setV4B(V4BState.Observe)
    }
}