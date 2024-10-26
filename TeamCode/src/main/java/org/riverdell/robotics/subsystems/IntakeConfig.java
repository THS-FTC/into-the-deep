package org.riverdell.robotics.subsystems;

import com.acmerobotics.dashboard.config.Config;

@Config
public class IntakeConfig {
    public static double closePosition= 0.45;
    public static double openPositon  = 1.0;

    //rotation
    public static double transferPosition = 0.65;
    public static double observePosition= 0.3;
    public static double grabPosition= 0.2;

    // wrist
    public static double frontPosition= 0.0;
    public static double  backPosition = 0.0;
}
