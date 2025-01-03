package org.riverdell.robotics.PedroAuto.SlowBucket.leftSample;

import org.riverdell.robotics.pedroPathing.pathGeneration.BezierLine;
import org.riverdell.robotics.pedroPathing.pathGeneration.PathBuilder;
import org.riverdell.robotics.pedroPathing.pathGeneration.PathChain;
import org.riverdell.robotics.pedroPathing.pathGeneration.Point;

public class toLeftSample {

  public static PathChain path() {
    PathBuilder builder = new PathBuilder();

    builder
            .addPath(
                    // Line 1
                    new BezierLine(
                            new Point(14.200, -9.300, Point.CARTESIAN),
                            new Point(16.500, -16.800, Point.CARTESIAN)
                    )
            ).setLinearHeadingInterpolation(Math.toRadians(225),Math.toRadians(300));
    return builder.build();
  }
}
