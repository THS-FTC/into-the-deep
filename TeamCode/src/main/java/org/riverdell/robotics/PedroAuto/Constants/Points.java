package org.riverdell.robotics.PedroAuto.Constants;

import org.riverdell.robotics.pedroPathing.pathGeneration.Point;
import org.riverdell.robotics.pedroPathing.localization.Pose;

public final class Points {
//    public static final Point basketOuttake = new Point(19.000, 14.000 Point.CARTESIAN);
//    public static final Point basketOuttakeCloser = new Point(17.150, 126.800, Point.CARTESIAN);
    public static final Point slantStart = new Point(0.000, 0.000, Point.CARTESIAN);
    //public static final Point slantStart = new Point(0.0, 0.0, Point.CARTESIAN);
//    public static final Point humanAreaPark = new Point(10.0, 11.0, Point.CARTESIAN);

    public static final Pose slantStartPose = new Pose(slantStart.getX(), slantStart.getY(),Math.toRadians(225));
//    public static final Pose basketTileFrontStartPose = new Pose(9.0, 87.0, Math.PI/2);
}
