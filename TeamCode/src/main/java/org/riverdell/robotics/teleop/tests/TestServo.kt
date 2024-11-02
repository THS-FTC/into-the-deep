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
            val hardware = hardwareMap[ServoConfig.intakePulley] as Servo
            hardware.position = ServoConfig.IP
//            val hardware1 = hardwareMap[ServoConfig.outtakePulley] as Servo
//            hardware1.position = ServoConfig.OP
            val hardware2 = hardwareMap[ServoConfig.intakeWrist] as Servo
            hardware2.position = ServoConfig.IW
//            val hardware3 = hardwareMap[ServoConfig.IV4BL] as Servo
//            hardware3.position = ServoConfig.IL_changer
//            val hardware4 = hardwareMap[ServoConfig.IV4BR] as Servo
//            hardware4.position = ServoConfig.IR
//            val hardware5 = hardwareMap[ServoConfig.OV4BL] as Servo
//            hardware5.position = ServoConfig.OL_changer
//            val hardware6 = hardwareMap[ServoConfig.OV4BR] as Servo
//            hardware6.position = ServoConfig.OR

            Thread.sleep(50L)
        }
    }
}