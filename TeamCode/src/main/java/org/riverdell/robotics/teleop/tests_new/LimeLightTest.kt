package org.riverdell.robotics.teleop.tests_new

import com.qualcomm.hardware.limelightvision.Limelight3A
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.riverdell.robotics.CompositeIntakeInterface
import org.riverdell.robotics.ExtensionInterface
import org.riverdell.robotics.IV4BInterface
import org.riverdell.robotics.subsystems.LimelightManager
import java.util.concurrent.CompletableFuture

@TeleOp(name = "Servo Power Test", group = "Tests")
class LimeLightTest : LinearOpMode() {

    lateinit var lime: LimelightManager
    var limeThread: Thread? = null

    override fun runOpMode() {
        // Retrieve the limelight device from hardwareMap.
        val limelightDevice = hardwareMap.get(Limelight3A::class.java, "limelight")

        // Create a dummy extension implementation.
        val extensionDevice = object : ExtensionInterface {
            override fun extendToAndStayAt(position: Int) {
                telemetry.addData("Extension", "Position: $position")
                telemetry.update()
            }
        }

        // Create a dummy IV4B implementation.
        val iv4bDevice = object : IV4BInterface {
            override fun v4bRotateTo(position: Double): CompletableFuture<Void> {
                telemetry.addData("IV4B", "v4bRotateTo: $position")
                telemetry.update()
                return CompletableFuture.completedFuture(null)
            }
            override fun leftDiffyRotate(position: Double): CompletableFuture<Void> {
                telemetry.addData("IV4B", "leftDiffyRotate: $position")
                telemetry.update()
                return CompletableFuture.completedFuture(null)
            }
            override fun rightDiffyRotate(position: Double): CompletableFuture<Void> {
                telemetry.addData("IV4B", "rightDiffyRotate: $position")
                telemetry.update()
                return CompletableFuture.completedFuture(null)
            }
        }

        // Create a dummy composite intake implementation.
        val compositeIntakeDevice = object : CompositeIntakeInterface {
            override fun setIntake(state: CompositeIntakeInterface.IntakeState) {
                telemetry.addData("Intake", "State: $state")
                telemetry.update()
            }
        }

        // Instantiate LimelightManager with the required hardware interfaces.
        lime = LimelightManager(limelightDevice, extensionDevice, iv4bDevice, compositeIntakeDevice)

        waitForStart()
        lime.init()
        if (isStopRequested) return

        while (opModeIsActive()) {
            // Start the limelight processing on a separate thread if button A is pressed.
            if (gamepad2.a && (limeThread == null || !limeThread!!.isAlive)) {
                limeThread = Thread {
                    lime.limeIntaking() // This is a blocking call.
                }
                limeThread?.start()
            }
            // If button B is pressed, stop the limelight processing.
            if (gamepad2.b) {
                lime.turnOff()
                // Optionally wait for the thread to finish.
                if (limeThread != null && limeThread!!.isAlive) {
                    limeThread!!.join(100)
                }
            }
            telemetry.addData("Estimated X", lime.estimatedTargetX)
            telemetry.addData("Estimated Y", lime.estimatedTargetY)
            telemetry.addData("Turret Angle", lime.estimatedTurretAngle)
            telemetry.update()
            sleep(50)
        }
    }
}
