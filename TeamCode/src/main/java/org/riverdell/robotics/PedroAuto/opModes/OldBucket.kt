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

@Autonomous(name = "4+0 Bucket", group = "Comp")
class OldBucket : HypnoticAuto({ opmode ->
    single("subsystems") {
        opmode.robot.compositeout.setOuttake(CompositeOuttake.OuttakeState.Outtake)
        Thread.sleep(50L)
        opmode.robot.follower.followPath(Paths.slant_to_bucket)
        while (!opmode.robot.follower.atParametricEnd()) {
            if (!opmode.opModeIsActive()) {
                break;
            }
            Thread.sleep(50L)
        }
    }
    single("Drop Sample") {
        Thread.sleep(700)
        opmode.robot.outtake.setOuttakeGrip(Outtake.ClawState.Open)
        Thread.sleep(700)

    }
    /*
    //simultaneous("First Sample") {
    single("pathing") {
        opmode.robot.follower.followPath(Paths.bucket_to_right)
        while (!opmode.robot.follower.atParametricEnd()) {
            if (!opmode.opModeIsActive()) {
                break;
            }
            Thread.sleep(50L)
        }
    }
    single("subsystems") {
        Thread.sleep(800)
        opmode.robot.compositeout.setOuttake(CompositeOuttake.OuttakeState.Transfer)
        opmode.robot.iv4b.setV4B(IV4B.V4BState.Observe)
        opmode.robot.intake.setRotationPulley(Intake.RotationState.Grab)
    }
    //}
    single("Grab Right") {
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
    }*/
})