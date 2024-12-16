package org.riverdell.robotics.subsystems

import io.liftgate.robotics.mono.subsystem.AbstractSubsystem
import org.riverdell.robotics.HypnoticRobot
import org.riverdell.robotics.subsystems.Intake.ClawState
import org.riverdell.robotics.utilities.motionprofile.Constraint
import java.util.concurrent.CompletableFuture

class Outtake(private val robot: HypnoticRobot) : AbstractSubsystem() {
//    @Serializable
//    data class OuttakeConfig(
//        val openPosition: Double = 0.5,
//        val closedPosition: Double = 0.0,
//
//        val frontPosition: Double = 0.0,
//        val backPosition: Double = 0.5,
//
//        val transferPosition: Double = 0.4,
//        val outtakePositon: Double = 0.0,
//    )

    enum class ClawState {
        Closed, Open, Idle
    }

    enum class PulleyState {
        Transfer, Bucket, Idle, Specimen
    }

//    private val outtakeConfig = konfig<OuttakeConfig>()
//        .apply {
//            println("HORS ${get().openPosition}")
//        }

    //    fun wristRotateTo(position: Double) = wrist.setMotionProfileTarget(position)
    fun pulleyRotateTo(position: Double): CompletableFuture<Void> {
//        pulley.forcefullySetTarget(position)
        return CompletableFuture.completedFuture(null)
    }

    fun gripRotateTo(position: Double): CompletableFuture<Void> {
        actuator.forcefullySetTarget(position)
        return CompletableFuture.completedFuture(null)
    }

//    private val pulley = motionProfiledServo(robot.hardware.outtakeWrist, Constraint.HALF.scale(5.0))
    private val actuator = motionProfiledServo(
        robot.hardware.outtakeGrip,
        Constraint.HALF.scale(5.0)
    ) // change to outtakegrip

    private var currentClawState = ClawState.Idle

    fun setOuttakeGrip(newState: ClawState): CompletableFuture<Void> {
        if (currentClawState == newState) {
            return CompletableFuture.completedFuture(null)
        }

        return if (newState == ClawState.Open) {
            currentClawState = ClawState.Open
            gripRotateTo(OutakeConfig.openPosition)
                .thenAccept {
                    println(it)
                }
        } else {
            currentClawState = ClawState.Closed
            gripRotateTo(OutakeConfig.closePositon)
                .thenAccept {
                    println(it)
                }
        }
    }

    fun toggleOuttakeGrip(): CompletableFuture<Void> {
        return if (currentClawState == ClawState.Closed) {
            gripRotateTo(OutakeConfig.openPosition).apply {
                currentClawState = ClawState.Open
            }
        } else {
            gripRotateTo(OutakeConfig.closePositon).apply {
                currentClawState = ClawState.Closed
            }
        }
    }

    override fun start() {

    }

    override fun doInitialize() {
//        setWrist(WristState.Front)
//        setOuttakeGrip(ClawState.Open)
    }
}