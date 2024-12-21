package org.riverdell.robotics.teleop

import com.arcrobotics.ftclib.gamepad.GamepadEx
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import io.liftgate.robotics.mono.Mono.commands
import io.liftgate.robotics.mono.gamepad.ButtonType
import org.riverdell.robotics.HypnoticOpMode
import org.riverdell.robotics.HypnoticRobot
import org.riverdell.robotics.subsystems.composite.CompositeState
import org.riverdell.robotics.subsystems.intake.composite.CompositeAll
import org.riverdell.robotics.subsystems.intake.v4b.IV4B
import org.riverdell.robotics.subsystems.intake.other.Intake
import org.riverdell.robotics.subsystems.outtake.other.OClawState
import kotlin.math.absoluteValue

@TeleOp(
    name = "Bit-By-Bit Drive",
    group = "Drive"
)
class HypnoticTeleOp : HypnoticOpMode() {
    override fun buildRobot() = TeleOpRobot()

    inner class TeleOpRobot : HypnoticRobot(this@HypnoticTeleOp) {
        private val gp1Commands by lazy { commands(gamepad1) }
        private val gp2Commands by lazy { commands(gamepad2) }
        //val visionPipeline by lazy { VisionPipeline(this@HypnoticTeleOp) }
//    val wrist by lazy { hardware<Servo>("intake_wrist") }

        override fun additionalSubSystems() = listOf(gp1Commands, gp2Commands)
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
            val robotDriver = GamepadEx(gamepad2)
            buildCommands()
            while (opModeIsActive()) {
                val multiplier = 0.55 + gamepad2.right_trigger * 0.5
                drivetrain.driveRobotCentric(robotDriver, multiplier)
                if ((composite.state == CompositeState.IntakeReady || composite.state == CompositeState.IntakeReady))
                {
                    val wantedPower = -opMode.gamepad1.left_trigger + opMode.gamepad1.right_trigger
                    if (wantedPower.absoluteValue > 0.1 && !extension.slides.isTravelling())
                    {
                        if (wantedPower < 0)
                        {
                            if (extension.slides.currentPosition() >= -10)
                            {
                                extension.slides.supplyPowerToAll(0.0)
                            } else
                            {
                                extension.slides.supplyPowerToAll(-wantedPower.toDouble() / 2.0)
                            }
                        } else
                        {
                            if (extension.slides.currentPosition() < -435)
                            {
                                extension.slides.supplyPowerToAll(0.0)
                            } else
                            {
                                extension.slides.supplyPowerToAll(-wantedPower.toDouble() / 2.0)
                            }
                        }
                    } else if (!extension.slides.isTravelling())
                    {
                        extension.slides.supplyPowerToAll(0.0)
                    }
                } else if (( composite.state == CompositeState.Rest ) ){
                    val wantedPower = opMode.gamepad1.left_trigger - opMode.gamepad1.right_trigger
                    if (wantedPower.absoluteValue > 0.1 && !extension.slides.isTravelling())
                    {
                        if (wantedPower < 0)
                        {
                            if (extension.slides.currentPosition() > -80)
                            {
                                extension.slides.supplyPowerToAll(0.0)
                            } else
                            {
                                extension.slides.supplyPowerToAll(-wantedPower.toDouble() / 2.0)
                            }
                        } else
                        {
                            if (extension.slides.currentPosition() <= -92)
                            {
                                extension.slides.supplyPowerToAll(0.0)
                            } else
                            {
                                extension.slides.supplyPowerToAll(-wantedPower.toDouble() / 2.0)
                            }
                        }
                    } else if (!extension.slides.isTravelling())
                    {
                        extension.slides.supplyPowerToAll(0.0)
                    }
                }

                telemetry.addLine("Extendo Left Position: ${hardware.extensionMotorLeft.currentPosition}")
                telemetry.addLine("Extendo Right Position: ${hardware.extensionMotorRight.currentPosition}")
                telemetry.update()

                gp1Commands.run()
                gp2Commands.run()
                runPeriodics()
            }
        }

        private fun buildCommands() {
            //game pad 1 commands



            //grab positions
            gp1Commands.apply {
                where(ButtonType.ButtonA)
                    .onlyWhen { composite.state == CompositeState.IntakeReady }
                    .triggers {
                        composite.intakeGrabAndConfirm()
                    }
                    .whenPressedOnce()

                where(ButtonType.BumperRight)
                    .onlyWhen { composite.state != CompositeState.Rest || composite.state != CompositeState.Outtaking }
                    .triggers {
                        composite.exitOuttakeReadyToRest()
                    }
                    .whenPressedOnce()

                where(ButtonType.BumperRight)
                    .onlyWhen { composite.state == CompositeState.Rest }
                    .triggers {
                        composite.initialOuttakeFromRest()
                    }
                    .whenPressedOnce()
                where(ButtonType.BumperLeft)
                    .onlyWhen { composite.state != CompositeState.IntakeReady || composite.state != CompositeState.Intake }
                    .triggers {
                        composite.intakeObserve()
                    }
                    .whenPressedOnce()

                where(ButtonType.BumperLeft)
                    .onlyWhen { composite.state == CompositeState.IntakeReady }
                    .triggers {
                        composite.confirmTransferAndReady()
                    }
                    .whenPressedOnce()

                where(ButtonType.DPadDown)
                    .onlyWhen { composite.state == CompositeState.IntakeReady }
                    .triggers {
                        intake.horizWrist()
                    }
                    .whenPressedOnce()

                where(ButtonType.DPadLeft)
                    .onlyWhen { composite.state == CompositeState.IntakeReady }
                    .triggers {
                        intake.leftWrist()
                    }
                    .whenPressedOnce()

                where(ButtonType.DPadRight)
                    .onlyWhen { composite.state == CompositeState.IntakeReady }
                    .triggers {
                        intake.rightWrist()
                    }
                    .whenPressedOnce()

                where(ButtonType.DPadUp)
                    .onlyWhen { composite.state == CompositeState.IntakeReady }
                    .triggers {
                        intake.latWrist()

                    }
                    .whenPressedOnce()

                where(ButtonType.ButtonX)
                    .triggers {
                        composite.exitOuttakeReadyToRest()
                    }
                    .whenPressedOnce()

                where(ButtonType.ButtonB)
                    .onlyWhen { outtake.clawState == OClawState.Close }
                    .triggers {
                        if (composite.state == CompositeState.Outtaking) {
                            composite.outtakeCompleteAndReturnToOuttakeReady()
                        }
                        outtake.openClaw()
                    }
                    .whenPressedOnce()


                where(ButtonType.ButtonB)
                    .onlyWhen { outtake.clawState == OClawState.Open }
                    .triggers {
                        outtake.closeClaw()
                    }
                    .whenPressedOnce()


                where(ButtonType.ButtonY)
                    .onlyWhen {composite.state == CompositeState.Specimen }
                    .triggers {
                        composite.specimenScoredAndReturnToRest()
                    }
                    .whenPressedOnce()


                where(ButtonType.ButtonY)
                    .onlyWhen { composite.state == CompositeState.Rest }
                    .triggers {
                        composite.initialspecimenOuttakeFromRest()
                    }
                    .andIsHeldUntilReleasedWhere {
                        composite.scoreSpecimen()
                    }


            }



            gp1Commands.doButtonUpdatesManually()
            gp2Commands.doButtonUpdatesManually()
        }
    }
}