package org.riverdell.robotics.subsystems

import com.arcrobotics.ftclib.drivebase.MecanumDrive
import com.arcrobotics.ftclib.gamepad.GamepadEx
import com.arcrobotics.ftclib.hardware.motors.Motor
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple
import io.liftgate.robotics.mono.subsystem.AbstractSubsystem
import org.riverdell.robotics.HypnoticRobot
import org.riverdell.robotics.autonomous.HypnoticAuto

class Drivetrain(private val robot: HypnoticRobot) : AbstractSubsystem()
{
    private val voltageSensor = robot.opMode.hardwareMap.voltageSensor.first()

    //private val imuState by state(write = { _ -> }, read = { robot.hardware.pinpointDriver.heading })
    private val voltageState by state(write = { _ -> }, read = { voltageSensor.voltage})

    private lateinit var backingDriveBase: MecanumDrive

    fun voltage() = voltageState.current()
    //fun imu() = imuState.current()

    fun driveRobotCentric(driverOp: GamepadEx, scaleFactor: Double)
    {
        backingDriveBase.driveRobotCentric(
            -driverOp.leftY * scaleFactor,
            -driverOp.leftX * scaleFactor,
            -driverOp.rightX * scaleFactor,
            true
        )
    }

    /*fun driveFieldCentric(driverOp: GamepadEx, scaleFactor: Double)
    {
        val heading = imuState.current()
        backingDriveBase.driveFieldCentric(
            -driverOp.leftX * scaleFactor,
            -driverOp.leftY * scaleFactor,
            driverOp.rightX * scaleFactor,
            heading,
            true
        )
    } */

    override fun start()
    {

    }

    /**
     * Initializes both the IMU and all drivebase motors.
     */
    override fun doInitialize()
    {
        if (robot.opMode !is HypnoticAuto)
        {
            setupDriveBase()
        }
    }

    fun setupDriveBase()
    {
        val backLeft = Motor(robot.opMode.hardwareMap, "frontRight")
        val backRight = Motor(robot.opMode.hardwareMap, "backRight")
        val frontLeft = Motor(robot.opMode.hardwareMap, "frontLeft")
        val frontRight = Motor(robot.opMode.hardwareMap, "backLeft")

        backingDriveBase = MecanumDrive(
            frontLeft, frontRight, backLeft, backRight
        )
    }

    fun stopAndResetMotors() = configureMotorsToDo {
        it.power = 0.0
        it.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
    }

    fun runWithoutEncoders() = configureMotorsToDo {
        it.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
    }

    private fun configureMotorsToDo(consumer: (DcMotor) -> Unit)
    {
        listOf(robot.hardware.backLeft, robot.hardware.frontLeft, robot.hardware.frontRight, robot.hardware.backRight).forEach(consumer::invoke)
    }

    override fun isCompleted() = true
    override fun dispose()
    {
        if (robot.opMode is HypnoticAuto)
        {
            stopAndResetMotors()
        }
    }
}