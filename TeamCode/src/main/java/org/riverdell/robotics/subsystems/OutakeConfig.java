package org.riverdell.robotics.subsystems;

import com.acmerobotics.dashboard.config.Config;

@Config
public class OutakeConfig {
    public static double openPosition= 0.4;
    public static double closePositon  = 0.9;

    //rotation
    public static double frontPosition = 1.0;

    //TODO: Change both values for the wrist, above and below
    public static double specimenPosition = 0.45;

    // pulley
    public static double pulleyTransferPosition= 0.0;
    public static double  pulleyBucketPosition = 0.5;
    public static double  pulleySpecimenPosition = 0.0;
}
