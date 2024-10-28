package org.riverdell.robotics.subsystems

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import io.liftgate.robotics.mono.konfig.konfig
import io.liftgate.robotics.mono.states.StateResult
import io.liftgate.robotics.mono.subsystem.AbstractSubsystem
import kotlinx.serialization.Serializable
import org.riverdell.robotics.utilities.motionprofile.ProfileConstraints
import java.util.concurrent.CompletableFuture

class CompositeIntake(opMode: LinearOpMode) : AbstractSubsystem()
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

//    enum class ClawState
//    {
//        Closed, Open
//    }
//    enum class WristState
//    {
//        Front, Back
//    }
//    enum class RotationState
//    {
//        Transfer, Observe, Grab,Idle
//    }
//
////    private val intakeConfig = konfig<IntakeConfig>()
////        .apply {
////            println("HORS ${get().openPosition}")
////        }
//
//    private var currentClawState = ClawState.Closed
//    private var currentwristState = WristState.Front
//    private var currentrotationState = RotationState.Idle
//
//    private val wristConstraints = konfig<ProfileConstraints> { withCustomFileID("intake_wrist_motionprofile") }
//    private val wrist = motionProfiledServo("intake_wrist", wristConstraints)
//
//    private val pulleyConstraints = konfig<ProfileConstraints> { withCustomFileID("intake_pulley_motionprofile") }
//    private val pulley = motionProfiledServo("intake_pulley", pulleyConstraints)
//
//    private val rotationConstraints = konfig<ProfileConstraints> { withCustomFileID("intake_grip_motionprofile") }
//    private val grip = motionProfiledServo("intake_grip", rotationConstraints)
//
//    fun wristRotateTo(position: Double) = wrist.setMotionProfileTarget(position)
//    fun gripRotateTo(position: Double) = grip.setMotionProfileTarget(position)
//    fun pulleyRotateTo(position: Double) = pulley.setMotionProfileTarget(position)
//
//    //toggles the intake grip
//    fun toggleIntakeGrip(): CompletableFuture<StateResult>
//    {
//        return if (currentClawState == ClawState.Closed)
//        {
//            gripRotateTo(IntakeConfig.openPosition).apply {
//                currentClawState = ClawState.Open
//            }
//        } else
//        {
//            gripRotateTo(IntakeConfig.closePositon).apply {
//                currentClawState = ClawState.Closed
//            }
//        }
//    }
//    //sets intake grip to either open or close
//    fun setIntakeGrip(newState: ClawState): CompletableFuture<Void>
//    {
//        if (currentClawState == newState)
//        {
//            return CompletableFuture.completedFuture(null)
//        }
//
//        return if (newState == ClawState.Open)
//        {
//            gripRotateTo(IntakeConfig.openPosition)
//                .thenAccept {
//                    println(it)
//                }
//        } else
//        {
//            gripRotateTo(IntakeConfig.closePositon)
//                .thenAccept {
//                    println(it)
//                }
//        }
//    }
//
//    fun setRotationPulley(newState: RotationState): CompletableFuture<Void>
//    {
//        if (currentrotationState == newState)
//        {
//            return CompletableFuture.completedFuture(null)
//        }
//
//        return if (newState == RotationState.Transfer)
//        {
//            pulleyRotateTo(IntakeConfig.transferPosition)
//                .thenAccept {
//                    println(it)
//                }
//        } else if (newState == RotationState.Grab){
//            pulleyRotateTo(IntakeConfig.grabPosition)
//                .thenAccept {
//                    println(it)
//                }
//        }
//        else
//        {
//            pulleyRotateTo(IntakeConfig.observePosition)
//                .thenAccept {
//                    println(it)
//                }
//        }
//    }
//
//
   override fun start()
   {

   }

    override fun doInitialize()
    {

   }
}