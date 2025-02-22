package org.riverdell.robotics.teleop.tests

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.Servo
import org.riverdell.robotics.teleop.tests_new.config.IntakePrepareConfig


@TeleOp(
    name = "Prepare Outtake",
    group = "Tests"
)
class PrepareIntakeTune : LinearOpMode()
{
    override fun runOpMode()
    {
        waitForStart()
        if (isStopRequested)
        {
            return
        }

        val left = hardwareMap["iv4b_rotation_left"] as Servo
        val right = hardwareMap["iv4b_rotation_right"] as Servo
        val pulley = hardwareMap["intake_pulley"] as Servo

        while (opModeIsActive())
        {
            left.position = IntakePrepareConfig.v4b_position
            right.position = 1.0 - IntakePrepareConfig.v4b_position
            pulley.position = IntakePrepareConfig.pulley_position

            Thread.sleep(50L)
        }
    }
}