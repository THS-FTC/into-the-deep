package org.riverdell.robotics.teleop

import com.arcrobotics.ftclib.gamepad.GamepadEx
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.Servo
import io.liftgate.robotics.mono.Mono.commands
import io.liftgate.robotics.mono.gamepad.ButtonType
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit
import org.riverdell.robotics.HypnoticRobot
import org.riverdell.robotics.autonomous.detection.SampleType
import org.riverdell.robotics.autonomous.detection.VisionPipeline
import org.riverdell.robotics.utilities.hardware

@TeleOp(
    name = "Multiplayer",
    group = "Drive"
)
class HypnoticTeleOp : HypnoticRobot()
{
    private val gp1Commands by lazy { commands(gamepad1) }
    private val gp2Commands by lazy { commands(gamepad2) }

    val visionPipeline by lazy { VisionPipeline(this) }
    val wrist by lazy { hardware<Servo>("intake_wrist") }

    override fun additionalSubSystems() = listOf(gp1Commands, gp2Commands, visionPipeline)
    override fun initialize()
    {
        wrist.position = 0.5
        visionPipeline.sampleDetection.supplyCurrentWristPosition { wrist.position }
        visionPipeline.sampleDetection.setDetectionType(SampleType.Blue)

        while (!isStarted)
        {
            multipleTelemetry.addLine("Configured all subsystems. Waiting for start...")
            multipleTelemetry.update()
            runPeriodics()
        }
    }

    override fun opModeStart()
    {
        val robotDriver = GamepadEx(gamepad1)
        buildCommands()
        while (opModeIsActive())
        {
            val multiplier = 0.5 + gamepad1.right_trigger * 0.5
            drivetrain.driveRobotCentric(robotDriver, multiplier)

            gp1Commands.run()
            gp2Commands.run()

            wrist.position = visionPipeline.sampleDetection.targetWristPosition
            runPeriodics()
        }
    }

    private fun buildCommands()
    {
        gp1Commands.doButtonUpdatesManually()
        gp2Commands.doButtonUpdatesManually()
    }
}