package org.riverdell.robotics.autonomous.impl.tests

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import org.riverdell.robotics.autonomous.AutonomousWrapper
import org.riverdell.robotics.autonomous.movement.geometry.Pose
import org.riverdell.robotics.autonomous.movement.cubicBezier
import org.riverdell.robotics.autonomous.movement.navigateUnstableGVF

@Autonomous(name = "Test | GVF", group = "Test")
class TestGVF : AutonomousWrapper({ _ ->
    navigateUnstableGVF(cubicBezier(
        Pose(-20.0, 0.0, 0.0),
        Pose(-10.0, 20.0, 0.0),
        Pose(0.0, 0.0, 0.0),
        Pose(20.0, -20.0, 0.0)
    ))
})