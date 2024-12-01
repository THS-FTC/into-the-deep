package org.riverdell.robotics.subsystems.outtake.v4b

enum class PulleyState(val position: Double)
{
    Transfer(0.3), OuttakeBucket(0.5), OuttakeSpecimen(0.3)
}