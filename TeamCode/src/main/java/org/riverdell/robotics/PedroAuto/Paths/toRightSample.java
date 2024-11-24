package org.riverdell.robotics.PedroAuto.Paths;

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
                    new Point(17.000, 126.000, Point.CARTESIAN),
                    new Point(20.000, 122.000, Point.CARTESIAN)
            )
      ).setLinearHeadingInterpolation(Math.toRadians(135), Math.toRadians(180));
    return builder.build();
  }
}
