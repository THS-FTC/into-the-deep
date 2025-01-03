package org.riverdell.robotics.PedroAuto.SlowBucket;

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
                            new Point(0.000, -60.000, Point.CARTESIAN),
                            new Point(-10.000, -60.000, Point.CARTESIAN)
                    )
            )
            .setLinearHeadingInterpolation(Math.toRadians(350), Math.toRadians(350));//idk second angle, gotta change
    return builder.build();
  }
}
//TODO: Change second angle for the ascent park
