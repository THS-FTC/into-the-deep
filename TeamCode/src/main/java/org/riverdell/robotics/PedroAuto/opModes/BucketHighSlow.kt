package org.riverdell.robotics.autonomous.impl.tests

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import io.liftgate.robotics.mono.pipeline.single
import org.riverdell.robotics.PedroAuto.Constants.pathTimes
import org.riverdell.robotics.PedroAuto.SlowBucket.SlowPaths
import org.riverdell.robotics.autonomous.HypnoticAuto
import org.riverdell.robotics.pedroPathing.localization.Pose
import org.riverdell.robotics.subsystems.CompositeIntake
import org.riverdell.robotics.subsystems.CompositeOuttake
import org.riverdell.robotics.subsystems.IV4B
import org.riverdell.robotics.subsystems.Intake
import org.riverdell.robotics.subsystems.OV4B
import org.riverdell.robotics.subsystems.Outtake

@Autonomous(name = "4+0 Bucket", group = "Comp")
class BucketHighSlow : HypnoticAuto({ opmode ->

    //initial sample

    single("pathing_to_pre") {
        opmode.robot.follower.setStartingPose(Pose(0.000, 0.000, Math.toRadians(225.0)))
        opmode.robot.compositeout.setOuttake(CompositeOuttake.OuttakeState.Outtake)
        Thread.sleep(50L)
        opmode.robot.follower.followPath(SlowPaths.slant_to_pre,true)

        //while loop that keeps the follower on and sets outtake to out at a certain time

        while (!opmode.robot.follower.atParametricEnd()) {
            //checks if opmode is stopped
            if (!opmode.opModeIsActive()) {
                break;
            }
            Thread.sleep(50L)
        }
    }
    single("pathing_to_bucket") {
        Thread.sleep(400L)
        opmode.robot.follower.followPath(SlowPaths.initialPre_to_bucket)

        //while loop that keeps the follower on and sets outtake to out at a certain time
        while (!opmode.robot.follower.atParametricEnd()) {
            if (!opmode.opModeIsActive()) {
                break;
            }
            Thread.sleep(50L)
        }
    }
    //drops sample
    single("First Drop Sample") {
        Thread.sleep(1000L)
        opmode.robot.outtake.setOuttakeGrip(Outtake.ClawState.Open)
        Thread.sleep(400L)

    }



    //start of first cycle to samples (right sample)




    single("pathing/outtake in") {
        Thread.sleep(400L)
        opmode.robot.follower.followPath(SlowPaths.bucket_to_right,true)
        opmode.robot.compositein.setIntake(CompositeIntake.IntakeState.Intake)
        Thread.sleep(50L)
        while (!opmode.robot.follower.atParametricEnd()) {
            val t = opmode.robot.follower.currentTValue
            if (!opmode.opModeIsActive()) {
                break;
            }
            Thread.sleep(50L)
        }
    }
    single("Grab Right") {
        Thread.sleep(800L)
        opmode.robot.compositeout.setOuttake(CompositeOuttake.OuttakeState.Transfer)
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
        Thread.sleep(500L)
        opmode.robot.compositein.setIntake(CompositeIntake.IntakeState.Transfer)
        Thread.sleep(1000L)
        opmode.robot.outtake.setOuttakeGrip(Outtake.ClawState.Closed)
        Thread.sleep(600L)
    }
    single("pathing_to_Prebucket"){
        opmode.robot.compositeout.setOuttake(CompositeOuttake.OuttakeState.Outtake)
        Thread.sleep(400L)
        opmode.robot.follower.followPath(SlowPaths.right_to_pre,true)
        while (!opmode.robot.follower.atParametricEnd()) {
            val t = opmode.robot.follower.currentTValue
            if (!opmode.opModeIsActive()) {
                break;
            }
            Thread.sleep(50L)
        }
    }
    single("pathing_to_bucket") {
        Thread.sleep(400L)
        opmode.robot.follower.followPath(SlowPaths.rightPre_to_bucket)
        //while loop that keeps the follower on and sets outtake to out at a certain time

        while (!opmode.robot.follower.atParametricEnd()) {
            //checks if opmode is stopped
            if (!opmode.opModeIsActive()) {
                break;
            }
            Thread.sleep(50L)
        }
    }
    single("Second Drop Sample") {
        Thread.sleep(1000L)
        opmode.robot.outtake.setOuttakeGrip(Outtake.ClawState.Open)
        Thread.sleep(400L)

    }





    //start of second cycle to samples (middle sample)



    single("pathing/outtake in") {
        Thread.sleep(400L)
        opmode.robot.compositein.setIntake(CompositeIntake.IntakeState.Intake)
        Thread.sleep(50L)
        opmode.robot.follower.followPath(SlowPaths.bucket_to_middle)
        while (!opmode.robot.follower.atParametricEnd()) {
            val t = opmode.robot.follower.currentTValue
            if (!opmode.opModeIsActive()) {
                break;
            }
            Thread.sleep(50L)
        }
    }
    single("Grab Middle") {
        Thread.sleep(400L)
        opmode.robot.compositeout.setOuttake(CompositeOuttake.OuttakeState.Transfer)
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
        Thread.sleep(500L)
        opmode.robot.compositein.setIntake(CompositeIntake.IntakeState.Transfer)
        Thread.sleep(1000L)
        opmode.robot.outtake.setOuttakeGrip(Outtake.ClawState.Closed)
        Thread.sleep(600L)
    }
    single("pathing_to_Prebucket"){
        opmode.robot.compositeout.setOuttake(CompositeOuttake.OuttakeState.Outtake)
        Thread.sleep(50L)
        opmode.robot.follower.followPath(SlowPaths.middle_to_pre)
        while (!opmode.robot.follower.atParametricEnd()) {
            val t = opmode.robot.follower.currentTValue
            if (!opmode.opModeIsActive()) {
                break;
            }
            Thread.sleep(50L)
        }
    }
    single("pathing_to_bucket") {
        Thread.sleep(400L)
        opmode.robot.follower.followPath(SlowPaths.middlePre_to_bucket)

        //while loop that keeps the follower on and sets outtake to out at a certain time

        while (!opmode.robot.follower.atParametricEnd()) {
            //checks if opmode is stopped
            if (!opmode.opModeIsActive()) {
                break;
            }
            Thread.sleep(50L)
        }
    }
    single("Second Drop Sample") {
        Thread.sleep(1000L)
        opmode.robot.outtake.setOuttakeGrip(Outtake.ClawState.Open)
        Thread.sleep(400L)

    }



    //start of third cycle to samples (left sample)


    single("pathing/outtake in") {
        Thread.sleep(200L)
        opmode.robot.compositein.setIntake(CompositeIntake.IntakeState.Intake)
        opmode.robot.intake.setWrist(Intake.WristState.Right)
        Thread.sleep(500L)
        opmode.robot.follower.followPath(SlowPaths.bucket_to_left)
        while (!opmode.robot.follower.atParametricEnd()) {
            val t = opmode.robot.follower.currentTValue
            if (!opmode.opModeIsActive()) {
                break;
            }
            Thread.sleep(50L)
        }
    }
    single("Grab Left") {
        Thread.sleep(600L)
        opmode.robot.compositeout.setOuttake(CompositeOuttake.OuttakeState.Transfer)
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
        Thread.sleep(500L)
        opmode.robot.compositein.setIntake(CompositeIntake.IntakeState.Transfer)
        Thread.sleep(1500L)
        opmode.robot.outtake.setOuttakeGrip(Outtake.ClawState.Closed)
        Thread.sleep(600L)
    }
    single("pathing_to_Prebucket"){
        opmode.robot.compositeout.setOuttake(CompositeOuttake.OuttakeState.Outtake)
        Thread.sleep(50L)
        opmode.robot.follower.followPath(SlowPaths.left_to_pre)
        while (!opmode.robot.follower.atParametricEnd()) {
            val t = opmode.robot.follower.currentTValue
            if (!opmode.opModeIsActive()) {
                break;
            }
            Thread.sleep(50L)
        }
    }
    single("pathing_to_bucket") {
        Thread.sleep(400L)
        opmode.robot.follower.followPath(SlowPaths.leftPre_to_bucket)
        //while loop that keeps the follower on and sets outtake to out at a certain time
        while (!opmode.robot.follower.atParametricEnd()) {
            //checks if opmode is stopped
            if (!opmode.opModeIsActive()) {
                break;
            }
            Thread.sleep(50L)
        }
    }
    single("Fourth Drop Sample") {
        Thread.sleep(1000L)
        opmode.robot.outtake.setOuttakeGrip(Outtake.ClawState.Open)
        Thread.sleep(400L)

    }


    //heading over to ascent zone


    single("parking"){
        Thread.sleep(50L)
        opmode.robot.compositeout.setOuttake(CompositeOuttake.OuttakeState.Transfer)
        Thread.sleep(50L)
        opmode.robot.follower.followPath(SlowPaths.bucket_to_preascent)
        while (!opmode.robot.follower.atParametricEnd()) {
            val t = opmode.robot.follower.currentTValue
            if (!opmode.opModeIsActive()) {
                break;
            }
            Thread.sleep(50L)
        }
    }
    single("parking"){
        Thread.sleep(50L)
        opmode.robot.ov4b.setV4B(OV4B.OV4BState.Specimen)
        Thread.sleep(50L)
        opmode.robot.compositein.setIntake(CompositeIntake.IntakeState.Idle)
        Thread.sleep(50L)
        opmode.robot.follower.followPath(SlowPaths.pre_to_park)
        while (!opmode.robot.follower.atParametricEnd()) {
            val t = opmode.robot.follower.currentTValue
            if (!opmode.opModeIsActive()) {
                break;
            }
            Thread.sleep(50L)
        }
    }
})