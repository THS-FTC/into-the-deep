package org.riverdell.robotics.subsystems.outtake.v4b

import io.liftgate.robotics.mono.subsystem.AbstractSubsystem
import org.riverdell.robotics.HypnoticRobot
import org.riverdell.robotics.subsystems.motionProfiledServo
import org.riverdell.robotics.utilities.managed.ServoBehavior
import org.riverdell.robotics.utilities.motionprofile.Constraint
import java.util.concurrent.CompletableFuture

class OV4B(private val robot: HypnoticRobot) : AbstractSubsystem()
{
    private val leftRotation = motionProfiledServo(robot.hardware.outtakeRotationLeft, Constraint.HALF.scale(30.5))
    private val rightRotation = motionProfiledServo(robot.hardware.outtakeRotationRight, Constraint.HALF.scale(30.5))
    private val pulley = motionProfiledServo(robot.hardware.outtakePulley, Constraint.HALF.scale(10.5))

    var rotationState = OPulleyState.Transfer
    var V4bState = ov4bState.Transfer

    fun transferRotation() = setRotation(OPulleyState.Transfer)
    fun specimenRotation() = setRotation(OPulleyState.OuttakeSpecimen)
    fun bucketRotation() = setRotation(OPulleyState.OuttakeBucket)

    fun setRotation(state: OPulleyState) = let {
        if (rotationState == state)
            return@let CompletableFuture.completedFuture(null)

        rotationState = state
        return@let updateRotationState()
    }

    private fun updateRotationState(): CompletableFuture<*>
    {
        return pulleyRotateTo(rotationState.position)
    }

    fun v4bOuttake() = setV4B(ov4bState.Outtake)
    fun v4bSpeicmen() = setV4B(ov4bState.Specimen)
    fun v4bIdle() = setV4B(ov4bState.Idle)
    fun v4bTransfer() = setV4B(ov4bState.Transfer)


    fun setV4B(state: ov4bState) = let {
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
    private fun pulleyRotateTo(position: Double) = pulley.setTarget(position, ServoBehavior.MotionProfile)

    override fun start()
    {

    }

    override fun doInitialize()
    {

    }
}