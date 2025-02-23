package org.riverdell.robotics.subsystems;

import com.acmerobotics.dashboard.config.Config;

@Config
public class IntakeConfig {
    public static double closePosition= 0.48;
    public static double openPositon  = 0.2;
    public static double resetPosition  = 0.0;

    //rotation
    public static double transferPosition = 0.48;
    public static double observePosition= 0.94;
    public static double grabPosition= 0.88;

    // wrist
    public static double frontPosition= 0.6;
    public static double  veritcalPosition = 0.32;
    public static double  otherPosition = 0.6;
    public static double  leftPosition = 0.47;
    public static double  rightPosition = 0.2;


}
