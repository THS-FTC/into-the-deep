package org.riverdell.robotics.subsystems

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import io.liftgate.robotics.mono.konfig.konfig
import io.liftgate.robotics.mono.subsystem.AbstractSubsystem
import kotlinx.serialization.Serializable
import org.riverdell.robotics.utilities.motionprofile.MotionProfileConstraints
import java.util.concurrent.CompletableFuture

class Outtake(opMode: LinearOpMode) : AbstractSubsystem()
{
    @Serializable
    data class OuttakeConfig(val leftIsReversed: Boolean = false)

    private val outtakeConfig = konfig<OuttakeConfig>()

    private val wristConstraints = konfig<MotionProfileConstraints> { withCustomFileID("outtake_wrist_motionprofile") }
    private val wrist = motionProfiledServo("outtake_wrist", wristConstraints)

    private val rotationConstraints = konfig<MotionProfileConstraints> { withCustomFileID("outtake_grip_motionprofile") }
    private val Grip = motionProfiledServo("outtake_grip", rotationConstraints)

    fun oWristRotateTo(position: Double) = wrist.setMotionProfileTarget(position)
    fun oWripRotateTo(position: Double) = CompletableFuture.allOf(
        Grip.setMotionProfileTarget(
            if (!outtakeConfig.get().leftIsReversed)
                (1.0 - position) else position
        )
    )

    override fun start()
    {

    }

    override fun doInitialize()
    {

    }
}