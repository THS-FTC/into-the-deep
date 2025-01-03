package org.riverdell.robotics.PedroAuto.Paths.middleSample;

import org.riverdell.robotics.pedroPathing.pathGeneration.BezierLine;
import org.riverdell.robotics.pedroPathing.pathGeneration.PathBuilder;
import org.riverdell.robotics.pedroPathing.pathGeneration.PathChain;
import org.riverdell.robotics.pedroPathing.pathGeneration.Point;

public class toBasketFromMiddle {

  public static PathChain path() {
    PathBuilder builder = new PathBuilder();

    builder
            .addPath(
                    // Line 1
                    new BezierLine(
                            new Point(16.100, -13.000, Point.CARTESIAN),
                            new Point(15.900, -10.700, Point.CARTESIAN)
                    )
            )
            .setLinearHeadingInterpolation(Math.toRadians(260), Math.toRadians(225));
    return builder.build();
  }
}
