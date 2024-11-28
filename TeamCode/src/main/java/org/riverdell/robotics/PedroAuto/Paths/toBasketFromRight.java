package org.riverdell.robotics.PedroAuto.Paths;

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
                            new Point(32.770, 121.155, Point.CARTESIAN),
                            new Point(17.400, 127.147, Point.CARTESIAN)
                    )
            )
            .setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(158));
    return builder.build();
  }
}
