package org.riverdell.robotics

import android.util.Log
import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import io.liftgate.robotics.mono.Mono
import io.liftgate.robotics.mono.subsystem.AbstractSubsystem
import io.liftgate.robotics.mono.subsystem.Subsystem
import io.liftgate.robotics.mono.subsystem.System
import org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap
import com.pedropathing.follower.Follower;
import org.riverdell.robotics.subsystems.CompositeIntake
import org.riverdell.robotics.subsystems.CompositeOuttake
import org.riverdell.robotics.subsystems.Drivetrain
import org.riverdell.robotics.subsystems.Extension
import org.riverdell.robotics.subsystems.IV4B
import org.riverdell.robotics.subsystems.Intake
import org.riverdell.robotics.subsystems.Lift
import org.riverdell.robotics.subsystems.OV4B
import org.riverdell.robotics.subsystems.Outtake

abstract class HypnoticRobot(val opMode: HypnoticOpMode) : System
{
    companion object
    {
        @JvmStatic
        lateinit var instance: HypnoticRobot
    }

    override val subsystems: MutableSet<Subsystem> = mutableSetOf()
    lateinit var hardware: HypnoticRobotHardware

    val drivetrain by lazy { Drivetrain(this) }
    val intake by lazy { Intake(this) }
    val iv4b by lazy { IV4B(this) }
    val lift by lazy { Lift(this) }
    val extension by lazy { Extension(this) }
    val ov4b by lazy { OV4B(this) }
    val outtake by lazy { Outtake(this) }
    val compositeout by lazy { CompositeOuttake(this) }
    val compositein by lazy { CompositeIntake(this) }
    val follower by lazy { Follower(opMode.hardwareMap) }

    val multipleTelemetry by lazy {
        MultipleTelemetry(
            opMode.telemetry,
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

    fun runOpMode()
    {
        instance = this

        hardware = HypnoticRobotHardware(opMode)
        hardware.initializeHardware()

        register(
            drivetrain, intake, iv4b, lift, extension, ov4b, outtake, compositeout, compositein,
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

        opMode.waitForStart()
        if (opMode.isStopRequested)
        {
            return
        }

        startAll()
        opModeStart()
        disposeOfAll()
    }
}