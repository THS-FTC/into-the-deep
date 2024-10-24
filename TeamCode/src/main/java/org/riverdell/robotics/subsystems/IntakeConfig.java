package org.riverdell.robotics.subsystems;

import com.acmerobotics.dashboard.config.Config;

@Config
public class IntakeConfig {
    public static double openPosition= 0.4;
    public static double closePositon  = 0.85;

    //rotation
    public static double transferPosition = 0.5;
    public static double observePosition= 0.3;
    public static double grabPosition= 0.2;

    // wrist
    public static double frontPosition= 0.0;
    public static double  backPosition = 1.0;
}
