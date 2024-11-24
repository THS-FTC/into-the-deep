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
          new Point(0.000, 0.000, Point.CARTESIAN), //8 to 100
          new Point(20.000, 20.000, Point.CARTESIAN)//20 to 123
        )
      )
      .setLinearHeadingInterpolation(Math.toRadians(90), Math.toRadians(135))
      .addPath(
        // Line 2
        new BezierLine(
          new Point(20.000, 20.000, Point.CARTESIAN), //20 to 123
          new Point(0.000, 0.000, Point.CARTESIAN) //17 to 126
        )
      )
      .setTangentHeadingInterpolation();
    return builder.build();
  }
}
