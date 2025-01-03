package org.riverdell.robotics.PedroAuto.Paths.initialAndRightSamples;

import org.riverdell.robotics.pedroPathing.pathGeneration.BezierLine;
import org.riverdell.robotics.pedroPathing.pathGeneration.PathBuilder;
import org.riverdell.robotics.pedroPathing.pathGeneration.PathChain;
import org.riverdell.robotics.pedroPathing.pathGeneration.Point;

public class toRightSample {

  public static PathChain path() {
    PathBuilder builder = new PathBuilder();

    builder
            .addPath(
                    // Line 1
                    new BezierLine(
                            new Point(13.800, -8.000, Point.CARTESIAN),
                            //new Point(30.500, 5.000, Point.CARTESIAN)
                            new Point(10.900, -12.950, Point.CARTESIAN)
                    )
            ).setLinearHeadingInterpolation(Math.toRadians(225),Math.toRadians(260));
    return builder.build();
  }
}
