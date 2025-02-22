package org.riverdell.robotics.subsystems

import android.transition.Slide
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import io.liftgate.robotics.mono.states.StateResult
import io.liftgate.robotics.mono.subsystem.AbstractSubsystem
import org.riverdell.robotics.HypnoticRobot
import org.riverdell.robotics.subsystems.CompositeIntake.IntakeState
import org.riverdell.robotics.subsystems.Outtake.ClawState
import java.util.Currency
import java.util.concurrent.CompletableFuture

class CompositeOuttake(val robot: HypnoticRobot) : AbstractSubsystem() {

    enum class OuttakeState {
        Transfer, Outtake, Init, SpecimenStart, SpecimenIntake, SpecimenScoring, SpecimenScored, SampleStart
    }

    var currentOuttakeState = OuttakeState.Init


    fun setOuttake(newState: OuttakeState): CompletableFuture<Void> {
        if (currentOuttakeState == newState) {
            return CompletableFuture.completedFuture(null)
        }

        return if (newState == OuttakeState.Transfer) {
            //idle outtake stuff here
            CompletableFuture.allOf(
                robot.ov4b.setPulley(OV4B.PulleyState.Intake),
                robot.ov4b.setV4B(OV4B.OV4BState.Transfer),
                robot.lift.extendToAndStayAt(SlideConfig.liftClosed)
            ).thenAccept { robot.lift.idle() }.apply { currentOuttakeState = OuttakeState.Transfer }
        } else if (newState == OuttakeState.Outtake) {
            //closes the outtake claw and opens the intake claw, then the robot extends forwards
            robot.intake.setIntakeGrip(Intake.ClawState.Open).thenCompose {
                CompletableFuture.allOf(
                    robot.lift.extendToAndStayAt(SlideConfig.liftHighBucket),
                    robot.extension.extendToAndStayAt(SlideConfig.extendoGetOut)
                )
            }
            robot.intake.setRotationPulley(Intake.RotationState.Observe).thenCompose {
                CompletableFuture.allOf(
                    robot.ov4b.setPulley(OV4B.PulleyState.Bucket),
                    robot.ov4b.setV4B(OV4B.OV4BState.Outtake)
                )
            }.apply { currentOuttakeState = OuttakeState.Outtake }
        } else if (newState == OuttakeState.SpecimenStart) {
            CompletableFuture.allOf(
                robot.ov4b.setV4B(OV4B.OV4BState.Away),
                robot.intake.setWrist(Intake.WristState.Vertical)
            )
                .thenComposeAsync {
                    CompletableFuture.allOf(
                        robot.iv4b.setV4B(IV4B.V4BState.Hidden),
                        robot.intake.setRotationPulley(Intake.RotationState.Observe)
                    ).join()
                    CompletableFuture.allOf(
                        robot.ov4b.setV4B(OV4B.OV4BState.SpecimenIntake),
                        robot.outtake.setWrist(Outtake.WristState.Front)
                    )
                }
                .apply { currentOuttakeState = OuttakeState.SpecimenStart }
        } else if (newState == OuttakeState.SampleStart) {
            CompletableFuture.allOf(
                robot.ov4b.setV4B(OV4B.OV4BState.Away),
                robot.intake.setWrist(Intake.WristState.Vertical)
            )
                .thenComposeAsync {
                    CompletableFuture.allOf(
                        robot.iv4b.setV4B(IV4B.V4BState.Observe),
                        robot.intake.setRotationPulley(Intake.RotationState.Observe)
                    ).join()
                    CompletableFuture.allOf(
                        robot.ov4b.setV4B(OV4B.OV4BState.Transfer),
                        robot.outtake.setWrist(Outtake.WristState.Front)
                    )
                }
                .apply { currentOuttakeState = OuttakeState.SpecimenStart }
        }

        else if (newState == OuttakeState.SpecimenScoring) {
            CompletableFuture.allOf(
                robot.ov4b.setPulley(OV4B.PulleyState.Specimen),
                robot.ov4b.setV4B(OV4B.OV4BState.Specimen)
            ).thenCompose {
                robot.outtake.setWrist(Outtake.WristState.Specimen)
            }.apply { currentOuttakeState = OuttakeState.SpecimenScoring }
        } else if (newState == OuttakeState.SpecimenScored) {
            CompletableFuture.allOf(
                robot.ov4b.setV4B(OV4B.OV4BState.SpecimenScore),
                robot.ov4b.setPulley(OV4B.PulleyState.SpecimenScore)
            ).apply { currentOuttakeState = OuttakeState.SpecimenScored }
        } else if (newState == OuttakeState.SpecimenIntake) {
            CompletableFuture.allOf(
                robot.ov4b.setPulley(OV4B.PulleyState.SpecimenIntake),
                robot.ov4b.setV4B(OV4B.OV4BState.SpecimenIntake)
            ).thenCompose {
                robot.outtake.setWrist(Outtake.WristState.Front)
            }.apply { currentOuttakeState = OuttakeState.SpecimenIntake }
        } else {
            robot.compositein.setIntake(IntakeState.Idle).thenCompose {
                CompletableFuture.allOf(
                    robot.ov4b.setPulley(OV4B.PulleyState.Intake),
                    robot.ov4b.setV4B(OV4B.OV4BState.Transfer),
                    robot.lift.extendToAndStayAt(SlideConfig.liftClosed),
                )
            }.thenAccept { robot.lift.idle() }.apply { currentOuttakeState = OuttakeState.Transfer }
        }
    }

    fun toggleBucket(): CompletableFuture<Void> {
        return if (currentOuttakeState == OuttakeState.Outtake) {
            setOuttake(OuttakeState.Transfer)
        } else {
            //closes the outtake claw and opens the intake claw, then the robot extends forwards
            setOuttake(OuttakeState.Outtake)
        }
    }

    fun toggleSpecimen(): CompletableFuture<Void> {
        return if (currentOuttakeState == OuttakeState.SpecimenIntake || currentOuttakeState == OuttakeState.SpecimenStart) {
            setOuttake(OuttakeState.SpecimenScoring)
        } else if (currentOuttakeState == OuttakeState.SpecimenScoring) {
            setOuttake(OuttakeState.SpecimenScored)
        } else {
            setOuttake(OuttakeState.SpecimenIntake)
        }
    }
    fun toggleMode(): CompletableFuture<Void> {
        return if(currentOuttakeState != OuttakeState.SpecimenScored || currentOuttakeState != OuttakeState.SpecimenIntake || currentOuttakeState != OuttakeState.SpecimenScoring){
            setOuttake(OuttakeState.SpecimenStart)
        } else {
            setOuttake(OuttakeState.SampleStart)
        }
    }


    override fun doInitialize() {
//        setOuttake(OuttakeState.Transfer)
    }

    override fun start() {

    }
}