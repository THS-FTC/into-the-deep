package org.riverdell.robotics.autonomous

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.pedropathing.localization.Pose;
import io.liftgate.robotics.mono.pipeline.single


import org.riverdell.robotics.subsystems.CompositeIntake
import org.riverdell.robotics.subsystems.CompositeOuttake
import org.riverdell.robotics.subsystems.IV4B
import org.riverdell.robotics.subsystems.Intake
import org.riverdell.robotics.subsystems.Outtake

@Autonomous(name = "5 Specimen")
class `5Spec` : HypnoticAuto({ opmode ->
    //reference
    //opmode.robot.follower.setMaxPower(0.0)
    val toIntake = 0.7
    val toBar = 0.9
    val toPush = 0.95

    opmode.robot.follower.setStartingPose(Pose(0.000, 0.000, Math.toRadians(225.0))) //TODO: Insert Start Pose

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
        opmode.robot.follower.setMaxPower(toBar)
        //opmode.robot.follower.followPath(Paths.park_to_bar)
        SpecOut()
        while (!opmode.robot.follower.atParametricEnd()) {
            if (!opmode.opModeIsActive()) {
                break;
            }
            Thread.sleep(50L)
        }
    }
    single("firstPush") {
        SpecScored()
        Thread.sleep(300)
        opmode.robot.follower.setMaxPower(toPush)
        //opmode.robot.follower.followPath(Paths.bar_to_first)
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
        //opmode.robot.follower.followPath(Paths.first_to_second)
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
        //opmode.robot.follower.followPath(Paths.second_to_third)
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
        //opmode.robot.follower.followPath(Paths.third_to_FS)
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
        Thread.sleep(200)
        SpecOut()
        Thread.sleep(300)
        //opmode.robot.follower.followPath(Paths.FS_to_Bar)
        while (!opmode.robot.follower.atParametricEnd()) {
            if (!opmode.opModeIsActive()) {
                break;
            }
            Thread.sleep(50L)
        }
    }

    // ----------------------------------------------------- Second Spec --------------------------------------------------

    single("pathSecondSpec") {
        SpecScored()
        Thread.sleep(300)
        opmode.robot.follower.setMaxPower(toIntake)
        //opmode.robot.follower.followPath(Paths.Bar_SS)
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
        Thread.sleep(200)
        SpecOut()
        Thread.sleep(300)
        //opmode.robot.follower.followPath(Paths.SS_to_Bar)
        while (!opmode.robot.follower.atParametricEnd()) {
            if (!opmode.opModeIsActive()) {
                break;
            }
            Thread.sleep(50L)
        }
    }

    // ----------------------------------------------------- Third Spec --------------------------------------------------

    single("pathThirdSpec") {
        SpecScored()
        Thread.sleep(300)
        opmode.robot.follower.setMaxPower(toIntake)
        //opmode.robot.follower.followPath(Paths.Bar_TS)
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
        Thread.sleep(200)
        SpecOut()
        Thread.sleep(300)
        //opmode.robot.follower.followPath(Paths.TS_to_Bar)
        while (!opmode.robot.follower.atParametricEnd()) {
            if (!opmode.opModeIsActive()) {
                break;
            }
            Thread.sleep(50L)
        }
    }

    // ----------------------------------------------------- Fourth Spec --------------------------------------------------

    single("pathFourthSpec") {
        SpecScored()
        Thread.sleep(300)
        opmode.robot.follower.setMaxPower(toIntake)
        //opmode.robot.follower.followPath(Paths.Bar_FS)
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
        Thread.sleep(200)
        SpecOut()
        Thread.sleep(300)
        //opmode.robot.follower.followPath(Paths.FS_to_Bar)
        while (!opmode.robot.follower.atParametricEnd()) {
            if (!opmode.opModeIsActive()) {
                break;
            }
            Thread.sleep(50L)
        }
    }




})

