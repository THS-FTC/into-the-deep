package org.riverdell.robotics.teleop.tests;

import com.acmerobotics.dashboard.config.Config;

@Config
public class ServoConfig {
    public static double IP = 0.0;
    public static String intakePulley = "intake_pulley";

    public static double IL_changer = 0.0;
    public static String IV4BL = "iv4b_rotation_left";

    public static double IR = 1.0;
    public static String IV4BR = "iv4b_rotation_right";

    public static double OP = 0.0;
    public static String outtakePulley = "ov4b_pulley";
    public static double IW = 0.0;
    public static String intakeWrist = "intake_wrist";
    public static double OL_changer = 0.0;
    public static String OV4BL = "ov4b_rotation_left";
    public static double OR = 1.0;
    public static String OV4BR = "ov4b_rotation_right";
}
