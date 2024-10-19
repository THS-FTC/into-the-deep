package org.riverdell.robotics

import android.util.Log
import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import io.liftgate.robotics.mono.Mono
import io.liftgate.robotics.mono.subsystem.AbstractSubsystem
import io.liftgate.robotics.mono.subsystem.Subsystem
import io.liftgate.robotics.mono.subsystem.System
import org.riverdell.robotics.autonomous.impl.tests.ExampleSystem
import org.riverdell.robotics.subsystems.Drivetrain
import org.riverdell.robotics.subsystems.Outtake

abstract class HypnoticRobot : LinearOpMode(), System
{
    companion object
    {
        @JvmStatic
        lateinit var instance: HypnoticRobot
    }

    override val subsystems: MutableSet<Subsystem> = mutableSetOf()

    val drivetrain by lazy { Drivetrain(this) }
    val outtake by lazy { Outtake(this) }

    val multipleTelemetry by lazy {
        MultipleTelemetry(
            this.telemetry,
            FtcDashboard.getInstance().telemetry
        )
    }

    abstract fun initialize()
    abstract fun opModeStart()

    fun runPeriodics()
    {
        subsystems
            .map { it as AbstractSubsystem }
            .forEach { it.allPeriodic() }
    }

    open fun additionalSubSystems(): List<AbstractSubsystem>
    {
        return emptyList()
    }

    override fun runOpMode()
    {
        instance = this

        register(
            drivetrain, outtake,
            *additionalSubSystems().toTypedArray()
        )

        // keep all log entries
        Mono.logSink = {
            multipleTelemetry.addLine("[mono] $it")
            multipleTelemetry.update()
            Log.i("mono", it)
        }

        initializeAll()
        initialize()

        waitForStart()
        if (isStopRequested)
        {
            return
        }

        startAll()
        opModeStart()
        disposeOfAll()
    }
}