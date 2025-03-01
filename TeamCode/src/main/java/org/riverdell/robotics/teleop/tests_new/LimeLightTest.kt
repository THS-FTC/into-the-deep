package org.riverdell.robotics.teleop.tests_new

import com.qualcomm.hardware.limelightvision.LLResult
import com.qualcomm.hardware.limelightvision.Limelight3A
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.riverdell.robotics.subsystems.LimelightManager


@TeleOp(
    name = "Servo Power Test",
    group = "Tests"
)
class LimeLightTest : LinearOpMode()
{
    lateinit var lime : LimelightManager
    override fun runOpMode()
    {
        waitForStart()
        lime.init()
        lime.setCurrentPipeline(LimelightManager.LimelightState.Yellow)
        if (isStopRequested)
        {
            return
        }

        while (opModeIsActive())
        {
            if(gamepad2.a){
                lime.limeIntaking()
            }
            if(gamepad2.b){
                lime.turnOff()
            }

            Thread.sleep(50)
        }
    }
}