package org.riverdell.robotics.subsystems

import com.acmerobotics.roadrunner.control.PIDCoefficients
import io.liftgate.robotics.mono.subsystem.AbstractSubsystem
import org.riverdell.robotics.HypnoticRobot
import org.riverdell.robotics.utilities.managed.ManagedMotorGroup
import org.riverdell.robotics.utilities.managed.pidf.PIDFConfig

class Extension(val robot: HypnoticRobot) : AbstractSubsystem()
{
    val slides = with(PIDFConfig(0.01, 0.0, 0.0)) {
        ManagedMotorGroup(
            this@Extension,
            PIDCoefficients(kP, kI, kD),
            kV, kA, kStatic,
//            kF =  { position, velocity ->
//                if (position > -100 && velocity!! > 0) {
//                    0.35
//                } else {
//                    0.0
//                }
            master = robot.hardware.extensionMotorLeft,
            slaves = listOf(robot.hardware.extensionMotorRight)
        ).withTimeout(1500L)
    }

    fun position() = robot.hardware.extensionMotorRight.currentPosition
    fun extendToAndStayAt(position: Int) = slides.goTo(position)
    fun idle() = slides.idle();

    fun isExtending() = slides.isTravelling()


    override fun start()
    {
        extendToAndStayAt(0)
    }

    override fun doInitialize()
    {

    }

}