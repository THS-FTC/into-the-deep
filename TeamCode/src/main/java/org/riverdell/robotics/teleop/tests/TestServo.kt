package org.riverdell.robotics.teleop.tests

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.Servo

@TeleOp(
    name = "Servo Test",
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
            val servo1 = hardwareMap[ServoConfig.name1] as Servo
            servo1.position = ServoConfig.position1
            val servo2 = hardwareMap[ServoConfig.name2] as Servo
            servo2.position = ServoConfig.position2
            Thread.sleep(50L)
        }
    }
}