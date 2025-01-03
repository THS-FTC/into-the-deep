package org.riverdell.robotics.PedroAuto.SlowBucket.middleSample;

import org.riverdell.robotics.pedroPathing.pathGeneration.BezierLine;
import org.riverdell.robotics.pedroPathing.pathGeneration.PathBuilder;
import org.riverdell.robotics.pedroPathing.pathGeneration.PathChain;
import org.riverdell.robotics.pedroPathing.pathGeneration.Point;

public class toMiddleSample {

  public static PathChain path() {
    PathBuilder builder = new PathBuilder();

    builder
            .addPath(
                    // Line 1
                    new BezierLine(
                            new Point(14.150, -9.000, Point.CARTESIAN),
                            new Point(18.920, -13.200, Point.CARTESIAN)
                    )
            ).setLinearHeadingInterpolation(Math.toRadians(225),Math.toRadians(255));
    return builder.build();
  }
}
