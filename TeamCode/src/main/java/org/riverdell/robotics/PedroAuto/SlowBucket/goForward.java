package org.riverdell.robotics.PedroAuto.SlowBucket;

import org.riverdell.robotics.pedroPathing.pathGeneration.BezierLine;
import org.riverdell.robotics.pedroPathing.pathGeneration.PathBuilder;
import org.riverdell.robotics.pedroPathing.pathGeneration.PathChain;
import org.riverdell.robotics.pedroPathing.pathGeneration.Point;

public class goForward {

  public static PathChain path() {
    PathBuilder builder = new PathBuilder();

    builder
      .addPath(
            // Line 1
            new BezierLine(
                    new Point(0.0, 0.0, Point.CARTESIAN),
                    new Point(0.0, 10.0, Point.CARTESIAN)
            )
      );
    return builder.build();
  }
}
