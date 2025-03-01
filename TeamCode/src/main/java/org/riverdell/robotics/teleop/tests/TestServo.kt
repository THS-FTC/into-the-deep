package org.riverdell.robotics.teleop.tests

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.Servo

@TeleOp(
    name = "Two Servo Test",
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
            val servo1 = hardwareMap[TwoServoConfig.name1] as Servo
            servo1.position = TwoServoConfig.position1
            val servo2 = hardwareMap[TwoServoConfig.name2] as Servo
            servo2.position = TwoServoConfig.position2
            Thread.sleep(50L)
        }
    }
}