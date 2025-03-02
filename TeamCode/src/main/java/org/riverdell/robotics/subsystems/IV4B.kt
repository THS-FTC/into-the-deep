package org.riverdell.robotics.subsystems

import io.liftgate.robotics.mono.subsystem.AbstractSubsystem
import org.riverdell.robotics.HypnoticRobot
import org.riverdell.robotics.IV4BInterface
import org.riverdell.robotics.utilities.motionprofile.Constraint
import java.util.concurrent.CompletableFuture

class IV4B(private val robot: HypnoticRobot) : AbstractSubsystem(), IV4BInterface {

    enum class V4BState {
        Observe, Transfer, Grab, Idle, MoveAway, Init, Hidden
    }

    object IV4BConfig {
        val transferPosition: Double = 0.3
        val grabPosition: Double = 0.6
        val hiddenPosition: Double = 0.0
        val idlePosition: Double = 0.5
        val moveAwayPosition: Double = 0.8
        val observePosition: Double = 0.2
    }

    private var currentV4BState = V4BState.Init

    // Example: two motionProfiledServo references from your hardware
    private val leftRotation = motionProfiledServo(robot.hardware.intakeV4BLeft, Constraint.HALF.scale(5.0))
    private val rightRotation = motionProfiledServo(robot.hardware.intakeV4BRight, Constraint.HALF.scale(5.0))

    override fun v4bRotateTo(position: Double): CompletableFuture<Void> =
        CompletableFuture.allOf(
            leftRotation.setMotionProfileTarget(position),
            rightRotation.setMotionProfileTarget(position)
        )

    override fun leftDiffyRotate(position: Double): CompletableFuture<Void> =
        CompletableFuture.allOf(
            leftRotation.setMotionProfileTarget(position)
        )

    override fun rightDiffyRotate(position: Double): CompletableFuture<Void> =
        CompletableFuture.allOf(
            rightRotation.setMotionProfileTarget(position)
        )

    fun setV4B(newState: V4BState): CompletableFuture<Void> {
        if (currentV4BState == newState) {
            return CompletableFuture.completedFuture(null)
        }
        val future = when (newState) {
            V4BState.Transfer -> v4bRotateTo(IV4BConfig.transferPosition)
            V4BState.Grab -> v4bRotateTo(IV4BConfig.grabPosition)
            V4BState.Hidden -> v4bRotateTo(IV4BConfig.hiddenPosition)
            V4BState.Idle -> v4bRotateTo(IV4BConfig.idlePosition)
            V4BState.MoveAway -> v4bRotateTo(IV4BConfig.moveAwayPosition)
            V4BState.Observe -> v4bRotateTo(IV4BConfig.observePosition)
            else -> v4bRotateTo(IV4BConfig.idlePosition)
        }
        currentV4BState = newState
        return future
    }

    override fun start() {}
    override fun doInitialize() {}
}
