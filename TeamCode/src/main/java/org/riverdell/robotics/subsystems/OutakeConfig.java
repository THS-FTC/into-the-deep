package org.riverdell.robotics.subsystems;

import com.acmerobotics.dashboard.config.Config;

@Config
public class OutakeConfig {
    public static double openPosition= 0.5;
    public static double closePositon  = 0.74;

    //rotation
    public static double frontPosition = 1.0;
    public static double specimenPosition = 0.0;

    // pulley
    public static double pulleyTransferPosition= 0.0;
    public static double  pulleyBucketPosition = 0.5;
    public static double  pulleySpecimenPosition = 0.0;
}
