package org.riverdell.robotics.teleop.tests

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.Servo
import org.riverdell.robotics.teleop.tests_new.config.OuttakePrepareConfig
import org.riverdell.robotics.teleop.tests_new.config.WristPrepareConfig


@TeleOp(
    name = "Prepare Outtake",
    group = "Tests"
)
class PrepareWrists : LinearOpMode()
{
    override fun runOpMode()
    {
        waitForStart()
        if (isStopRequested)
        {
            return
        }

        val intakeWrist= hardwareMap["intake_wrist"] as Servo
        val outtakeWrist = hardwareMap["outtake_wrist"] as Servo


        while (opModeIsActive())
        {
            intakeWrist.position = WristPrepareConfig.intake_wrist
            outtakeWrist.position = WristPrepareConfig.outtake_wrist
            Thread.sleep(50L)
        }
    }
}