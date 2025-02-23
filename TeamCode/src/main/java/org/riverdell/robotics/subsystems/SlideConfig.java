package org.riverdell.robotics.subsystems;

import com.acmerobotics.dashboard.config.Config;

@Config
public class SlideConfig {
    //lift
    public static int liftHighBucket = 2000; //1700 maybe for quick score
    public static int liftSpecimen  = 600;
    public static int downSpecimen  = 0;

    public static int liftLowBucket  = 300;
    public static int liftClosed  = 0;

   //extendo
    public static int extendoIntake = -300;
    public static int extendoTransfer = -100;
    public static int extendoClosed = 0;
    public static int extendoIdle = 0;
    public static int extendoGetOut = -200;
    
 
}
