package org.riverdell.robotics.subsystems

import io.liftgate.robotics.mono.konfig.konfig
import io.liftgate.robotics.mono.states.StateResult
import io.liftgate.robotics.mono.subsystem.AbstractSubsystem
import kotlinx.serialization.Serializable
import org.riverdell.robotics.HypnoticRobot
import org.riverdell.robotics.utilities.motionprofile.Constraint
import org.riverdell.robotics.utilities.motionprofile.ProfileConstraints
import java.util.concurrent.CompletableFuture

class OV4B(private val robot: HypnoticRobot) : AbstractSubsystem()
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
        Transfer,Outtake,Idle,Init,Specimen
    }
    enum class PulleyState
    {
        Bucket,Intake,Idle,Init,Specimen
    }
//    private val ov4bConfig = konfig<V4BConfig>()
    private var currentV4BState = OV4BState.Init
    private var currentRotateState = PulleyState.Init


    private val leftRotation = motionProfiledServo(robot.hardware.outtakeRotationLeft, Constraint.HALF.scale(15.0))
    private val rightRotation = motionProfiledServo(robot.hardware.outtakeRotationRight, Constraint.HALF.scale(15.0))
    private val clawPulley = motionProfiledServo(robot.hardware.outtakePulley, Constraint.HALF.scale(5.0))


    fun pulleyRotateTo(position: Double) = clawPulley.setMotionProfileTarget(position)

    fun v4bRotateTo(position: Double) = CompletableFuture.allOf(
        leftRotation.setMotionProfileTarget(
            if (OV4BConfig.leftIsReverse)
                (1.0 - position) else position
        ),
        rightRotation.setMotionProfileTarget(
            if (!OV4BConfig.leftIsReverse)
                (1.0 - position) else position
        )
    )

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
        } else if (newState == PulleyState.Specimen) {
            pulleyRotateTo(OV4BConfig.specimenPulley)
                .thenAccept {
                    println(it)
                }
        }
        else{
            pulleyRotateTo(OV4BConfig.bucketPulley)
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
            v4bRotateTo(OV4BConfig.transferPosition).apply {
                currentV4BState = OV4BState.Transfer
            }
        } else if (newState == OV4BState.Outtake){
            v4bRotateTo(OV4BConfig.OuttakePosition)
                .apply {
                    currentV4BState = OV4BState.Outtake
                }
        }
        else
        {
            v4bRotateTo(OV4BConfig.SpecimenPosition)
                .apply {
                    currentV4BState = OV4BState.Specimen
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
//        pulleyRotateTo(OV4BConfig.idlePulley)
//        v4bRotateTo(OV4BConfig.IdlePosition)

    }
}