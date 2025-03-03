package org.riverdell.robotics.teleop.tests

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.riverdell.robotics.HypnoticRobot
import org.riverdell.robotics.subsystems.LimelightManager

@TeleOp(name = "Servo Power Test", group = "Tests")
class LimeLightTest : LinearOpMode() {

    lateinit var robotInstance: HypnoticRobot
    lateinit var lime: LimelightManager

    override fun runOpMode() {
        // Instantiate your full robot instance. This should initialize your hardwareMap

        // Instantiate LimelightManager using the robot instance.
        // (This uses your original IV4B implementation from your repository.)
        lime = LimelightManager(robotInstance)

        waitForStart()
        lime.init()
        if (isStopRequested) return

        while (opModeIsActive()) {
            // Press gamepad2.a to start limelight processing.
            // Note: lime.limeIntaking() is blocking and will continue running until turnOff() is called.
            if (gamepad2.a) {
                lime.limeIntaking()
            }
            // Press gamepad2.b to stop limelight processing.
            if (gamepad2.b) {
                lime.turnOff()
            }
            telemetry.addData("Estimated X", lime.estimatedTargetX)
            telemetry.addData("Estimated Y", lime.estimatedTargetY)
            telemetry.addData("Turret Angle", lime.estimatedTurretAngle)
            telemetry.update()
            sleep(50)
        }
    }
}
