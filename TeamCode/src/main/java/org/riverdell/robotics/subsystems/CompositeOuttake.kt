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
        Idle,Outtake,Init
    }

    private var currentOuttakeState = OuttakeState.Init


    fun setOuttake(newState: OuttakeState): CompletableFuture<Void>
    {
        if (currentOuttakeState == newState)
        {
            return CompletableFuture.completedFuture(null)
        }

        return if (newState == OuttakeState.Idle)
        {
            //idle outtake stuff here
            robot.outtake.setOuttakeGrip(Outtake.ClawState.Open)
            robot.ov4b.setPulley(OV4B.PulleyState.Intake)
            robot.ov4b.setV4B(OV4B.OV4BState.Idle)
            robot.extension.extendToAndStayAt(0)
            robot.outtake.setWrist(Outtake.WristState.Front).apply { currentOuttakeState = OuttakeState.Idle }
        } else
        {
            //closes the outtake claw and opens the intake claw, then the robot extends forwards
            robot.outtake.setOuttakeGrip(Outtake.ClawState.Closed)
            robot.intake.setIntakeGrip(Intake.ClawState.Open).thenCompose { robot.extension.extendToAndStayAt(200) }

            //the outtake rotates outwards
            robot.ov4b.setPulley(OV4B.PulleyState.Outtake)
            robot.outtake.setWrist(Outtake.WristState.Front)
            robot.ov4b.setV4B(OV4B.OV4BState.Outtake).apply { currentOuttakeState = OuttakeState.Outtake }
        }
    }
    fun toggleOuttake(): CompletableFuture<Void>
    {
        return if (currentOuttakeState == OuttakeState.Outtake)
        {
            robot.outtake.setOuttakeGrip(Outtake.ClawState.Open)
            robot.ov4b.setPulley(OV4B.PulleyState.Intake)
            robot.ov4b.setV4B(OV4B.OV4BState.Idle)
            robot.extension.extendToAndStayAt(0)
            robot.outtake.setWrist(Outtake.WristState.Front).apply { currentOuttakeState = OuttakeState.Idle }
        } else
        {
            //closes the outtake claw and opens the intake claw, then the robot extends forwards
            robot.outtake.setOuttakeGrip(Outtake.ClawState.Closed)
            robot.intake.setIntakeGrip(Intake.ClawState.Open).thenCompose { robot.extension.extendToAndStayAt(200) }

            //the outtake rotates outwards
            robot.ov4b.setPulley(OV4B.PulleyState.Outtake)
            robot.outtake.setWrist(Outtake.WristState.Front)
            robot.ov4b.setV4B(OV4B.OV4BState.Outtake).apply { currentOuttakeState = OuttakeState.Outtake }
        }
    }
//    setOuttake(OuttakeState.Idle)
    override fun doInitialize() {
    }
//    setOuttake(OuttakeState.Idle)
    override fun start() {
//
    }
}