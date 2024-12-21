package org.riverdell.robotics.subsystems.outtake.other

import org.riverdell.robotics.subsystems.slides.SlideConfig


enum class OuttakeLevel(val encoderPercentage: Double)
{
    Bar1(0.2), Bar2(0.5), Bar2Down(0.3),Bar1Down(0.0), HighBasket(1.0),Rest(0.0);

    val encoderLevel: Int
        get() = (encoderPercentage * SlideConfig.OUTTAKE_MAXIMUM_EXTENSION).toInt()

    fun next() = entries.getOrNull(ordinal + 1)
    fun previous() = entries.getOrNull(ordinal - 1)
}