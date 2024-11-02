package org.riverdell.robotics.subsystems;

import com.acmerobotics.dashboard.config.Config;

@Config
public class IntakeConfig {
    public static double closePosition= 0.45;
    public static double openPositon  = 1.0;

    //rotation
    public static double transferPosition = 0.55;
    public static double observePosition= 0.9;
    public static double grabPosition= 0.95;

    // wrist
    public static double frontPosition= 0.0;
    public static double  backPosition = 0.0;
    public static double  veritcalPosition = 0.3;
    public static double  leftPosition = 0.2;
    public static double  rightPosition = 0.5;


}
