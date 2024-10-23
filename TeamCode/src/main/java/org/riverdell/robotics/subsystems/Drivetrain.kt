package org.riverdell.robotics.subsystems

import com.arcrobotics.ftclib.drivebase.MecanumDrive
import com.arcrobotics.ftclib.gamepad.GamepadEx
import com.arcrobotics.ftclib.hardware.motors.Motor
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.IMU
import io.liftgate.robotics.mono.subsystem.AbstractSubsystem
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import org.riverdell.robotics.HypnoticRobot
import org.riverdell.robotics.autonomous.HypnoticAuto
import org.riverdell.robotics.autonomous.movement.localization.TwoWheelLocalizer
import org.riverdell.robotics.gobilda.GoBildaPinpointDriver
import org.riverdell.robotics.utilities.hardware

class Drivetrain(private val opMode: HypnoticRobot) : AbstractSubsystem()
{
    lateinit var frontRight: DcMotorEx
    lateinit var frontLeft: DcMotorEx

    lateinit var backRight: DcMotorEx
    lateinit var backLeft: DcMotorEx

    lateinit var pinpointDriver: GoBildaPinpointDriver

    private val imuState by state(write = { _ -> }, read = { pinpointDriver.heading })
    private val voltageState by state(write = { _ -> }, read = {
        opMode.hardwareMap.voltageSensor.first().voltage
    })

    val localizer by lazy {
        TwoWheelLocalizer(opMode)
    }

    private lateinit var backingDriveBase: MecanumDrive

    fun voltage() = voltageState.current()
    fun imu() = imuState.current()

    fun driveRobotCentric(driverOp: GamepadEx, scaleFactor: Double)
    {
        backingDriveBase.driveRobotCentric(
            -driverOp.leftY * scaleFactor,
            -driverOp.leftX * scaleFactor,
            -driverOp.rightX * scaleFactor,
            true
        )
    }

    fun driveFieldCentric(driverOp: GamepadEx, scaleFactor: Double)
    {
        val heading = imuState.current()
        backingDriveBase.driveFieldCentric(
            -driverOp.leftX * scaleFactor,
            -driverOp.leftY * scaleFactor,
            driverOp.rightX * scaleFactor,
            heading,
            true
        )
    }

    override fun start()
    {

    }

    /**
     * Initializes both the IMU and all drivebase motors.
     */
    override fun doInitialize()
    {
        pinpointDriver = opMode.hardware<GoBildaPinpointDriver>("pinpoint")

        if (opMode is HypnoticAuto)
        {
            pinpointDriver.setOffsets(-84.0, -168.0) // TUNE THIS
            pinpointDriver.setEncoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD)
            pinpointDriver.recalibrateIMU()
            pinpointDriver.resetPosAndIMU()

            frontLeft = opMode.hardware<DcMotorEx>("frontLeft")
            frontRight = opMode.hardware<DcMotorEx>("frontRight")
            backLeft = opMode.hardware<DcMotorEx>("backLeft")
            backRight = opMode.hardware<DcMotorEx>("backRight")

            frontLeft.direction = DcMotorSimple.Direction.REVERSE
            frontLeft.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE

            frontRight.direction = DcMotorSimple.Direction.FORWARD
            frontRight.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE

            backLeft.direction = DcMotorSimple.Direction.REVERSE
            backLeft.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE

            backRight.direction = DcMotorSimple.Direction.FORWARD
            backRight.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE

            runWithoutEncoders()
        } else
        {
            setupDriveBase()
        }
    }

    fun setupDriveBase()
    {
        val backLeft = Motor(opMode.hardwareMap, "backLeft")
        val backRight = Motor(opMode.hardwareMap, "backRight")
        val frontLeft = Motor(opMode.hardwareMap, "frontLeft")
        val frontRight = Motor(opMode.hardwareMap, "frontRight")
        backLeft.inverted = true

        backingDriveBase = MecanumDrive(
            frontLeft, backLeft, frontRight, backRight
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
        listOf(backLeft, frontLeft, frontRight, backRight).forEach(consumer::invoke)
    }

    override fun isCompleted() = true
    override fun dispose()
    {
        if (opMode is HypnoticAuto)
        {
            stopAndResetMotors()
        }
    }
}