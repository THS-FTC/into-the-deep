package org.riverdell.robotics.subsystems

import io.liftgate.robotics.mono.states.StateResult
import io.liftgate.robotics.mono.subsystem.AbstractSubsystem
import org.riverdell.robotics.HypnoticRobot
import org.riverdell.robotics.utilities.motionprofile.Constraint
import java.util.concurrent.CompletableFuture

class Intake(private val robot: HypnoticRobot) : AbstractSubsystem()
{
//    @Serializable
//    data class IntakeConfig(
//        //claw
//        val openPosition: Double = 0.4,
//        val closedPosition: Double = 1.0,
//
//        //rotation
//        val transferPosition: Double = 1.0,
//        val observePosition: Double = 0.0,
//        val grabPosition: Double = 0.0,
//
//        // wrist
//        val frontPosition: Double = 0.0,
//        val backPosition: Double = 1.0,
//    )

    enum class ClawState
    {
        Closed, Open,Init
    }
    enum class WristState
    {
        Front, Back,Vertical,Left,Right, Init
    }
    enum class RotationState
    {
        Transfer, Observe, Grab,Idle,Init
    }


    private var currentClawState = ClawState.Init
    private var currentwristState = WristState.Init
    private var currentrotationState = RotationState.Init

    private val wrist = motionProfiledServo(robot.hardware.intakeWrist, Constraint.HALF.scale(5.0))
    private val pulley = motionProfiledServo(robot.hardware.intakePulley, Constraint.HALF.scale(5.0))
    private val grip = motionProfiledServo(robot.hardware.intakeGrip, Constraint.HALF.scale(5.0))

    fun wristRotateTo(position: Double) =  wrist.setMotionProfileTarget(position)
    //grip.setMotionProfileTarget(position)
    fun gripRotateTo(position: Double): CompletableFuture<Void>{
        grip.forcefullySetTarget(position)
        return CompletableFuture.completedFuture(null)
    }
    fun pulleyRotateTo(position: Double) = pulley.setMotionProfileTarget(position)

    //toggles the intake grip
    fun toggleIntakeGrip(): CompletableFuture<Void>
    {
        return if (currentClawState == ClawState.Closed)
        {
            gripRotateTo(IntakeConfig.openPositon).apply {
                currentClawState = ClawState.Open
            }
        } else
        {
            gripRotateTo(IntakeConfig.closePosition).apply {
                currentClawState = ClawState.Closed
            }
        }
    }
    //sets intake grip to either open or close
    fun setIntakeGrip(newState: ClawState): CompletableFuture<Void>
    {
        if (currentClawState == newState)
        {
            return CompletableFuture.completedFuture(null)
        }

        return if (newState == ClawState.Open)
        {
            gripRotateTo(IntakeConfig.openPositon)
                .thenAccept {
                    println(it)
                }
        } else
        {
            gripRotateTo(IntakeConfig.closePosition)
                .thenAccept {
                    println(it)
                }
        }
    }
    fun setWrist(newState: WristState): CompletableFuture<Void>
    {
        if (currentwristState == newState)
        {
            return CompletableFuture.completedFuture(null)
        }

        return if (newState == WristState.Front)
        {
            wristRotateTo(IntakeConfig.frontPosition)
                .thenAccept {
                    println(it)
                }
        } else if (newState == WristState.Back)
        {
            wristRotateTo(IntakeConfig.backPosition)
                .thenAccept {
                    println(it)
                }
        }
        else if (newState == WristState.Left)
        {
            wristRotateTo(IntakeConfig.leftPosition)
                .thenAccept {
                    println(it)
                }
        }
        else if (newState == WristState.Vertical)
        {
            wristRotateTo(IntakeConfig.veritcalPosition)
                .thenAccept {
                    println(it)
                }
        }
        else {
            wristRotateTo(IntakeConfig.rightPosition)
                .thenAccept {
                    println(it)
                }
        }
    }

    fun setRotationPulley(newState: RotationState): CompletableFuture<Void>
    {
        if (currentrotationState == newState)
        {
            return CompletableFuture.completedFuture(null)
        }

        return if (newState == RotationState.Transfer)
        {
            pulleyRotateTo(IntakeConfig.transferPosition)
                .thenAccept {
                    println(it)
                }
        } else if (newState == RotationState.Grab){
            pulleyRotateTo(IntakeConfig.grabPosition)
                .thenAccept {
                    println(it)
                }
        }
        else
        {
            pulleyRotateTo(IntakeConfig.observePosition)
                .thenAccept {
                    println(it)
                }
        }
    }


    override fun start()
    {

    }

    override fun doInitialize()
    {
//        setIntakeGrip(ClawState.Open)
//        setRotationPulley(RotationState.Observe)
//        setWrist(WristState.Front)
    }
}