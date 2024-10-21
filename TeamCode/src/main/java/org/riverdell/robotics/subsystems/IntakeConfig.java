package org.riverdell.robotics.subsystems;

import com.acmerobotics.dashboard.config.Config;

@Config
public class IntakeConfig {
    public static double openPosition= 0.4;
    public static double closePositon  = 1.0;

    //rotation
    public static double transferPosition = 1.0;
    public static double observePosition= 0.2;
    public static double grabPosition= 0.23;

    // wrist
    public static double frontPosition= 0.0;
    public static double  backPosition = 1.0;
}
