package org.riverdell.robotics.subsystems.outtake

import io.liftgate.robotics.mono.subsystem.AbstractSubsystem
import org.riverdell.robotics.HypnoticRobot
import org.riverdell.robotics.subsystems.motionProfiledServo
import org.riverdell.robotics.subsystems.outtake.other.ClawState
import org.riverdell.robotics.utilities.managed.ServoBehavior
import org.riverdell.robotics.utilities.motionprofile.Constraint
import java.util.concurrent.CompletableFuture

class Outtake(private val robot: HypnoticRobot) : AbstractSubsystem()
{
    private val claw = motionProfiledServo(robot.hardware.outtakeGrip, Constraint.HALF.scale(10.5))


    var clawState = ClawState.Close

    fun openClaw() = setClaw(ClawState.Open)
    fun closeClaw() = setClaw(ClawState.Close)

    fun setClaw(state: ClawState) = let {
        if (clawState == state)
            return@let CompletableFuture.completedFuture(null)

        clawState = state
        return@let updateClawState()
    }

    private fun updateClawState(): CompletableFuture<*>
    {
        return clawRotateTo(clawState.position)
    }

    private fun clawRotateTo(position: Double) = claw.setTarget(position, ServoBehavior.Direct)

    override fun start()
    {

    }

    override fun doInitialize()
    {

    }
}