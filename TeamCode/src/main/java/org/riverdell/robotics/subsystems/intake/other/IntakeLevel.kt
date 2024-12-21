package org.riverdell.robotics.subsystems.intake.other

import org.riverdell.robotics.subsystems.slides.SlideConfig


enum class IntakeLevel(val encoderPercentage: Double)
{
    FullOut(1.0), Transfer(0.6), PartialOut(0.9),OuttakeOut(0.7),Idle(0.0);

    val encoderLevel: Int
        get() = (encoderPercentage * SlideConfig.INTAKE_MAXIMUM_EXTENSION).toInt()

    fun next() = entries.getOrNull(ordinal + 1)
    fun previous() = entries.getOrNull(ordinal - 1)
}