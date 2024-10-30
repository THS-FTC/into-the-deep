package org.riverdell.robotics.autonomous.movement.localization;

import androidx.annotation.NonNull;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.localization.TwoTrackingWheelLocalizer;
import com.arcrobotics.ftclib.hardware.motors.Motor;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.riverdell.robotics.HypnoticRobot;
import org.riverdell.robotics.autonomous.movement.geometry.Pose;

import java.util.Arrays;
import java.util.List;

/*
 * Sample tracking wheel localizer implementation assuming the standard configuration:
 *
 *    ^
 *    |
 *    | ( x direction)
 *    |
 *    v
 *    <----( y direction )---->

 *        (forward)
 *    /--------------\
 *    |     ____     |
 *    |     ----     |    <- Perpendicular Wheel
 *    |           || |
 *    |           || |    <- Parallel Wheel
 *    |              |
 *    |              |
 *    \--------------/
 *
 */
public class TwoWheelLocalizer extends TwoTrackingWheelLocalizer {

    private final HypnoticRobot hypnoticRobot;

    public TwoWheelLocalizer(HypnoticRobot hypnoticRobot) {
        super(Arrays.asList(
                new Pose2d(0, 0, 0), // left + right
                new Pose2d(0, 0, Math.toRadians(90)) // front
        ));

        this.hypnoticRobot = hypnoticRobot;
    }

    @NonNull
    @Override
    public List<Double> getWheelPositions() {
        hypnoticRobot.getHardware().getPinpointDriver().update();

        Pose2D pose = hypnoticRobot.getHardware().getPinpointDriver().getPosition();
        return Arrays.asList(
                pose.getX(DistanceUnit.INCH),
                pose.getY(DistanceUnit.INCH)
        );
    }

    @Override
    public double getHeading() {
        return hypnoticRobot.getDrivetrain().imu();
    }

    @NonNull
    @Override
    public List<Double> getWheelVelocities() {
        // TODO: If your encoder velocity can exceed 32767 counts / second (such as the REV Through Bore and other
        //  competing magnetic encoders), change Encoder.getRawVelocity() to Encoder.getCorrectedVelocity() to enable a
        //  compensation method

        return Arrays.asList(0.0, 0.0);
    }

    public Pose getPose() {
        Pose2d pose = getPoseEstimate();
        return new Pose(-pose.getY(), pose.getX(), pose.getHeading());
    }
}