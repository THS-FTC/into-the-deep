package org.robotics.robotics.xdk.teamcode.subsystem.claw

import com.acmerobotics.dashboard.config.Config

@Config
object ClawExpansionConstants
{
    @JvmField var Test = 0.9

    @JvmField var CLAW_MOTION_PROFILE_VELOCITY = 5.0
    @JvmField var CLAW_MOTION_PROFILE_ACCEL = 10.0
    @JvmField var CLAW_MOTION_PROFILE_DECEL = 30.0

    @JvmField var CLAW_FINGER_PROFILE_VELOCITY = 5.0
    @JvmField var CLAW_FINGER_PROFILE_ACCEL = 33.0
    @JvmField var CLAW_FINGER_PROFILE_DECEL = 50.0

    @JvmField var CLOSED_LEFT_CLAW = 0.08
    @JvmField var OPEN_LEFT_CLAW = 0.16
    @JvmField var OPEN_LEFT_CLAW_INTAKE = 0.30

    @JvmField var CLOSED_RIGHT_CLAW = 0.9
    @JvmField var OPEN_RIGHT_CLAW = 0.82
    @JvmField var OPEN_RIGHT_CLAW_INTAKE = 0.68
}