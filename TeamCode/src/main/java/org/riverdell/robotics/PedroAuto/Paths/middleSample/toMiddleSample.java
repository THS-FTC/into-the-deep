package org.riverdell.robotics.PedroAuto.Paths.middleSample;

import org.riverdell.robotics.pedroPathing.pathGeneration.BezierLine;
import org.riverdell.robotics.pedroPathing.pathGeneration.PathBuilder;
import org.riverdell.robotics.pedroPathing.pathGeneration.PathChain;
import org.riverdell.robotics.pedroPathing.pathGeneration.Point;

public class toMiddleSample {

  public static PathChain path() {
    PathBuilder builder = new PathBuilder();

    builder
            .addPath(
                    // Line 1
                    new BezierLine(
                            new Point(14.000, -10.700, Point.CARTESIAN),
                            new Point(16.150, -13.100, Point.CARTESIAN)
                    )
            ).setLinearHeadingInterpolation(Math.toRadians(225),Math.toRadians(260));
    return builder.build();
  }
}
