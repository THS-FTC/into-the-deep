package org.riverdell.robotics.teleop.tests_new.misc

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.Servo

@TeleOp(
    name = "Prepare Intake",
    group = "Tests"
)
class PrepareIntake : LinearOpMode()
{
    override fun runOpMode()
    {
        waitForStart()
        if (isStopRequested)
        {
            return
        }

        val grip = hardwareMap["intake_grip"] as Servo
        val wrist = hardwareMap["intake_wrist"] as Servo

        while (opModeIsActive())
        {
            grip.position = 0.0
            wrist.position = 0.5
            Thread.sleep(50L)
        }
    }
}