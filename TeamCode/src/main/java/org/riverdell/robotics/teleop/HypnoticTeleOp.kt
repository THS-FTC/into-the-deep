package org.riverdell.robotics.teleop

import com.arcrobotics.ftclib.gamepad.GamepadEx
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import io.liftgate.robotics.mono.Mono.commands
import io.liftgate.robotics.mono.gamepad.ButtonType
import org.riverdell.robotics.HypnoticOpMode
import org.riverdell.robotics.HypnoticRobot
import org.riverdell.robotics.autonomous.detection.VisionPipeline
import org.riverdell.robotics.subsystems.CompositeOuttake
import org.riverdell.robotics.subsystems.IV4B
import org.riverdell.robotics.subsystems.Intake
import org.riverdell.robotics.subsystems.OV4B
import org.riverdell.robotics.subsystems.Outtake
import org.riverdell.robotics.subsystems.Outtake.ClawState
import org.riverdell.robotics.subsystems.SlideConfig

@TeleOp(
    name = "Bit-By-Bit Drive",
    group = "Drive"
)
class HypnoticTeleOp : HypnoticOpMode() {
    override fun buildRobot() = TeleOpRobot()

    inner class TeleOpRobot : HypnoticRobot(this@HypnoticTeleOp) {
        private val gp1Commands by lazy { commands(gamepad1) }
        private val gp2Commands by lazy { commands(gamepad2) }
        val visionPipeline by lazy { VisionPipeline(this@HypnoticTeleOp) }
//    val wrist by lazy { hardware<Servo>("intake_wrist") }

        override fun additionalSubSystems() = listOf(gp1Commands, gp2Commands, visionPipeline)
        override fun initialize() {
//        wrist.position = 0.5
//        visionPipeline.sampleDetection.supplyCurrentWristPosition { wrist.position }
//        visionPipeline.sampleDetection.setDetectionType(SampleType.Blue)

            while (!isStarted) {
                multipleTelemetry.addLine("Configured all subsystems. Waiting for start...")
                multipleTelemetry.update()
                runPeriodics()
            }
        }

        override fun opModeStart() {
            val robotDriver = GamepadEx(gamepad1)
            buildCommands()
            while (opModeIsActive()) {
                val multiplier = 0.5 + gamepad1.right_trigger * 0.5
                drivetrain.driveRobotCentric(robotDriver, multiplier)

                gp1Commands.run()
                gp2Commands.run()
                runPeriodics()
            }
        }

        private fun buildCommands() {
            //game pad 1 commands



            //grab positions
            gp1Commands.where(ButtonType.ButtonA)
                .triggers {
                    intake.setRotationPulley(Intake.RotationState.Grab)
                    iv4b.setV4B(IV4B.V4BState.Grab).thenCompose { intake.setIntakeGrip(Intake.ClawState.Closed) }.thenCompose { iv4b.setV4B(IV4B.V4BState.Observe) }
                    intake.setRotationPulley(Intake.RotationState.Observe)
                }
                .whenPressedOnce()




            //Composite Intake and Out
            gp1Commands.where(ButtonType.BumperLeft)
                .triggers {
                    compositein.toggle()
                }
                .whenPressedOnce()

            gp1Commands.where(ButtonType.BumperRight)
                .triggers {
                   compositeout.toggleBucket()
                }
                .whenPressedOnce()






            //Wrist rotations
            gp1Commands.where(ButtonType.DPadDown)
                .triggers {
                    intake.setWrist(Intake.WristState.Vertical)
                }
                .whenPressedOnce()

            gp1Commands.where(ButtonType.DPadLeft)
                .triggers {
                    intake.setWrist(Intake.WristState.Left)
                }
                .whenPressedOnce()

            gp1Commands.where(ButtonType.DPadRight)
                .triggers {
                    intake.setWrist(Intake.WristState.Right)
                }
                .whenPressedOnce()
            gp1Commands.where(ButtonType.DPadUp)
                .triggers {
                    intake.setWrist(Intake.WristState.Front)

                }
                .whenPressedOnce()






            //Grips
            gp1Commands.where(ButtonType.ButtonX)
                .triggers {
                    intake.toggleIntakeGrip()
                }
                .whenPressedOnce()
            gp1Commands.where(ButtonType.ButtonB)
                .triggers {
                    outtake.toggleOuttakeGrip()
                }
                .whenPressedOnce()
            gp1Commands.where(ButtonType.ButtonY)
                .triggers {
                    compositeout.toggleSpecimen()
                }
                .whenPressedOnce()


            //game pad 2 commands (NA rn)


            gp1Commands.doButtonUpdatesManually()
            gp2Commands.doButtonUpdatesManually()
        }
    }
}