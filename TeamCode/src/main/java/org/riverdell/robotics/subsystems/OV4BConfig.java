package org.riverdell.robotics.subsystems;

import com.acmerobotics.dashboard.config.Config;

@Config
public class OV4BConfig {

    //v4b
    public static double transferPosition = 1.0; //0.2
    public static double OuttakePosition= 0.6;
    public static double SpecimenPosition= 0.5;
    public static double IdlePosition= 1.0;
    public static boolean leftIsReverse = false;

    // pulley
    public static double  idlePulley = 0.3;
    public static double  bucketPulley = 0.7;
    public static double  specimenPulley = 0.7;


}
