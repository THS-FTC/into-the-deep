package org.riverdell.robotics.teleop.tests

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.Servo
import io.liftgate.robotics.mono.konfig.konfig
import kotlinx.serialization.Serializable
import org.riverdell.robotics.autonomous.movement.konfig.NavigationNodeCollection
import org.riverdell.robotics.subsystems.IV4B
import org.riverdell.robotics.utilities.hardware

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
            //different servos to test

//            val hardware1 = hardware<Servo>(ServoConfig.OV4BL)
//            hardware1.position = ServoConfig.OL
            val hardware2 = hardware<Servo>(ServoConfig.intakePulley)
            hardware2.position = ServoConfig.IP
            val hardware3 = hardware<Servo>(ServoConfig.outtakePulley)
            hardware3.position = ServoConfig.OP
//            val hardware4 = hardware<Servo>(ServoConfig.OV4BR)
//            hardware4.position = ServoConfig.OR
            val hardware5 = hardware<Servo>(ServoConfig.intakeWrist)
            hardware5.position = ServoConfig.IW

            Thread.sleep(50L)
        }
    }
}