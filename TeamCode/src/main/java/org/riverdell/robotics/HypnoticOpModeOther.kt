package org.riverdell.robotics

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.OpMode

abstract class HypnoticOpModeOther : OpMode()
{
    lateinit var robot: HypnoticRobot

    abstract fun buildRobot(): HypnoticRobot

    fun runOpMode()
    {
        robot = buildRobot()
        robot.runOpMode()
    }
}