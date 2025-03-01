package org.riverdell.robotics.autonomous.PathFolder.PathChains;

import com.pedropathing.pathgen.BezierCurve;
import com.pedropathing.pathgen.BezierLine;
import com.pedropathing.pathgen.PathBuilder;
import com.pedropathing.pathgen.PathChain;
import com.pedropathing.pathgen.Point;

public class TwosamplePaths {

    public static double wallX = 8.000;
    public static PathBuilder builder = new PathBuilder();

    public static PathChain TB_FP = builder
            .addPath(
                    // Line 1
                    new BezierLine(
                            new Point(wallX, 71.500-17.5, Point.CARTESIAN),
                            new Point(27.000, 71.500-17.5, Point.CARTESIAN)
                    )
            )
            .setConstantHeadingInterpolation(Math.toRadians(180))
            .build();
    public static PathChain TF_FB = builder
            .addPath(
                    // Line 1
                    new BezierCurve(
                            new Point(45.500-17.5, 71.500-17.5, Point.CARTESIAN),
                            new Point(24.500-17.5, 51.500-17.5, Point.CARTESIAN),
                            new Point(34.500-17.5, 39.500-17.5, Point.CARTESIAN),
                            new Point(60.000-17.5, 30.000-17.5, Point.CARTESIAN),
                            new Point(59.000-17.5, 23.000-17.5, Point.CARTESIAN)
                    )
            )
            .setConstantHeadingInterpolation(Math.toRadians(180))
            .addPath(
                    // Line 1
                    new BezierLine(
                            new Point(59.000-17.5, 23.000-17.5, Point.CARTESIAN),
                            new Point(15.000-17.5, 23.000-17.5, Point.CARTESIAN)
                    )
            )
            .setConstantHeadingInterpolation(Math.toRadians(180))
            .build();
    public static PathChain FF_TS = builder
            .addPath(
                    // Line 1
                    new BezierCurve(
                            new Point(15.000-17.5, 23.000-17.5, Point.CARTESIAN),
                            new Point(42.000-17.5, 21.500-17.5, Point.CARTESIAN),
                            new Point(53.000-17.5, 33.000-17.5, Point.CARTESIAN),
                            new Point(59.000-17.5, 12.300-17.5, Point.CARTESIAN)
                    )
            )
            .setConstantHeadingInterpolation(Math.toRadians(180))
            .addPath(
                    // Line 1
                    new BezierLine(
                            new Point(59.000-17.5, 12.300-17.5, Point.CARTESIAN),
                            new Point(15.000-17.5, 12.300-17.5, Point.CARTESIAN)
                    )
            )
            .setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180))
            .build();
    public static PathChain FS_TT = builder
            .addPath(
                    // Line 1
                    new BezierCurve(
                            new Point(15.000-17.5, 12.300-17.5, Point.CARTESIAN),
                            new Point(29.500-17.5, 8.000-17.5, Point.CARTESIAN),
                            new Point(55.000-17.5, 20.000-17.5, Point.CARTESIAN),
                            new Point(59.000-17.5, 6.500-17.5, Point.CARTESIAN)
                    )
            )
            .setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180))
            .addPath(
                    // Line 1
                    new BezierLine(
                            new Point(59.000-17.5, 6.500-17.5, Point.CARTESIAN),
                            new Point(12.000-17.5, 6.500-17.5, Point.CARTESIAN)
                    )
            )
            .setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180))
            .build();
    public static PathChain FT_TSPEC2 = builder
            .addPath(
                    // Line 1
                    new BezierCurve(
                            new Point(12.000-17.5, 6.500-17.5, Point.CARTESIAN),
                            new Point(11.500-17.5, 22.000-17.5, Point.CARTESIAN),
                            new Point(25.800-17.5, 36.800-17.5, Point.CARTESIAN),
                            new Point(8.000-17.5, 35.000-17.5, Point.CARTESIAN)
                    )
            )
            .setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180))
            .build();
    public static PathChain FSPEC_TB = builder
            .addPath(
                    // Line 1
                    new BezierCurve(
                            new Point(8.000-17.5, 35.000-17.5, Point.CARTESIAN),
                            new Point(27.800-17.5, 73.000-17.5, Point.CARTESIAN),
                            new Point(33.500-17.5, 70.700-17.5, Point.CARTESIAN),
                            new Point(45.500-17.5, 71.500-17.5, Point.CARTESIAN)
                    )
            )
            .setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180))
            .build();

    public static PathChain FB_TSPEC = builder
            .addPath(
                    // Line 1
                    new BezierCurve(
                            new Point(45.500-17.5, 71.500-17.5, Point.CARTESIAN),
                            new Point(32.300-17.5, 55.000-17.5, Point.CARTESIAN),
                            new Point(39.000-17.5, 34.000-17.5, Point.CARTESIAN),
                            new Point(8.000-17.5, 35.000-17.5, Point.CARTESIAN)
                    )
            )
            .setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180))
            .build();
    public static PathChain FB_TP = builder
            .addPath(
                    // Line 1
                    new BezierLine(
                            new Point(45.500-17.5, 71.500-17.5, Point.CARTESIAN),
                            new Point(30.000-17.5, 71.500-17.5, Point.CARTESIAN)
                    )
            )
            .setConstantHeadingInterpolation(Math.toRadians(180))
            .build();
}
