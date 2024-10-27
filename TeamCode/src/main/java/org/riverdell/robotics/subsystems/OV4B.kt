package org.riverdell.robotics.subsystems

import io.liftgate.robotics.mono.konfig.konfig
import io.liftgate.robotics.mono.states.StateResult
import io.liftgate.robotics.mono.subsystem.AbstractSubsystem
import kotlinx.serialization.Serializable
import org.riverdell.robotics.HypnoticRobot
import org.riverdell.robotics.utilities.motionprofile.MotionProfileConstraints
import java.util.concurrent.CompletableFuture

class OV4B(opMode: HypnoticRobot) : AbstractSubsystem()
{
//    @Serializable
//    data class V4BConfig(
//        val leftIsReversed: Boolean = false,
//        val idlePosition: Double = 0.0,
//
//        val transferPosition: Double = 0.2,
//        val outtakePosition: Double = 0.5,
//
//        val pulleyIntake: Double = 0.2,
//    )
    enum class OV4BState
    {
        Transfer,Outtake,Idle
    }
    enum class PulleyState
    {
        Outtake,Intake,Idle
    }
//    private val ov4bConfig = konfig<V4BConfig>()
    private val currentV4BState = OV4BState.Idle
    private var currentRotateState = PulleyState.Idle


    private val rotationConstraints = konfig<MotionProfileConstraints> { withCustomFileID("ov4b_rotation_motionprofile") }
    private val leftRotation = motionProfiledServo("ov4b_rotation_left", rotationConstraints)
    private val rightRotation = motionProfiledServo("ov4b_rotation_right", rotationConstraints)

    private val clawConstraints = konfig<MotionProfileConstraints>()
    private val clawPulley = motionProfiledServo("ov4b_pulley", clawConstraints)

//    fun pulleyRotateTo(position: Double) = clawPulley.setMotionProfileTarget(position)
fun pulleyRotateTo(position: Double): CompletableFuture<Void>{
    clawPulley.forcefullySetTarget(position)
    return CompletableFuture.completedFuture(null)
}
    fun v4bRotateTo(position: Double): CompletableFuture<Void> {
        if (true) {
            leftRotation.forcefullySetTarget(
                if (OV4BConfig.leftIsReverse)
                    (1.0 - position) else position
            )
            rightRotation.forcefullySetTarget(
                if (!OV4BConfig.leftIsReverse)
                    (1.0 - position) else position
            )
            return CompletableFuture.completedFuture(null)
        }
        else {
            return CompletableFuture.completedFuture(null)
        }
    }
    fun toggleOuttakeRotate(): CompletableFuture<Void> //state when changed to motion profiled
    {
        return if (currentRotateState == PulleyState.Outtake)
        {
            pulleyRotateTo(OV4BConfig.transferPosition).apply {
                currentRotateState = PulleyState.Intake
            }
        } else
        {
            pulleyRotateTo(OV4BConfig.OuttakePosition).apply {
                currentRotateState = PulleyState.Outtake
            }
        }
    }
    fun setPulley(newState: PulleyState): CompletableFuture<Void>
    {
        if (currentRotateState == newState)
        {
            return CompletableFuture.completedFuture(null)
        }

        return if (newState == PulleyState.Intake)
        {
            pulleyRotateTo(OV4BConfig.idlePulley)
                .thenAccept {
                    println(it)
                }
        } else {
            pulleyRotateTo(OV4BConfig.backPulley)
                .thenAccept {
                    println(it)
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
            v4bRotateTo(OV4BConfig.transferPosition)
                .thenAccept {
                    println(it)
                }
        } else if (newState == OV4BState.Outtake){
            v4bRotateTo(OV4BConfig.OuttakePosition)
                .thenAccept {
                    println(it)
                }
        }
        else
        {
            v4bRotateTo(OV4BConfig.IdlePosition)
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
        pulleyRotateTo(OV4BConfig.idlePulley)
        v4bRotateTo(OV4BConfig.IdlePosition)

    }
}