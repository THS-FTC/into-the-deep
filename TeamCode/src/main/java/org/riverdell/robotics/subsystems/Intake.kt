package org.riverdell.robotics.subsystems

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import io.liftgate.robotics.mono.konfig.konfig
import io.liftgate.robotics.mono.subsystem.AbstractSubsystem
import kotlinx.serialization.Serializable
import org.riverdell.robotics.utilities.motionprofile.MotionProfileConstraints
import java.util.concurrent.CompletableFuture

class Intake(opMode: LinearOpMode) : AbstractSubsystem()
{
    private val wristConstraints = konfig<MotionProfileConstraints> { withCustomFileID("intake_wrist_motionprofile") }
    private val wrist = motionProfiledServo("intake_wrist", wristConstraints)

    private val rotationConstraints = konfig<MotionProfileConstraints> { withCustomFileID("intake_grip_motionprofile") }
    private val grip = motionProfiledServo("intake_grip", rotationConstraints)

    fun wristRotateTo(position: Double) = wrist.setMotionProfileTarget(position)
    fun gripRotateTo(position: Double) = grip.setMotionProfileTarget(position)

    override fun start()
    {

    }

    override fun doInitialize()
    {

    }
}