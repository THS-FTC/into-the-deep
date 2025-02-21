package org.riverdell.robotics.subsystems;

import com.acmerobotics.dashboard.config.Config;

@Config
public class IV4BConfig {

    public static double idlePosition= 0.3;
    public static double moveAwayPosition  = 0.4;
    public static double transferPosition = 0.25;
    public static double observePosition= 0.60;
    public static double grabPosition= 0.77;

    public static boolean leftIsReversed = false;
}