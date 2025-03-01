package org.riverdell.robotics.autonomous


import com.pedropathing.localization.Pose
import com.pedropathing.util.Constants
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import io.liftgate.robotics.mono.pipeline.single
import org.riverdell.robotics.autonomous.PathFolder.PathChains.fiveSpecPaths.FB_TSPEC
import org.riverdell.robotics.autonomous.PathFolder.PathChains.fiveSpecPaths.FF_TS
import org.riverdell.robotics.autonomous.PathFolder.PathChains.fiveSpecPaths.FSPEC_TB
import org.riverdell.robotics.autonomous.PathFolder.PathChains.fiveSpecPaths.FS_TT
import org.riverdell.robotics.autonomous.PathFolder.PathChains.fiveSpecPaths.FT_TSPEC2
import org.riverdell.robotics.autonomous.PathFolder.PathChains.fiveSpecPaths.TB_FP
import org.riverdell.robotics.autonomous.PathFolder.PathChains.fiveSpecPaths.TF_FB
import org.riverdell.robotics.pedroPathing.constants.FConstants
import org.riverdell.robotics.pedroPathing.constants.LConstants
import org.riverdell.robotics.subsystems.CompositeOuttake
import org.riverdell.robotics.subsystems.Outtake


@Autonomous(name = "5 Specimen")
class `2Sample` : HypnoticAuto({ opmode ->
    //reference
    //opmode.robot.follower.setMaxPower(0.0)
    val toIntake = 0.7
    val toBar = 0.9
    val toPush = 0.95

    opmode.robot.follower.setStartingPose(Pose(8.0, 71.5, Math.toRadians(180.0))) //TODO: Insert Start Pose

    fun SpecOut() {
        opmode.robot.compositeout.setOuttake(CompositeOuttake.OuttakeState.SpecimenScoring).join()
    }

    fun SpecScored() {
        opmode.robot.compositeout.setOuttake(CompositeOuttake.OuttakeState.SpecimenScored).join()
        opmode.robot.outtake.setOuttakeGrip(Outtake.ClawState.Open)
    }

    fun SpecGetReady() {
        opmode.robot.compositeout.setOuttake(CompositeOuttake.OuttakeState.SpecimenIntake).join()
    }
    fun CloseClaw() {
        opmode.robot.outtake.setOuttakeGrip(Outtake.ClawState.Closed).join()
    }
    fun OpenClaw() {
        opmode.robot.outtake.setOuttakeGrip(Outtake.ClawState.Open).join()
    }


    single("firstSpec") {
        Thread.sleep(300)
        SpecOut()
        Thread.sleep(200)
        opmode.robot.follower.setMaxPower(toBar)
        opmode.robot.follower.followPath(TB_FP)
        while (!opmode.robot.follower.atParametricEnd()) {
            if (!opmode.opModeIsActive()) {
                break;
            }
            Thread.sleep(50L)
        }
    }
    single("firstPush") {
        Thread.sleep(300)
        SpecScored()
        Thread.sleep(300)
        opmode.robot.follower.setMaxPower(toPush)
        opmode.robot.follower.followPath(TF_FB)
        SpecGetReady()
        while (!opmode.robot.follower.atParametricEnd()) {
            if (!opmode.opModeIsActive()) {
                break;
            }
            Thread.sleep(50L)
        }
    }
    single("secondPush") {
        Thread.sleep(300)
        opmode.robot.follower.setMaxPower(toPush)
        opmode.robot.follower.followPath(FF_TS)
        while (!opmode.robot.follower.atParametricEnd()) {
            if (!opmode.opModeIsActive()) {
                break;
            }
            Thread.sleep(50L)
        }
    }
    single("thirdPush") {
        Thread.sleep(300)
        opmode.robot.follower.setMaxPower(toPush)
        opmode.robot.follower.followPath(FS_TT)
        while (!opmode.robot.follower.atParametricEnd()) {
            if (!opmode.opModeIsActive()) {
                break;
            }
            Thread.sleep(50L)
        }
    }


    // -----------------------------------------------------SPEC SCORING--------------------------------------------------
    //--------------------------------------------------------------------------------------------------------------------




    // ----------------------------------------------------- First Spec --------------------------------------------------




    single("pathFirstSpec") {
        Thread.sleep(300)
        opmode.robot.follower.setMaxPower(toIntake)
        OpenClaw()
        Thread.sleep(300)
        opmode.robot.follower.followPath(FT_TSPEC2)
        while (!opmode.robot.follower.atParametricEnd()) {
            if (!opmode.opModeIsActive()) {
                break;
            }
            Thread.sleep(50L)
        }
    }
    single("scoreFirst") {
        Thread.sleep(300)
        opmode.robot.follower.setMaxPower(toBar)
        CloseClaw()
        Thread.sleep(700)
        SpecOut()
        Thread.sleep(300)
        opmode.robot.follower.followPath(FSPEC_TB)
        while (!opmode.robot.follower.atParametricEnd()) {
            if (!opmode.opModeIsActive()) {
                break;
            }
            Thread.sleep(50L)
        }
    }

    // ----------------------------------------------------- Second Spec --------------------------------------------------

    single("pathSecondSpec") {
        Thread.sleep(300)
        SpecScored()
        Thread.sleep(300)
        opmode.robot.follower.setMaxPower(toIntake)
        opmode.robot.follower.followPath(FB_TSPEC)
        while (!opmode.robot.follower.atParametricEnd()) {
            if (!opmode.opModeIsActive()) {
                break;
            }
            Thread.sleep(50L)
        }
    }
    single("scoreSecond") {
        Thread.sleep(300)
        opmode.robot.follower.setMaxPower(toBar)
        CloseClaw()
        Thread.sleep(700)
        SpecOut()
        Thread.sleep(300)
        opmode.robot.follower.followPath(FSPEC_TB)
        while (!opmode.robot.follower.atParametricEnd()) {
            if (!opmode.opModeIsActive()) {
                break;
            }
            Thread.sleep(50L)
        }
    }

    // ----------------------------------------------------- Third Spec --------------------------------------------------

    single("pathThirdSpec") {
        Thread.sleep(300)
        SpecScored()
        Thread.sleep(300)
        opmode.robot.follower.setMaxPower(toIntake)
        opmode.robot.follower.followPath(FB_TSPEC)
        while (!opmode.robot.follower.atParametricEnd()) {
            if (!opmode.opModeIsActive()) {
                break;
            }
            Thread.sleep(50L)
        }
    }
    single("scoreThird") {
        Thread.sleep(300)
        opmode.robot.follower.setMaxPower(toBar)
        CloseClaw()
        Thread.sleep(700)
        SpecOut()
        Thread.sleep(300)
        opmode.robot.follower.followPath(FSPEC_TB)
        while (!opmode.robot.follower.atParametricEnd()) {
            if (!opmode.opModeIsActive()) {
                break;
            }
            Thread.sleep(50L)
        }
    }

    // ----------------------------------------------------- Fourth Spec --------------------------------------------------

    single("pathFourthSpec") {
        Thread.sleep(300)
        SpecScored()
        Thread.sleep(300)
        opmode.robot.follower.setMaxPower(toIntake)
        opmode.robot.follower.followPath(FB_TSPEC)
        while (!opmode.robot.follower.atParametricEnd()) {
            if (!opmode.opModeIsActive()) {
                break;
            }
            Thread.sleep(50L)
        }
    }
    single("scoreFourth") {
        Thread.sleep(300)
        opmode.robot.follower.setMaxPower(toBar)
        CloseClaw()
        Thread.sleep(700)
        SpecOut()
        Thread.sleep(300)
        opmode.robot.follower.followPath(FSPEC_TB)
        while (!opmode.robot.follower.atParametricEnd()) {
            if (!opmode.opModeIsActive()) {
                break;
            }
            Thread.sleep(50L)
        }
    }




})

