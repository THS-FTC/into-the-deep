package org.riverdell.robotics.subsystems.intake.other

import io.liftgate.robotics.mono.subsystem.AbstractSubsystem
import org.riverdell.robotics.HypnoticRobot
import org.riverdell.robotics.subsystems.motionProfiledServo
import org.riverdell.robotics.utilities.managed.ServoBehavior
import org.riverdell.robotics.utilities.motionprofile.Constraint
import org.riverdell.robotics.utilities.motionprofile.ProfileConstraints
import java.util.concurrent.CompletableFuture

class Intake(private val robot: HypnoticRobot) : AbstractSubsystem() {
    val wrist = motionProfiledServo(robot.hardware.intakeWrist, ProfileConstraints(20.0, 2.0, 2.0))
    private val grip = motionProfiledServo(robot.hardware.intakeGrip, Constraint.HALF.scale(10.5))
    private val pulley = motionProfiledServo(robot.hardware.intakePulley, Constraint.HALF.scale(10.5))


    var clawState = IClawState.Open
    var wristState = WristState.Lat
    var pulleyState = IPulleyState.Observe


    fun openIntake() = setIntake(IClawState.Open)
    fun closeIntake() = setIntake(IClawState.Closed)

    fun setIntake(state: IClawState) = let {
        if (clawState == state)
            return@let CompletableFuture.completedFuture(null)

        clawState = state
        return@let updateClawState()
    }

    fun setPulley(state: IPulleyState) = let {
        if (pulleyState == state)
            return@let CompletableFuture.completedFuture(null)

        pulleyState = state
        return@let updatePulleyState()
    }
    fun pulleyObserve() = setPulley(IPulleyState.Observe)
    fun pulleyGrab() = setPulley(IPulleyState.Grab)
    fun pulleyTransfer() = setPulley(IPulleyState.Transfer)

    fun leftWrist() = setWrist(WristState.Left)
    fun rightWrist() = setWrist(WristState.Right)
    fun latWrist() = setWrist(WristState.Lat)
    fun horizWrist() = setWrist(WristState.Horiz)

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
    private fun intakeRotateTo(state: IClawState) = grip.setTarget(state.position, ServoBehavior.Direct)
    private fun pulleyRotateTo(state: IPulleyState) = pulley.setTarget(state.position, ServoBehavior.MotionProfile)


    override fun start() {

    }

    override fun doInitialize() {
        updateClawState()
        updateWristState()
        updatePulleyState()
    }
}