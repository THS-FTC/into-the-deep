package org.robotics.robotics.xdk.teamcode.autonomous

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import io.liftgate.robotics.mono.pipeline.waitMillis
import org.robotics.robotics.xdk.teamcode.Global
import org.robotics.robotics.xdk.teamcode.autonomous.profiles.AutonomousProfile

@Autonomous(
    name = "Red | Far | 2+0F",
    group = "RedFast",
    preselectTeleOp = Global.RobotCentricTeleOpName
)
class AutoPipelineRedPlayer2 : AbstractAutoPipeline(AutonomousProfile.RedPlayer2TwoPlusZero)

@Autonomous(
    name = "Red | Far | 2+0S",
    group = "RedSlow",
    preselectTeleOp = Global.RobotCentricTeleOpName
)
class AutoPipelineRedPlayer2SlowMode : AbstractAutoPipeline(
    AutonomousProfile.RedPlayer2TwoPlusZero,
    blockExecutionGroup = { k, v ->
        waitMillis(3000L)
        AutonomousProfile.RedPlayer2TwoPlusZero
            .buildExecutionGroup()(k, v)
    }
)

@Autonomous(
    name = "Red | Close | 2+0F",
    group = "RedFast",
    preselectTeleOp = Global.RobotCentricTeleOpName
)
class AutoPipelineRedPlayer1 : AbstractAutoPipeline(AutonomousProfile.RedPlayer1TwoPlusZero)

@Autonomous(
    name = "Blue | Far | 2+0F",
    group = "BlueFast",
    preselectTeleOp = Global.RobotCentricTeleOpName
)
class AutoPipelineBluePlayer2 : AbstractAutoPipeline(AutonomousProfile.BluePlayer2TwoPlusZero)

@Autonomous(
    name = "Blue | Far | 2+0S",
    group = "BlueSlow",
    preselectTeleOp = Global.RobotCentricTeleOpName
)
class AutoPipelineBluePlayer2SlowMode : AbstractAutoPipeline(
    AutonomousProfile.BluePlayer2TwoPlusZero,
    blockExecutionGroup = { k, v ->
        waitMillis(3000L)
        AutonomousProfile.BluePlayer2TwoPlusZero
            .buildExecutionGroup()(k, v)
    }
)

@Autonomous(
    name = "Blue | Close | 2+0F",
    group = "BlueFast",
    preselectTeleOp = Global.RobotCentricTeleOpName
)
class AutoPipelineBluePlayer1 : AbstractAutoPipeline(AutonomousProfile.BluePlayer1TwoPlusZero)