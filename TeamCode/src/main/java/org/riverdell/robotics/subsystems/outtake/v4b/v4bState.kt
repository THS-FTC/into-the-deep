package org.riverdell.robotics.subsystems.outtake.v4b

enum class v4bState(val position: Double)
{
    Outtake(0.3), Transfer(1.0), Specimen(0.1),Idle(1.0)
}