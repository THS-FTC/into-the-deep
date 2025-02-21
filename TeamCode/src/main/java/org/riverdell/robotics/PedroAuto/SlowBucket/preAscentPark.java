package org.riverdell.robotics.PedroAuto.SlowBucket;

import org.riverdell.robotics.pedroPathing.pathGeneration.BezierCurve;
import org.riverdell.robotics.pedroPathing.pathGeneration.PathBuilder;
import org.riverdell.robotics.pedroPathing.pathGeneration.PathChain;
import org.riverdell.robotics.pedroPathing.pathGeneration.Point;

public class preAscentPark {

  public static PathChain path() {
    PathBuilder builder = new PathBuilder();

    builder
            .addPath(
                    // Line 1
                    new BezierCurve(
                           // new Point(15.900, 10.700, Point.CARTESIAN),
                            new Point(13.900, -9.200, Point.CARTESIAN),
                            new Point(25.000, -20.000, Point.CARTESIAN),
                            new Point(0.000, -60.000, Point.CARTESIAN)
                    )
            )
            .setLinearHeadingInterpolation(Math.toRadians(225), Math.toRadians(350));//idk second angle, gotta change
    return builder.build();
  }
}
//TODO: Change second angle for the ascent park
