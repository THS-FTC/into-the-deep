package org.riverdell.robotics.autonomous.PathFolder.PathChains;

import com.pedropathing.pathgen.BezierCurve;
import com.pedropathing.pathgen.BezierLine;
import com.pedropathing.pathgen.PathBuilder;
import com.pedropathing.pathgen.PathChain;
import com.pedropathing.pathgen.Point;

public class fiveSpecPaths {

    public static double wallX = 8.000;
    public static PathBuilder builder = new PathBuilder();

    public static PathChain TB_FP = builder
            .addPath(
                    // Line 1
                    new BezierLine(
                            new Point(wallX, 71.500, Point.CARTESIAN),
                            new Point(45.500, 71.500, Point.CARTESIAN)
                    )
            )
            .setTangentHeadingInterpolation()
            .build();
    public static PathChain TF_FB = builder
            .addPath(
                    // Line 1
                    new BezierCurve(
                            new Point(45.500, 71.500, Point.CARTESIAN),
                            new Point(24.500, 51.500, Point.CARTESIAN),
                            new Point(34.500, 39.500, Point.CARTESIAN),
                            new Point(60.000, 30.000, Point.CARTESIAN),
                            new Point(59.000, 23.000, Point.CARTESIAN)
                    )
            )
            .setConstantHeadingInterpolation(Math.toRadians(0))
            .addPath(
                    // Line 1
                    new BezierLine(
                            new Point(59.000, 23.000, Point.CARTESIAN),
                            new Point(15.000, 23.000, Point.CARTESIAN)
                    )
            )
            .setConstantHeadingInterpolation(Math.toRadians(0))
            .build();
    public static PathChain FF_TS = builder
            .addPath(
                    // Line 1
                    new BezierCurve(
                            new Point(15.000, 23.000, Point.CARTESIAN),
                            new Point(42.000, 21.500, Point.CARTESIAN),
                            new Point(53.000, 33.000, Point.CARTESIAN),
                            new Point(59.000, 12.300, Point.CARTESIAN)
                    )
            )
            .setConstantHeadingInterpolation(Math.toRadians(0))
            .addPath(
                    // Line 1
                    new BezierLine(
                            new Point(59.000, 12.300, Point.CARTESIAN),
                            new Point(15.000, 12.300, Point.CARTESIAN)
                    )
            )
            .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(0))
            .build();
    public static PathChain FS_TT = builder
            .addPath(
                    // Line 1
                    new BezierCurve(
                            new Point(15.000, 12.300, Point.CARTESIAN),
                            new Point(29.500, 8.000, Point.CARTESIAN),
                            new Point(55.000, 20.000, Point.CARTESIAN),
                            new Point(59.000, 6.500, Point.CARTESIAN)
                    )
            )
            .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(0))
            .addPath(
                    // Line 1
                    new BezierLine(
                            new Point(59.000, 6.500, Point.CARTESIAN),
                            new Point(12.000, 6.500, Point.CARTESIAN)
                    )
            )
            .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(0))
            .build();
    public static PathChain FT_TSPEC2 = builder
            .addPath(
                    // Line 1
                    new BezierCurve(
                            new Point(12.000, 6.500, Point.CARTESIAN),
                            new Point(11.500, 22.000, Point.CARTESIAN),
                            new Point(25.800, 36.800, Point.CARTESIAN),
                            new Point(8.000, 35.000, Point.CARTESIAN)
                    )
            )
            .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(0))
            .build();
    public static PathChain FSPEC_TB = builder
            .addPath(
                    // Line 1
                    new BezierCurve(
                            new Point(8.000, 35.000, Point.CARTESIAN),
                            new Point(27.800, 73.000, Point.CARTESIAN),
                            new Point(33.500, 70.700, Point.CARTESIAN),
                            new Point(45.500, 71.500, Point.CARTESIAN)
                    )
            )
            .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(0))
            .build();

    public static PathChain FB_TSPEC = builder
            .addPath(
                    // Line 1
                    new BezierCurve(
                            new Point(45.500, 71.500, Point.CARTESIAN),
                            new Point(32.300, 55.000, Point.CARTESIAN),
                            new Point(39.000, 34.000, Point.CARTESIAN),
                            new Point(8.000, 35.000, Point.CARTESIAN)
                    )
            )
            .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(0))
            .build();
    public static PathChain FB_TP = builder
            .addPath(
                    // Line 1
                    new BezierLine(
                            new Point(45.500, 71.500, Point.CARTESIAN),
                            new Point(30.000, 71.500, Point.CARTESIAN)
                    )
            )
            .setTangentHeadingInterpolation()
            .build();
}
