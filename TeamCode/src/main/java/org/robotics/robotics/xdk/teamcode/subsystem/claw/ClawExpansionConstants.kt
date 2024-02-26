package org.robotics.robotics.xdk.teamcode.subsystem.claw

import com.acmerobotics.dashboard.config.Config

@Config
object ClawExpansionConstants
{
    @JvmField var CLAW_MOTION_PROFILE_VELOCITY = 5.0
    @JvmField var CLAW_MOTION_PROFILE_ACCEL = 5.0
    @JvmField var CLAW_MOTION_PROFILE_DECEL = 2.0

    @JvmField var CLAW_FINGER_PROFILE_VELOCITY = 5.0
    @JvmField var CLAW_FINGER_PROFILE_ACCEL = 33.0
    @JvmField var CLAW_FINGER_PROFILE_DECEL = 50.0

    @JvmField var CLOSED_LEFT_CLAW = 0.5
    @JvmField var OPEN_LEFT_CLAW = 0.66 // 0.65 prev
    @JvmField var OPEN_LEFT_CLAW_INTAKE = 0.70

    @JvmField var CLOSED_RIGHT_CLAW = 0.9
    @JvmField var OPEN_RIGHT_CLAW = 0.78 // 0.76 prev
    @JvmField var OPEN_RIGHT_CLAW_INTAKE = 0.71
}