package org.riverdell.robotics.subsystems;

import com.acmerobotics.dashboard.config.Config;

@Config
public class SlideConfig {
    //lift
    public static int liftHighBucket = 2000;
    public static int liftSpecimen  = 500;
    public static int downSpecimen  = 300;

    public static int liftLowBucket  = 300;
    public static int liftClosed  = 0;

   //extendo
    public static int extendoIntake = -500;
    public static int extendoTransfer = -65;
    public static int extendoClosed = 0;
    public static int extendoGetOut = -140;
    
 
}
