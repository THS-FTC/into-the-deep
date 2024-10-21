package org.riverdell.robotics.subsystems;

import com.acmerobotics.dashboard.config.Config;

@Config
public class IV4BConfig {
    public static double idlePosition= 0.0;
    public static double moveAwayPosition  = 0.5;

    //rotation
    public static double transferPosition = 0.0;
    public static double observePosition= 0.8;
    public static double grabPosition= 0.92;

    public static boolean leftIsReversed = false;
}
