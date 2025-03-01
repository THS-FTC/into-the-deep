package org.riverdell.robotics.subsystems

import io.liftgate.robotics.mono.subsystem.AbstractSubsystem
import org.riverdell.robotics.HypnoticRobot
import org.riverdell.robotics.subsystems.Intake.ClawState
import org.riverdell.robotics.subsystems.Intake.WristState
import org.riverdell.robotics.utilities.motionprofile.Constraint
import java.util.concurrent.CompletableFuture

class Outtake(private val robot: HypnoticRobot) : AbstractSubsystem() {

    enum class ClawState {
        Closed, Open, Idle
    }

    enum class WristState {
        Front, Specimen, Idle
    }

    fun gripRotateTo(position: Double): CompletableFuture<Void> {
        actuator.forcefullySetTarget(position)
        return CompletableFuture.completedFuture(null)
    }

    private val actuator = motionProfiledServo(
        robot.hardware.outtakeGrip,
        Constraint.HALF.scale(5.0)
    ) // change to outtakegrip
    private val wrist =
        motionProfiledServo(robot.hardware.outtakeWrist, Constraint.HALF.scale(15.0))

    fun wristRotateTo(position: Double): CompletableFuture<Void> {
        wrist.forcefullySetTarget(position)
        return CompletableFuture.completedFuture(null)
    }

    private var currentClawState = ClawState.Idle
    private var currentWristState = WristState.Idle

    fun setOuttakeGrip(newState: ClawState): CompletableFuture<Void> {
        if (currentClawState == newState) {
            return CompletableFuture.completedFuture(null)
        }

        return if (newState == ClawState.Open) {
            currentClawState = ClawState.Open
            gripRotateTo(OutakeConfig.openPosition)
        } else {
            currentClawState = ClawState.Closed
            gripRotateTo(OutakeConfig.closePositon)
        }
    }

    fun setWrist(newState: WristState): CompletableFuture<Void> {
        if (currentWristState == newState) {
            return CompletableFuture.completedFuture(null)
        }

        return if (newState == WristState.Front) {
            currentWristState = WristState.Front
            wristRotateTo(OutakeConfig.frontPosition)
        } else {
            currentWristState = WristState.Specimen
            wristRotateTo(OutakeConfig.specimenPosition)
        }
    }

    fun toggleOuttakeGrip(): CompletableFuture<Void> {
        return if (currentClawState == ClawState.Closed) {
            setOuttakeGrip(ClawState.Open)
        } else {
            setOuttakeGrip(ClawState.Closed)
        }
    }

    override fun start() {

    }

    override fun doInitialize() {
//        setWrist(WristState.Front)
//        setOuttakeGrip(ClawState.Open)
    }
}