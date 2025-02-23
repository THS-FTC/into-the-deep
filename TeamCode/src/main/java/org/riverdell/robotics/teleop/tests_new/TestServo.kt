package org.riverdell.robotics.teleop.tests_new

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.CRServoImplEx
import com.qualcomm.robotcore.hardware.Servo
import org.riverdell.robotics.teleop.tests_new.config.ServoConfig


@TeleOp(
    name = "Servo Power Test",
    group = "Tests"
)
class TestServo : LinearOpMode()
{
    override fun runOpMode()
    {
        waitForStart()
        if (isStopRequested)
        {
            return
        }

        while (opModeIsActive())
        {
            val hardware = hardwareMap[ServoConfig.name] as CRServoImplEx
            hardware.power = ServoConfig.power
            Thread.sleep(50L)
        }
    }
}