package org.riverdell.robotics.subsystems.composite

import io.liftgate.robotics.mono.subsystem.AbstractSubsystem
import org.riverdell.robotics.HypnoticRobot
import org.riverdell.robotics.subsystems.intake.other.Intake
import org.riverdell.robotics.subsystems.outtake.other.Outtake
import org.riverdell.robotics.subsystems.slides.SlideConfig
import org.riverdell.robotics.subsystems.outtake.v4b.OV4B
import java.util.concurrent.CompletableFuture

class CompositeOuttake(val robot: HypnoticRobot) : AbstractSubsystem() {

    enum class OuttakeState {
        Transfer, Outtake, Init, SpecimenUp,SpecimenDown
    }

    var currentOuttakeState = OuttakeState.Init


    fun setOuttake(newState: OuttakeState): CompletableFuture<Void> {
        if (currentOuttakeState == newState) {
            return CompletableFuture.completedFuture(null)
        }

        return if (newState == OuttakeState.Transfer) {
            //idle outtake stuff here
            robot.compositein.setIntake(CompositeIntake.IntakeState.Idle).thenCompose {
                CompletableFuture.allOf(
                    robot.ov4b.setPulley(OV4B.PulleyState.Intake),
                    robot.ov4b.setV4B(OV4B.OV4BState.Transfer),
                    robot.lift.extendToAndStayAt(SlideConfig.liftClosed),
                    robot.outtake.setWrist(Outtake.WristState.Front),
                )
            }.thenAccept { robot.lift.idle() }.apply { currentOuttakeState = OuttakeState.Transfer }
        } else if (newState == OuttakeState.Outtake) {
            //closes the outtake claw and opens the intake claw, then the robot extends forwards
            robot.intake.setIntakeGrip(Intake.ClawState.Open)
                .thenAccept { robot.extension.extendToAndStayAt(SlideConfig.extendoGetOut) }
            //the outtake rotates outwards
            //robot.ov4b.setPulley(OV4B.PulleyState.Outtake)
            robot.outtake.setWrist(Outtake.WristState.Front)
            robot.lift.extendToAndStayAt(SlideConfig.liftHighBucket)
            robot.ov4b.setV4B(OV4B.OV4BState.Outtake)
                .apply { currentOuttakeState = OuttakeState.Outtake }
        } else if (newState == OuttakeState.SpecimenUp) {
            robot.intake.setIntakeGrip(Intake.ClawState.Open)
                robot.intake.setRotationPulley(Intake.RotationState.Grab)
                .thenAccept { robot.extension.extendToAndStayAt(SlideConfig.extendoGetOut) }
            //the outtake rotates outwards
            //robot.ov4b.setPulley(OV4B.PulleyState.Outtake)
            robot.outtake.setWrist(Outtake.WristState.Front)
            robot.lift.extendToAndStayAt(SlideConfig.liftSpecimen)
            robot.ov4b.setV4B(OV4B.OV4BState.Specimen)
                .apply { currentOuttakeState = OuttakeState.SpecimenUp }
        }else if (newState == OuttakeState.SpecimenDown) {
            robot.lift.extendToAndStayAt(SlideConfig.downSpecimen).thenCompose { robot.ov4b.setV4B(
                OV4B.OV4BState.Specimen) }
//            robot.outtake.toggleOuttakeGrip().thenCompose{robot.ov4b.setV4B(OV4B.OV4BState.Transfer)}
                .apply { currentOuttakeState = OuttakeState.SpecimenDown }
        }
        else{
            robot.compositein.setIntake(CompositeIntake.IntakeState.Idle).thenCompose {
                CompletableFuture.allOf(
                    robot.ov4b.setPulley(OV4B.PulleyState.Intake),
                    robot.ov4b.setV4B(OV4B.OV4BState.Transfer),
                    robot.lift.extendToAndStayAt(SlideConfig.liftClosed),
                    robot.outtake.setWrist(Outtake.WristState.Front),
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

    fun toggleSpecimen() : CompletableFuture<Void>{
        return if (currentOuttakeState == OuttakeState.Transfer || currentOuttakeState == OuttakeState.SpecimenDown){
            setOuttake(OuttakeState.SpecimenUp)
        }
        else{
            setOuttake(OuttakeState.SpecimenDown)
        }
    }

    override fun doInitialize() {
//        setOuttake(OuttakeState.Transfer)
    }

    override fun start() {

    }
}