package org.riverdell.robotics.subsystems

import io.liftgate.robotics.mono.subsystem.AbstractSubsystem
import org.riverdell.robotics.HypnoticRobot
import org.riverdell.robotics.utilities.motionprofile.Constraint
import java.util.concurrent.CompletableFuture

class IV4B(private val robot: HypnoticRobot) : AbstractSubsystem()
{
//    @Serializable
//    data class V4BConfig(
//        val leftIsReversed: Boolean = false,
//        val idlePosition: Double = 0.0,
//        val transferPosition: Double = 0.0,
//        val observePosition: Double = 0.8,
//        val grabPosition: Double = 0.3,
//        val moveAwayPosition: Double = 0.5,
//    )
    enum class V4BState
    {
        Observe, Transfer, Grab, Idle, MoveAway, Init
    }
//    private val v4bConfig = konfig<V4BConfig>()
    private val currentV4BState = V4BState.Init
    private val leftRotation = motionProfiledServo(robot.hardware.intakeV4BLeft, Constraint.HALF.scale(10.0))
    private val rightRotation = motionProfiledServo(robot.hardware.intakeV4BRight, Constraint.HALF.scale(10.0))

    fun v4bRotateTo(position: Double) = CompletableFuture.allOf(
        leftRotation.setMotionProfileTarget(
            if (IV4BConfig.leftIsReversed)
                (1.0 - position) else position
        ),
        rightRotation.setMotionProfileTarget(
            if (!IV4BConfig.leftIsReversed)
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
            v4bRotateTo(IV4BConfig.transferPosition)
                .thenAccept {
                    println(it)
                }
        } else if (newState == V4BState.Grab){
            v4bRotateTo(IV4BConfig.grabPosition)
                .thenAccept {
                    println(it)
                }
        }
        else if (newState == V4BState.Idle){
            v4bRotateTo(IV4BConfig.idlePosition)
                .thenAccept {
                    println(it)
                }
        }
        else if (newState == V4BState.MoveAway){
            v4bRotateTo(IV4BConfig.moveAwayPosition)
                .thenAccept {
                    println(it)
                }
        }
        else
        {
            v4bRotateTo(IV4BConfig.observePosition)
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
//        setV4B(V4BState.Observe)
    }
}