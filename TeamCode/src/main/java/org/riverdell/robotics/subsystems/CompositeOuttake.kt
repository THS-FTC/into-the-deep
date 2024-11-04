package org.riverdell.robotics.subsystems

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import io.liftgate.robotics.mono.states.StateResult
import io.liftgate.robotics.mono.subsystem.AbstractSubsystem
import org.riverdell.robotics.HypnoticRobot
import org.riverdell.robotics.subsystems.Outtake.ClawState
import java.util.Currency
import java.util.concurrent.CompletableFuture

class CompositeOuttake(val robot: HypnoticRobot) : AbstractSubsystem()
{

    enum class OuttakeState
    {
        Transfer,Outtake,Init,Specimen
    }

    private var currentOuttakeState = OuttakeState.Init


    fun setOuttake(newState: OuttakeState): CompletableFuture<Void>
    {
        if (currentOuttakeState == newState)
        {
            return CompletableFuture.completedFuture(null)
        }

        return if (newState == OuttakeState.Transfer)
        {
            //idle outtake stuff here
            robot.outtake.setOuttakeGrip(ClawState.Open)
            robot.ov4b.setPulley(OV4B.PulleyState.Intake)
            robot.ov4b.setV4B(OV4B.OV4BState.Idle)
            robot.lift.extendToAndStayAt(SlideConfig.liftClosed).thenAccept{robot.lift.idle() }
            robot.outtake.setWrist(Outtake.WristState.Front).thenCompose { robot.compositein.setIntake(CompositeIntake.IntakeState.Idle) }
                .apply { currentOuttakeState = OuttakeState.Transfer }
        } else if (newState == OuttakeState.Outtake)
        {
            //closes the outtake claw and opens the intake claw, then the robot extends forwards
            robot.outtake.setOuttakeGrip(ClawState.Closed).thenAccept { robot.intake.setIntakeGrip(Intake.ClawState.Open) }.thenAccept { robot.extension.extendToAndStayAt(SlideConfig.extendoGetOut) }
            //the outtake rotates outwards
            //robot.ov4b.setPulley(OV4B.PulleyState.Outtake)
            robot.outtake.setWrist(Outtake.WristState.Front)
            robot.lift.extendToAndStayAt(SlideConfig.liftHighBucket)
            robot.ov4b.setV4B(OV4B.OV4BState.Outtake).apply { currentOuttakeState = OuttakeState.Outtake }
        }
        else {
            robot.outtake.setOuttakeGrip(ClawState.Closed).thenCompose { robot.intake.setIntakeGrip(Intake.ClawState.Open) }.thenCompose { robot.extension.extendToAndStayAt(SlideConfig.extendoGetOut) }
            //the outtake rotates outwards
            //robot.ov4b.setPulley(OV4B.PulleyState.Outtake)
            robot.outtake.setWrist(Outtake.WristState.Front)
            robot.lift.extendToAndStayAt(SlideConfig.liftSpecimen).thenCompose {  robot.ov4b.setV4B(OV4B.OV4BState.Outtake) }.thenAccept { robot.lift.extendToAndStayAt(SlideConfig.downSpecimen) }
                .thenAccept{ setOuttake(OuttakeState.Transfer)}
                .apply { currentOuttakeState = OuttakeState.Transfer }
        }
    }
    fun toggleBucket(): CompletableFuture<Void>
    {
        return if (currentOuttakeState == OuttakeState.Outtake)
        {
            setOuttake(OuttakeState.Transfer)
        } else
        {
            //closes the outtake claw and opens the intake claw, then the robot extends forwards
            setOuttake(OuttakeState.Outtake)
        }
    }
    fun toggleSpecimen() = setOuttake(OuttakeState.Specimen)

    override fun doInitialize() {
//        setOuttake(OuttakeState.Transfer)
    }

    override fun start() {

    }
}