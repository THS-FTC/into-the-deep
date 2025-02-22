package org.riverdell.robotics.teleop.tests

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.Servo
import org.riverdell.robotics.teleop.tests_new.config.OuttakePrepareConfig


@TeleOp(
    name = "Prepare Outtake",
    group = "Tests"
)
class PrepareOuttake : LinearOpMode()
{
    override fun runOpMode()
    {
        waitForStart()
        if (isStopRequested)
        {
            return
        }

        val left = hardwareMap["ov4b_rotation_left"] as Servo
        val right = hardwareMap["ov4b_rotation_left"] as Servo
        val pulley = hardwareMap["ov4b_pulley"] as Servo

        while (opModeIsActive())
        {
            left.position = OuttakePrepareConfig.v4b_position
            right.position = 1.0 - OuttakePrepareConfig.v4b_position
            pulley.position = OuttakePrepareConfig.pulley_position
            Thread.sleep(50L)
        }
    }
}