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
                            new Point(19.000, 12.000, Point.CARTESIAN),
                            //new Point(30.500, 5.000, Point.CARTESIAN)
                            new Point(26.000, 5.000, Point.CARTESIAN)
                    )
            ).setLinearHeadingInterpolation(Math.toRadians(270),Math.toRadians(320));
    return builder.build();
  }
}
