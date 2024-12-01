package org.riverdell.robotics.subsystems.outtake

import io.liftgate.robotics.mono.subsystem.AbstractSubsystem
import org.riverdell.robotics.HypnoticRobot
import org.riverdell.robotics.subsystems.outtake.v4b.v4bState
import org.riverdell.robotics.subsystems.motionProfiledServo
import org.riverdell.robotics.subsystems.outtake.v4b.PulleyState
import org.riverdell.robotics.utilities.managed.ServoBehavior
import org.riverdell.robotics.utilities.motionprofile.Constraint
import java.util.concurrent.CompletableFuture

class OV4B(private val robot: HypnoticRobot) : AbstractSubsystem()
{
    private val leftRotation = motionProfiledServo(robot.hardware.outtakeRotationLeft, Constraint.HALF.scale(30.5))
    private val rightRotation = motionProfiledServo(robot.hardware.outtakeRotationRight, Constraint.HALF.scale(30.5))
    private val pulley = motionProfiledServo(robot.hardware.outtakePulley, Constraint.HALF.scale(10.5))

    var rotationState = PulleyState.Transfer
    var V4bState = v4bState.Transfer

    fun transferRotation() = setRotation(PulleyState.Transfer)
    fun specimenRotation() = setRotation(PulleyState.OuttakeSpecimen)
    fun bucketRotation() = setRotation(PulleyState.OuttakeBucket)

    fun setRotation(state: PulleyState) = let {
        if (rotationState == state)
            return@let CompletableFuture.completedFuture(null)

        rotationState = state
        return@let updateRotationState()
    }

    private fun updateRotationState(): CompletableFuture<*>
    {
        return pulleyRotateTo(rotationState.position)
    }

    fun v4bOuttake() = setV4B(v4bState.Outtake)
    fun v4bSpeicmen() = setV4B(v4bState.Specimen)
    fun v4bIdle() = setV4B(v4bState.Idle)
    fun v4bTransfer() = setV4B(v4bState.Transfer)


    fun setV4B(state: v4bState) = let {
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