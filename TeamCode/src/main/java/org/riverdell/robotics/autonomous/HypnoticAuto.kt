package org.riverdell.robotics.autonomous

import io.liftgate.robotics.mono.Mono
import io.liftgate.robotics.mono.konfig.konfig
import io.liftgate.robotics.mono.pipeline.RootExecutionGroup
import io.liftgate.robotics.mono.subsystem.AbstractSubsystem
import org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion
import org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import org.riverdell.robotics.HypnoticOpMode
import org.riverdell.robotics.HypnoticRobot
import org.riverdell.robotics.PedroAuto.Constants.Points
import org.riverdell.robotics.autonomous.HypnoticAuto.Companion
import org.riverdell.robotics.pedroPathing.follower.Follower
import org.riverdell.robotics.utilities.managed.ManagedMotorGroup
import java.util.concurrent.CountDownLatch
import kotlin.concurrent.thread

abstract class HypnoticAuto(
    internal val blockExecutionGroup: RootExecutionGroup.(HypnoticAuto) -> Unit
) : HypnoticOpMode()
{
    companion object
    {
        @JvmStatic
        lateinit var instance: HypnoticAuto
    }

    inner class HypnoticAutoRobot : HypnoticRobot(this@HypnoticAuto)
    {
        //val navigationConfig = NavigationConfig()
//       val visionPipeline by lazy { VisionPipeline(this@HypnoticAuto) } // TODO: new season

        override fun additionalSubSystems() = listOf<AbstractSubsystem>(/*visionPipeline*/)
        override fun initialize()
        {
            HypnoticAuto.instance = this@HypnoticAuto
            robot.follower.setStartingPose(Points.slantStartPose)

            while (opModeInInit())
            {
                runPeriodics()
                //drivetrain.localizer.update()
                robot.follower.update()

                multipleTelemetry.addLine("--- Initialization ---")
                multipleTelemetry.addData(
                    "Voltage",
                    drivetrain.voltage()
                )
//                multipleTelemetry.addData(
//                    "IMU",
//                    drivetrain.imu()
//                )
//                multipleTelemetry.addData(
//                    "Pose",
//                    drivetrain.localizer.pose
//                )

                multipleTelemetry.update()
            }
        }

        override fun opModeStart()
        {

            thread {
                while (!isStopRequested)
                {
                    runPeriodics()
                    robot.follower.update()
                    //drivetrain.localizer.update()
                    // add later when using hypnotic follower

                    multipleTelemetry.addLine("--- Autonomous ---")
                    multipleTelemetry.addData(
                        "Voltage",
                        drivetrain.voltage()
                    )
//                    multipleTelemetry.addData(
//                        "IMU",
//                        drivetrain.imu()
//                    )
//                    multipleTelemetry.addData(
//                        "Pose",
//                        drivetrain.localizer.pose
                    //)

                    multipleTelemetry.update()
                }
            }

            val executionGroup = Mono.buildExecutionGroup {
                blockExecutionGroup(
                    this@HypnoticAuto
                )
            }

            ManagedMotorGroup.autonomous = true
            executionGroup.executeBlocking()
        }
    }

    override fun buildRobot() = HypnoticAutoRobot()
}
