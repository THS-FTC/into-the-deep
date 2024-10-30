package org.riverdell.robotics.teleop.tests

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.Servo
import io.liftgate.robotics.mono.konfig.konfig
import kotlinx.serialization.Serializable
import org.riverdell.robotics.autonomous.movement.konfig.NavigationNodeCollection
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
            val hardware1 = hardware<Servo>(ServoConfig.name1)
            hardware1.position = ServoConfig.position1
//            val hardware2 = hardware<Servo>(ServoConfig.name2)
//            hardware2.position = ServoConfig.position2
//            val hardware3 = hardware<Servo>(ServoConfig.name3)
//            hardware3.position = ServoConfig.position3
            Thread.sleep(50L)
        }
    }
}