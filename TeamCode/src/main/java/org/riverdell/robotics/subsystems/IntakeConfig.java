package org.riverdell.robotics.subsystems;

import com.acmerobotics.dashboard.config.Config;

@Config
public class IntakeConfig {
    public static double closePosition= 0.425;
    public static double openPositon  = 0.2;
    public static double resetPosition  = 0.0;

    //rotation
    public static double transferPosition = 0.65;
    public static double observePosition= 0.94;
    public static double grabPosition= 0.91;

    // wrist
    public static double frontPosition= 0.05;
    public static double  backPosition = 0.05;
    public static double  veritcalPosition = 0.3;
    public static double  leftPosition = 0.45;
    public static double  rightPosition = 0.2;


}
