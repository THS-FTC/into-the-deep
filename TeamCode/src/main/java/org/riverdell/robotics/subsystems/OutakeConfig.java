package org.riverdell.robotics.subsystems;

import com.acmerobotics.dashboard.config.Config;

@Config
public class OutakeConfig {
    public static double openPosition= 0.5;
    public static double closePositon  = 0.78;

    //rotation
    public static double transferPosition = 1.0;
    public static double outtakePosition = 0.0;

    // wrist
    public static double frontPosition= 0.0;
    public static double  backPosition = 0.5;
}
