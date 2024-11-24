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

@Autonomous(name = "Go Forwards", group = "Comp")
class TestForwards : HypnoticAuto({ opmode ->
    single("Go Forward"){
        opmode.robot.follower.followPath(Paths.goForward)
        while (!opmode.robot.follower.atParametricEnd())
        {
//            if (!opmode.opModeIsActive()){
//                break;
//            }
            Thread.sleep(50L)
        }
    }
})