package org.riverdell.robotics.PedroAuto.Paths;

import org.riverdell.robotics.pedroPathing.pathGeneration.BezierLine;
import org.riverdell.robotics.pedroPathing.pathGeneration.PathBuilder;
import org.riverdell.robotics.pedroPathing.pathGeneration.PathChain;
import org.riverdell.robotics.pedroPathing.pathGeneration.Point;

public class toBasketFromSlant {

  public static PathChain path() {
    PathBuilder builder = new PathBuilder();

    builder
      .addPath(
        // Line 1
        new BezierLine(
          new Point(8.000, 100.000, Point.CARTESIAN),
          new Point(20.000, 123.000, Point.CARTESIAN)
        )
      )
      .setLinearHeadingInterpolation(Math.toRadians(90), Math.toRadians(135))
      .addPath(
        // Line 2
        new BezierLine(
          new Point(20.000, 123.000, Point.CARTESIAN),
          new Point(17.000, 126.000, Point.CARTESIAN)
        )
      )
      .setTangentHeadingInterpolation();
    return builder.build();
  }
}
