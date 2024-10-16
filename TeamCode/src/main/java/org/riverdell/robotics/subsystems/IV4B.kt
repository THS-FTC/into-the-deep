package org.riverdell.robotics.subsystems

import io.liftgate.robotics.mono.konfig.konfig
import io.liftgate.robotics.mono.subsystem.AbstractSubsystem
import kotlinx.serialization.Serializable
import org.riverdell.robotics.HypnoticRobot
import org.riverdell.robotics.utilities.motionprofile.MotionProfileConstraints
import java.util.concurrent.CompletableFuture

class IV4B(opMode: HypnoticRobot) : AbstractSubsystem()
{
    @Serializable
    data class V4BConfig(val leftIsReversed: Boolean = false)

    private val v4bConfig = konfig<V4BConfig>()

    private val rotationConstraints = konfig<MotionProfileConstraints> { withCustomFileID("v4b_rotation_motionprofile") }
    private val leftRotation = motionProfiledServo("iv4b_rotation_left", rotationConstraints)
    private val rightRotation = motionProfiledServo("iv4b_rotation_right", rotationConstraints)

    private val clawConstraints = konfig<MotionProfileConstraints>()
    private val clawRotation = motionProfiledServo("iv4b_claw", clawConstraints)

    fun clawRotateTo(position: Double) = clawRotation.setMotionProfileTarget(position)
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

    }
}