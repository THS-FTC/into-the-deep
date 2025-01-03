package org.riverdell.robotics.PedroAuto.PathsBucketFast.initialAndRightSamples;

import org.riverdell.robotics.pedroPathing.pathGeneration.BezierLine;
import org.riverdell.robotics.pedroPathing.pathGeneration.PathBuilder;
import org.riverdell.robotics.pedroPathing.pathGeneration.PathChain;
import org.riverdell.robotics.pedroPathing.pathGeneration.Point;

public class toBasketFromSlant {

  public static PathChain path() {
    PathBuilder builder = new PathBuilder();

    builder
//            .addPath(
//                    new BezierCurve(
//                            new Point(0.000, 0.000, Point.CARTESIAN),
//                            new Point(20.00, 5.00, Point.CARTESIAN),
//                            new Point(18.00, 14.00, Point.CARTESIAN)
//                    )
            .addPath(
                    // Line 1
                    new BezierLine(
                            new Point(0.000, 0.000, Point.CARTESIAN),
                            new Point(14.230, -8.230, Point.CARTESIAN)
                    )
            ).setLinearHeadingInterpolation(Math.toRadians(225), Math.toRadians(225));
//            .addPath(
//                    // Line 2
//                    new BezierLine(
//                            new Point(23.000, 8.000, Point.CARTESIAN),
//                            new Point(19.000, 10.00, Point.CARTESIAN)
//                    )
//            ).setLinearHeadingInterpolation(Math.toRadians(225), Math.toRadians(225)); //270 to 270
    return builder.build();
  }
}
