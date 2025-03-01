package org.riverdell.robotics.teleop.tests_new

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.CRServoImplEx
import com.qualcomm.robotcore.hardware.Servo
import org.riverdell.robotics.teleop.tests_new.config.TESTINGConfig


@TeleOp(
    name = "Servo Pos Test",
    group = "Tests"
)
class TestServoPos : LinearOpMode()
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
            val hardware = hardwareMap[TESTINGConfig.name] as Servo
            hardware.position = TESTINGConfig.position
            Thread.sleep(50L)
        }
    }
}