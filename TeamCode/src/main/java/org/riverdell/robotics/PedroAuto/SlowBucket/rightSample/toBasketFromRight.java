package org.riverdell.robotics.PedroAuto.SlowBucket.rightSample;

import org.riverdell.robotics.pedroPathing.pathGeneration.BezierLine;
import org.riverdell.robotics.pedroPathing.pathGeneration.PathBuilder;
import org.riverdell.robotics.pedroPathing.pathGeneration.PathChain;
import org.riverdell.robotics.pedroPathing.pathGeneration.Point;

public class toBasketFromRight {

  public static PathChain path() {
    PathBuilder builder = new PathBuilder();

    builder
            .addPath(
                    // Line 1
                    new BezierLine(
                            new Point(12.200, -10.000, Point.CARTESIAN),
                            new Point(14.500, -8.400, Point.CARTESIAN)
                    )
            )
            .setLinearHeadingInterpolation(Math.toRadians(225), Math.toRadians(225));
    return builder.build();
  }
}
