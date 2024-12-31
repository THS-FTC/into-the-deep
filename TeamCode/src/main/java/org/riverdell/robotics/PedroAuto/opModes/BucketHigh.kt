package org.riverdell.robotics.autonomous.impl.tests

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import io.liftgate.robotics.mono.pipeline.simultaneous
import org.riverdell.robotics.pedroPathing.follower.Follower
import org.riverdell.robotics.pedroPathing.localization.Pose
import io.liftgate.robotics.mono.pipeline.single
import org.riverdell.robotics.PedroAuto.Constants.Points
import org.riverdell.robotics.PedroAuto.Constants.pathTimes
import org.riverdell.robotics.PedroAuto.Paths.Paths
import org.riverdell.robotics.autonomous.HypnoticAuto
import org.riverdell.robotics.subsystems.CompositeIntake
import org.riverdell.robotics.subsystems.CompositeOuttake
import org.riverdell.robotics.subsystems.IV4B
import org.riverdell.robotics.subsystems.Intake
import org.riverdell.robotics.subsystems.OV4B
import org.riverdell.robotics.subsystems.Outtake
import java.util.concurrent.CompletableFuture

@Autonomous(name = "2+0 Bucket", group = "Comp")
class BucketHigh : HypnoticAuto({ opmode ->

    //initial sample

    single("subsystems") {
//        opmode.robot.compositeout.setOuttake(CompositeOuttake.OuttakeState.Outtake)
//        Thread.sleep(50L)
        //starts path to the bucket
        opmode.robot.follower.setStartingPose(Pose(0.000, 0.000, Math.toRadians(225.0)))
        opmode.robot.follower.followPath(Paths.slant_to_bucket,true)

        //while loop that keeps the follower on and sets outtake to out at a certain time

        while (!opmode.robot.follower.atParametricEnd()) {
            //checks if opmode is stopped
            val t = opmode.robot.follower.currentTValue
            if (t >= pathTimes.initialSampleOuttakeOut - 0.07 && t <= pathTimes.initialSampleOuttakeOut + 0.07) {
                opmode.robot.compositeout.setOuttake(CompositeOuttake.OuttakeState.Outtake)
                Thread.sleep(50L)
            }
            if (t >= pathTimes.sampleExtendoOut - 0.07 && t <= pathTimes.sampleExtendoOut + 0.07) {
                opmode.robot.compositein.setIntake(CompositeIntake.IntakeState.Intake)
                Thread.sleep(50L)
            }
            if (!opmode.opModeIsActive()) {
                break;
            }
            Thread.sleep(50L)
        }
    }
    //drops sample
    single("First Drop Sample") {
        Thread.sleep(700)
        opmode.robot.outtake.setOuttakeGrip(Outtake.ClawState.Open)
        Thread.sleep(300)

    }


    //start of first cycle to samples (right sample)


    single("pathing/outtake in") {
        opmode.robot.follower.followPath(Paths.bucket_to_right,true)
        while (!opmode.robot.follower.atParametricEnd()) {
            val t = opmode.robot.follower.currentTValue
            if (t >= pathTimes.sampleOuttakeIn - 0.07 && t <= pathTimes.sampleOuttakeIn + 0.07) {
                opmode.robot.compositeout.setOuttake(CompositeOuttake.OuttakeState.Transfer)
                Thread.sleep(50L)
            }
            if (t >= pathTimes.sampleExtendoOut - 0.07 && t <= pathTimes.sampleExtendoOut + 0.07) {
                opmode.robot.compositein.setIntake(CompositeIntake.IntakeState.Intake)
                Thread.sleep(50L)
            }
            if (!opmode.opModeIsActive()) {
                break;
            }
            Thread.sleep(50L)
        }
    }
    single("Grab Right") {
        Thread.sleep(600)
        opmode.robot.intake.setIntakeGrip(Intake.ClawState.Open)
        opmode.robot.intake.setRotationPulley(Intake.RotationState.Grab)
        opmode.robot.iv4b.setV4B(IV4B.V4BState.Grab)
            .thenCompose { opmode.robot.intake.setIntakeGrip(Intake.ClawState.Closed) }
            .thenCompose {
                opmode.robot.iv4b.setV4B(
                    IV4B.V4BState.Observe
                )
            }
        opmode.robot.intake.setRotationPulley(Intake.RotationState.Observe)
        Thread.sleep(500)
        opmode.robot.compositein.setIntake(CompositeIntake.IntakeState.Transfer)
        Thread.sleep(600)
    }
    single("pathing_to_bucket") {
        opmode.robot.follower.followPath(Paths.right_to_bucket,true)
        while (!opmode.robot.follower.atParametricEnd()) {
            val t = opmode.robot.follower.currentTValue
            if (t >= pathTimes.sampleOuttakeGripClose - 0.07 && t <= pathTimes.sampleOuttakeGripClose + 0.07) {
                opmode.robot.outtake.setOuttakeGrip(Outtake.ClawState.Closed)
                Thread.sleep(50L)
            }
            if (t >= pathTimes.SampleOuttakeOut - 0.07 && t <= pathTimes.SampleOuttakeOut + 0.07) {
                opmode.robot.compositeout.setOuttake(CompositeOuttake.OuttakeState.Outtake)
                Thread.sleep(50L)
            }
            if (!opmode.opModeIsActive()) {
                break;
            }
            Thread.sleep(50L)
        }
        Thread.sleep(500)
    }
    single("Second Drop Sample") {
        Thread.sleep(700)
        opmode.robot.outtake.setOuttakeGrip(Outtake.ClawState.Open)
        Thread.sleep(300)

    }
})


    /*

    //start of second cycle to samples (middle sample)

    //TODO: Replace all parts of the right side sample for middle paths



    single("pathing/outtake in") {
        opmode.robot.follower.followPath(Paths.bucket_to_middle)
        while (!opmode.robot.follower.atParametricEnd()) {
            val t = opmode.robot.follower.currentTValue
            if (!opmode.opModeIsActive()) {
                break;
            }
            if (t >= pathTimes.sampleOuttakeIn-0.07 && t <= pathTimes.sampleOuttakeIn+0.07){
                opmode.robot.compositeout.setOuttake(CompositeOuttake.OuttakeState.Transfer)
            }
            else if (t >= pathTimes.sampleExtendoOut-0.07 && t <= pathTimes.sampleExtendoOut+0.07){
                opmode.robot.compositein.setIntake(CompositeIntake.IntakeState.Intake)
            }
            Thread.sleep(50L)
        }
    }
    single("Grab Middle") {
        Thread.sleep(600)
        opmode.robot.intake.setIntakeGrip(Intake.ClawState.Open)
        opmode.robot.intake.setRotationPulley(Intake.RotationState.Grab)
        opmode.robot.iv4b.setV4B(IV4B.V4BState.Grab)
            .thenCompose { opmode.robot.intake.setIntakeGrip(Intake.ClawState.Closed) }
            .thenCompose {
                opmode.robot.iv4b.setV4B(
                    IV4B.V4BState.Observe
                )
            }
        opmode.robot.intake.setRotationPulley(Intake.RotationState.Observe)
        Thread.sleep(500)
    }
    single("pathing_to_bucket"){
        opmode.robot.follower.followPath(Paths.middle_to_bucket)
        while (!opmode.robot.follower.atParametricEnd()) {
            val t = opmode.robot.follower.currentTValue
            if (!opmode.opModeIsActive()) {
                break;
            }
            if (t >= pathTimes.sampleExtendoIn-0.07 && t <= pathTimes.sampleExtendoIn+0.07){
                opmode.robot.compositein.setIntake(CompositeIntake.IntakeState.Transfer)
            }
            else if (t >= pathTimes.sampleOuttakeGripClose-0.07 && t <= pathTimes.sampleOuttakeGripClose+0.07){
                opmode.robot.outtake.setOuttakeGrip(Outtake.ClawState.Closed)
            }
            else if (t >= pathTimes.SampleOuttakeOut-0.07 && t <= pathTimes.SampleOuttakeOut+0.07){
                opmode.robot.compositeout.setOuttake(CompositeOuttake.OuttakeState.Outtake)
            }
            Thread.sleep(50L)
        }
    }
    single("Third Drop Sample") {
        Thread.sleep(700)
        opmode.robot.outtake.setOuttakeGrip(Outtake.ClawState.Open)
        Thread.sleep(300)

    }



    //start of third cycle to samples (left sample)


    single("pathing/outtake in") {
        opmode.robot.follower.followPath(Paths.bucket_to_left)
        while (!opmode.robot.follower.atParametricEnd()) {
            val t = opmode.robot.follower.currentTValue
            if (!opmode.opModeIsActive()) {
                break;
            }
            if (t >= pathTimes.sampleOuttakeIn-0.07 && t <= pathTimes.sampleOuttakeIn+0.07){
                opmode.robot.compositeout.setOuttake(CompositeOuttake.OuttakeState.Transfer)
            }
            else if (t >= pathTimes.sampleExtendoOut-0.07 && t <= pathTimes.sampleExtendoOut+0.07){
                opmode.robot.compositein.setIntake(CompositeIntake.IntakeState.Intake)
                opmode.robot.intake.setWrist(Intake.WristState.Right)
            }
            Thread.sleep(50L)
        }
    }
    single("Grab Left") {
        Thread.sleep(600)
        opmode.robot.intake.setIntakeGrip(Intake.ClawState.Open)
        opmode.robot.intake.setRotationPulley(Intake.RotationState.Grab)
        opmode.robot.iv4b.setV4B(IV4B.V4BState.Grab)
            .thenCompose { opmode.robot.intake.setIntakeGrip(Intake.ClawState.Closed) }
            .thenCompose {
                opmode.robot.iv4b.setV4B(
                    IV4B.V4BState.Observe
                )
            }
        Thread.sleep(500)
    }
    single("pathing_to_bucket"){
        opmode.robot.follower.followPath(Paths.left_to_bucket)
        while (!opmode.robot.follower.atParametricEnd()) {
            val t = opmode.robot.follower.currentTValue
            if (!opmode.opModeIsActive()) {
                break;
            }
            if (t >= pathTimes.sampleExtendoIn-0.07 && t <= pathTimes.sampleExtendoIn+0.07){
                opmode.robot.compositein.setIntake(CompositeIntake.IntakeState.Transfer)
            }
            else if (t >= pathTimes.sampleOuttakeGripClose-0.07 && t <= pathTimes.sampleOuttakeGripClose+0.07){
                opmode.robot.outtake.setOuttakeGrip(Outtake.ClawState.Closed)
            }
            else if (t >= pathTimes.SampleOuttakeOut-0.07 && t <= pathTimes.SampleOuttakeOut+0.07){
                opmode.robot.compositeout.setOuttake(CompositeOuttake.OuttakeState.Outtake)
            }
            Thread.sleep(50L)
        }
    }
    single("Fourth Drop Sample") {
        Thread.sleep(700)
        opmode.robot.outtake.setOuttakeGrip(Outtake.ClawState.Open)
        Thread.sleep(300)

    }


    //heading over to ascent zone


    single("parking"){
        opmode.robot.follower.followPath(Paths.bucket_to_ascent)
        while (!opmode.robot.follower.atParametricEnd()) {
            val t = opmode.robot.follower.currentTValue
            if (!opmode.opModeIsActive()) {
                break;
            }
            else if (t >= pathTimes.ascentOut-0.07 && t <= pathTimes.ascentOut+0.07){
                opmode.robot.ov4b.setV4B(OV4B.OV4BState.Outtake)
            }
            Thread.sleep(50L)
        }
    }
})
*/
