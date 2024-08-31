package org.robotics.robotics.xdk.teamcode.autonomous.position;

import org.robotics.robotics.xdk.teamcode.autonomous.purepursuit.PathAlgorithm;
import org.robotics.robotics.xdk.teamcode.autonomous.purepursuit.PurePursuitPath;

import io.liftgate.robotics.mono.pipeline.RootExecutionGroup;

public class PurePursuitCommand extends PositionCommand {
    public PurePursuitCommand(final RootExecutionGroup executionGroup,
                              final PurePursuitPath purePursuitPath) {
        super(null, executionGroup);
        withCustomPathAlgorithm(new PathAlgorithm(
                purePursuitPath::calculateTargetPose,
                (robotPose, targetPose) -> purePursuitPath.isFinished()
        ));
    }
}
