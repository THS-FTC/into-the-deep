package org.riverdell.robotics.subsystems

import io.liftgate.robotics.mono.subsystem.AbstractSubsystem
import org.riverdell.robotics.HypnoticRobot
import org.riverdell.robotics.utilities.motionprofile.Constraint
import java.util.concurrent.CompletableFuture

class IV4B(private val robot: HypnoticRobot) : AbstractSubsystem() {

    enum class V4BState {
        Observe, Transfer, Grab, Idle, MoveAway, Init, Hidden
    }

    // Create motion-profiled servo objects using the hardware from the robot.
    private val leftRotation = motionProfiledServo(robot.hardware.intakeV4BLeft, Constraint.HALF.scale(5.0))
    private val rightRotation = motionProfiledServo(robot.hardware.intakeV4BRight, Constraint.HALF.scale(5.0))

    /**
     * Rotates both diffy servos to the given target position.
     */
    fun v4bRotateTo(position: Double): CompletableFuture<Void> =
        CompletableFuture.allOf(
            leftRotation.setMotionProfileTarget(position),
            rightRotation.setMotionProfileTarget(position)
        )

    fun leftDiffyRotate(position: Double) = leftRotation.setMotionProfileTarget(position)
    fun rightDiffyRotate(position: Double) = rightRotation.setMotionProfileTarget(position)

    /**
     * Sets the IV4B to one of its configured states.
     * (Configuration values should be defined as needed.)
     */
    fun setV4B(newState: V4BState): CompletableFuture<Void> {
        // If the state is already the current state, return immediately.
        // (You might store the state if desired; here we simply execute the command.)
        return when (newState) {
            V4BState.Transfer -> v4bRotateTo(IV4BConfig.transferPosition)
            V4BState.Grab -> v4bRotateTo(IV4BConfig.grabPosition)
            V4BState.Hidden -> v4bRotateTo(IV4BConfig.hiddenPosition)
            V4BState.Idle -> v4bRotateTo(IV4BConfig.idlePosition)
            V4BState.MoveAway -> v4bRotateTo(IV4BConfig.moveAwayPosition)
            else -> v4bRotateTo(IV4BConfig.observePosition)
        }
    }

    override fun start() {
        // Optional start code.
    }

    override fun doInitialize() {
        // Optional initialization code.
    }
}

/**
 * Configuration object for IV4B.
 * Adjust these values as needed.
 */
object IV4BConfig {
    val transferPosition: Double = 0.3
    val grabPosition: Double = 0.6
    val hiddenPosition: Double = 0.0
    val idlePosition: Double = 0.5
    val moveAwayPosition: Double = 0.8
    val observePosition: Double = 0.2
}
