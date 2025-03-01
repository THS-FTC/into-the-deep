package org.riverdell.robotics.subsystems;

import com.acmerobotics.dashboard.config.Config;

@Config
public class OV4BConfig {

    //v4b
    public static double transferPosition = 0.95; //ok
    public static double OuttakePosition= 0.9;


    //TODO: Specimen Positions To change
    public static double SpecimenScorePosition= 0.05;
    public static double SpecimenScoredPosition= 0.05;
    public static double SpecimenIntakePosition= 1.0;



    public static double awayPosition = 0.7;

    public static double IdlePosition= 0.0;
    public static boolean leftIsReverse = true;

    // pulley
    public static double  idlePulley = 0.7;
    public static double  transferPulley = 0.7;// change this
    public static double  bucketPulley = 0.7;

    //TODO: More Specimen Positions to Change
    public static double  specimenPulley = 0.4;
    public static double  specimenscorePulley = 0.4;
    public static double  specimenscoredPulley = 0.35;
    public static double  specimenIntakePulley = 1.0;




}
