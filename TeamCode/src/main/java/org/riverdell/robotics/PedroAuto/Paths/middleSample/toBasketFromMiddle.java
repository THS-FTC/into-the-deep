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
                            new Point(26.000, 15.500, Point.CARTESIAN),
                            new Point(19.000, 12.000, Point.CARTESIAN)
                    )
            )
            .setLinearHeadingInterpolation(Math.toRadians(320), Math.toRadians(270));
    return builder.build();
  }
}
