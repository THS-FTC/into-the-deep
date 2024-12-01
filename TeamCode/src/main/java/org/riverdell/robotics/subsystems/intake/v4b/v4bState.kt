package org.riverdell.robotics.subsystems.intake.v4b

enum class v4bState(val position: Double)
{
    Observe(0.65), Transfer(0.245), Grab(0.77),MoveAway(0.4),Idle(0.3)
}