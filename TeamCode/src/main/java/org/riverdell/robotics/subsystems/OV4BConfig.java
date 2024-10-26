package org.riverdell.robotics.subsystems;

import com.acmerobotics.dashboard.config.Config;

@Config
public class OV4BConfig {

    //rotation
    public static double transferPosition = 0.0; //0.2
    public static double OuttakePosition= 0.9;
    public static double IdlePosition= 0.0;

    // wrist
    public static boolean leftIsReverse = false;
    public static double  idlePulley = 0.45;
    public static double  backPulley = 0.4;

}
