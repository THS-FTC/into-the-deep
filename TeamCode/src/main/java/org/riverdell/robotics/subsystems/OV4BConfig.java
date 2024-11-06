package org.riverdell.robotics.subsystems;

import com.acmerobotics.dashboard.config.Config;

@Config
public class OV4BConfig {

    //v4b
    public static double transferPosition = 0.95; //0.2
    public static double OuttakePosition= 0.3;
    public static double IdlePosition= 1.0;

    // pulley
    public static boolean leftIsReverse = false;
    public static double  idlePulley = 0.3;
    public static double  backPulley = 0.3;

}
