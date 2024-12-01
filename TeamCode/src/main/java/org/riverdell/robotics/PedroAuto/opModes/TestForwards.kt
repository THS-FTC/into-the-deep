package org.riverdell.robotics.autonomous.impl.tests

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import io.liftgate.robotics.mono.pipeline.single
import org.riverdell.robotics.PedroAuto.Paths.Paths
import org.riverdell.robotics.autonomous.HypnoticAuto

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