package org.riverdell.robotics.PedroAuto.Paths;

import org.riverdell.robotics.pedroPathing.pathGeneration.BezierCurve;
import org.riverdell.robotics.pedroPathing.pathGeneration.PathBuilder;
import org.riverdell.robotics.pedroPathing.pathGeneration.PathChain;
import org.riverdell.robotics.pedroPathing.pathGeneration.Point;

public class ascentPark {

  public static PathChain path() {
    PathBuilder builder = new PathBuilder();

    builder
            .addPath(
                    // Line 1
                    new BezierCurve(
                            new Point(19.000, 12.000, Point.CARTESIAN),
                            new Point(60.000, 9.000, Point.CARTESIAN),
                            new Point(69.000, -17.50, Point.CARTESIAN)
                    )
            )
            .setLinearHeadingInterpolation(Math.toRadians(270), Math.toRadians(90));//idk second angle, gotta change
    return builder.build();
  }
}
//TODO: Change second angle for the ascent park
