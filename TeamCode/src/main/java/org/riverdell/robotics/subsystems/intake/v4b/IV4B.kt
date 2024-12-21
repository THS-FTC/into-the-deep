package org.riverdell.robotics.subsystems.intake.v4b

import io.liftgate.robotics.mono.subsystem.AbstractSubsystem
import org.riverdell.robotics.HypnoticRobot
import org.riverdell.robotics.subsystems.motionProfiledServo
import org.riverdell.robotics.utilities.managed.ServoBehavior
import org.riverdell.robotics.utilities.motionprofile.Constraint
import java.util.concurrent.CompletableFuture

class IV4B(robot: HypnoticRobot) : AbstractSubsystem()
{
    private val leftRotation = motionProfiledServo(robot.hardware.intakeV4BLeft, Constraint.HALF.scale(30.5))
    private val rightRotation = motionProfiledServo(robot.hardware.intakeV4BRight, Constraint.HALF.scale(30.5))

    var V4bState = iv4bState.Idle

    fun v4bObserve() = setV4B(iv4bState.Observe)
    fun v4bTransfer() = setV4B(iv4bState.Transfer)
    fun v4bIdle() = setV4B(iv4bState.Idle)
    fun v4bMoveAway() = setV4B(iv4bState.MoveAway)
    fun v4bGrab() = setV4B(iv4bState.Grab)

    fun setV4B(state: iv4bState) = let {
        if (state == V4bState)
        {
            println("Same state! $state and $V4bState")
            return@let CompletableFuture.completedFuture(null)
        }

        V4bState = state
        return@let updateFourBarState()
    }

    private fun updateFourBarState(): CompletableFuture<*>
    {
        return v4bRotateTo(V4bState.position)
    }

    private fun v4bRotateTo(position: Double) = CompletableFuture.allOf(
        leftRotation.setTarget(1.0 - position, ServoBehavior.MotionProfile),
        rightRotation.setTarget(position, ServoBehavior.MotionProfile)
    )

    override fun start()
    {

    }

    override fun doInitialize()
    {
        updateFourBarState()
    }
}