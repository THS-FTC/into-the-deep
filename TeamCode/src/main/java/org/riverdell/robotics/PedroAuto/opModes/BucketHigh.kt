package org.riverdell.robotics.autonomous.impl.tests

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import io.liftgate.robotics.mono.pipeline.simultaneous
import org.riverdell.robotics.pedroPathing.follower.Follower
import org.riverdell.robotics.pedroPathing.localization.Pose
import io.liftgate.robotics.mono.pipeline.single
import org.riverdell.robotics.PedroAuto.Constants.Points
import org.riverdell.robotics.PedroAuto.Paths.Paths
import org.riverdell.robotics.autonomous.HypnoticAuto
import org.riverdell.robotics.subsystems.CompositeOuttake
import org.riverdell.robotics.subsystems.Outtake

@Autonomous(name = "4+0 Bucket", group = "Comp")
class BucketHigh : HypnoticAuto({ opmode ->
    simultaneous("Intial Bucket Path") {
        single("pathing") {
            opmode.robot.follower.followPath(Paths.slant_to_basket)
            while (!opmode.robot.follower.atParametricEnd()) {
                if (!opmode.opModeIsActive()) {
                    break;
                }
                Thread.sleep(50L)
            }
        }
        single("subsystems") {
            opmode.robot.compositeout.setOuttake(CompositeOuttake.OuttakeState.Outtake)
            Thread.sleep(4000)
        }
    }
    single("Drop Sample"){
        opmode.robot.outtake.setOuttakeGrip(Outtake.ClawState.Open)
        Thread.sleep(1500)

    }
})