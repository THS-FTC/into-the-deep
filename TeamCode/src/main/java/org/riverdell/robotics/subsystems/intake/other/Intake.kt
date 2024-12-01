package org.riverdell.robotics.subsystems.intake

import io.liftgate.robotics.mono.subsystem.AbstractSubsystem
import org.riverdell.robotics.HypnoticRobot
import org.riverdell.robotics.subsystems.intake.other.ClawState
import org.riverdell.robotics.subsystems.intake.other.PulleyState
import org.riverdell.robotics.subsystems.intake.other.WristState
import org.riverdell.robotics.subsystems.motionProfiledServo
import org.riverdell.robotics.utilities.managed.ServoBehavior
import org.riverdell.robotics.utilities.motionprofile.Constraint
import org.riverdell.robotics.utilities.motionprofile.ProfileConstraints
import java.util.concurrent.CompletableFuture

class Intake(private val robot: HypnoticRobot) : AbstractSubsystem() {
    val wrist = motionProfiledServo(robot.hardware.intakeWrist, ProfileConstraints(20.0, 2.0, 2.0))
    private val grip = motionProfiledServo(robot.hardware.intakeGrip, Constraint.HALF.scale(10.5))
    private val pulley = motionProfiledServo(robot.hardware.intakePulley, Constraint.HALF.scale(10.5))


    var clawState = ClawState.Open
    var wristState = WristState.Lat
    var pulleyState = PulleyState.Observe


    fun openIntake() = setIntake(ClawState.Open)
    fun closeIntake() = setIntake(ClawState.Closed)

    fun setIntake(state: ClawState) = let {
        if (clawState == state)
            return@let CompletableFuture.completedFuture(null)

        clawState = state
        return@let updateClawState()
    }

    fun setPulley(state: PulleyState) = let {
        if (pulleyState == state)
            return@let CompletableFuture.completedFuture(null)

        pulleyState = state
        return@let updatePulleyState()
    }
    fun pulleyObserve() = setPulley(PulleyState.Observe)
    fun pulleyGrab() = setPulley(PulleyState.Grab)
    fun pulleyTransfer() = setPulley(PulleyState.Transfer)

    fun leftWrist() = setWrist(WristState.Left)
    fun rightWrist() = setWrist(WristState.Right)

    fun setWrist(state: WristState) = let {
        if (wristState == state)
            return@let CompletableFuture.completedFuture(null)
        wristState = state
        return@let updateWristState()
    }

    private fun updateClawState(): CompletableFuture<*> {
        return intakeRotateTo(clawState)
    }

    private fun updateWristState(): CompletableFuture<*> {
        return wristRotateTo(wristState)
    }
    private fun updatePulleyState(): CompletableFuture<*> {
        return pulleyRotateTo(pulleyState)
    }

    private fun wristRotateTo(state: WristState) = wrist.setTarget(state.position, ServoBehavior.Direct)
    private fun intakeRotateTo(state: ClawState) = grip.setTarget(state.position, ServoBehavior.Direct)
    private fun pulleyRotateTo(state: PulleyState) = pulley.setTarget(state.position, ServoBehavior.MotionProfile)


    override fun start() {

    }

    override fun doInitialize() {
        updateClawState()
        updateWristState()
        updatePulleyState()
    }
}