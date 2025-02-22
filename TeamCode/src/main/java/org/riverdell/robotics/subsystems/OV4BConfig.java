package org.riverdell.robotics.subsystems;

import com.acmerobotics.dashboard.config.Config;

@Config
public class OV4BConfig {

    //v4b
    public static double transferPosition = 0.92; //0.2
    public static double OuttakePosition= 0.6;


    //TODO: Specimen Positions To change
    public static double SpecimenPosition= 0.45;
    public static double SpecimenScorePosition= 0.45;
    public static double SpecimenIntakePosition= 0.85;



    public static double awayPosition = 0.60;

    public static double IdlePosition= 0.92;
    public static boolean leftIsReverse = false;

    // pulley
    public static double  idlePulley = 0.7;
    public static double  bucketPulley = 0.7;

    //TODO: More Specimen Positions to Change
    public static double  specimenPulley = 0.7;
    public static double  specimenscorePulley = 0.7;
    public static double  specimenIntakePulley = 0.7;




}
