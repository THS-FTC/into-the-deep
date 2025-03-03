package org.riverdell.robotics

import com.qualcomm.hardware.limelightvision.Limelight3A
import com.qualcomm.robotcore.hardware.CRServoImplEx
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.ServoImplEx
import org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap
import org.riverdell.robotics.subsystems.IV4BConfig
import org.riverdell.robotics.subsystems.IntakeConfig
import org.riverdell.robotics.subsystems.OV4BConfig
import org.riverdell.robotics.subsystems.OutakeConfig


class HypnoticRobotHardware(private val opMode: HypnoticOpMode)
{

    lateinit var liftMotorLeft: DcMotorEx
    lateinit var liftMotorRight: DcMotorEx

    lateinit var extensionMotorLeft: DcMotorEx
    lateinit var extensionMotorRight: DcMotorEx

    lateinit var frontRight: DcMotorEx
    lateinit var frontLeft: DcMotorEx
    lateinit var backRight: DcMotorEx
    lateinit var backLeft: DcMotorEx
    lateinit var limelight: Limelight3A


    lateinit var intakeV4BLeft: ServoImplEx
    lateinit var intakeV4BRight: ServoImplEx

    lateinit var hangServoLeft: CRServoImplEx
    lateinit var hangServoRight: CRServoImplEx

    lateinit var intakeWrist: ServoImplEx
    lateinit var intakePulley: ServoImplEx
    lateinit var intakeGrip: ServoImplEx

    lateinit var outtakeRotationLeft: ServoImplEx
    lateinit var outtakeRotationRight: ServoImplEx
    lateinit var outtakePulley: ServoImplEx

    lateinit var outtakeWrist: ServoImplEx
    lateinit var outtakeGrip: ServoImplEx

    fun initializeHardware()
    {
        //pinpointDriver = opMode.hardwareMap.get(GoBildaPinpointDriver::class.java, "pinpoint")
        /*pinpointDriver.setOffsets(-69.2125, -72.33931) // Done
        pinpointDriver.setEncoderResolution(37.2499090578) // TODO:("this is the encoder values, make sure it is right")
        pinpointDriver.setEncoderDirections(GoBildaPinpointDriver.EncoderDirection.FORWARD,GoBildaPinpointDriver.EncoderDirection.REVERSED ) //tune this!!!!
        pinpointDriver.recalibrateIMU()
        pinpointDriver.resetPosAndIMU() */

        frontLeft = opMode.hardwareMap.get(DcMotorEx::class.java, "frontLeft")
        frontRight = opMode.hardwareMap.get(DcMotorEx::class.java, "frontRight")
        backLeft = opMode.hardwareMap.get(DcMotorEx::class.java, "backLeft")
        backRight = opMode.hardwareMap.get(DcMotorEx::class.java, "backRight")

        backRight.direction = DcMotorSimple.Direction.REVERSE
        backLeft.direction = DcMotorSimple.Direction.FORWARD

        liftMotorLeft = opMode.hardwareMap["lift_motor_left"] as DcMotorEx
        liftMotorLeft.direction = DcMotorSimple.Direction.REVERSE
        liftMotorLeft.direction = DcMotorSimple.Direction.REVERSE// idk why this works

        liftMotorRight = opMode.hardwareMap["lift_motor_right"] as DcMotorEx
        liftMotorRight.direction = DcMotorSimple.Direction.REVERSE
        liftMotorRight.direction = DcMotorSimple.Direction.REVERSE// idk why this works

        extensionMotorLeft = opMode.hardwareMap["extension_motor_left"] as DcMotorEx
        extensionMotorLeft.direction = DcMotorSimple.Direction.REVERSE

        extensionMotorRight = opMode.hardwareMap["extension_motor_right"] as DcMotorEx
        extensionMotorRight.direction = DcMotorSimple.Direction.FORWARD

        limelight = hardwareMap.get(Limelight3A::class.java, "limelight")

        //THIS IS IN SPECIMEN POSITION BTW
        intakeV4BLeft = opMode.hardwareMap.get(ServoImplEx::class.java, "iv4b_rotation_left")
        intakeV4BLeft.position = IV4BConfig.hiddenPosition

        intakeV4BRight = opMode.hardwareMap.get(ServoImplEx::class.java, "iv4b_rotation_right")
        intakeV4BRight.position = 1.0 - IV4BConfig.hiddenPosition

        // Intake
        intakeWrist = opMode.hardwareMap.get(ServoImplEx::class.java, "intake_wrist")
        intakeWrist.position = IntakeConfig.veritcalPosition // test this remeber to

        intakePulley = opMode.hardwareMap.get(ServoImplEx::class.java, "intake_pulley")
        intakePulley.position = IntakeConfig.awayPulley

        intakeGrip = opMode.hardwareMap.get(ServoImplEx::class.java, "intake_grip")
        intakeGrip.position = IntakeConfig.closePosition

        hangServoLeft = opMode.hardwareMap.get(CRServoImplEx::class.java, "hang_left")
        hangServoLeft.power = 0.0

        hangServoRight = opMode.hardwareMap.get(CRServoImplEx::class.java, "hang_right")
        hangServoRight.power = 0.0
        // Outtake
        outtakeRotationLeft = opMode.hardwareMap.get(ServoImplEx::class.java, "ov4b_rotation_left")
        outtakeRotationLeft.position = 1.0 -OV4BConfig.SpecimenIntakePosition

        outtakeRotationRight = opMode.hardwareMap.get(ServoImplEx::class.java, "ov4b_rotation_right")
        outtakeRotationRight.position =OV4BConfig.SpecimenIntakePosition

        outtakePulley = opMode.hardwareMap.get(ServoImplEx::class.java, "ov4b_pulley")
        outtakePulley.position = OV4BConfig.specimenIntakePulley

        outtakeGrip = opMode.hardwareMap.get(ServoImplEx::class.java, "outtake_grip")
        outtakeGrip.position = OutakeConfig.closePositon

        outtakeWrist = opMode.hardwareMap.get(ServoImplEx::class.java, "outtake_wrist")
        outtakeWrist.position = OutakeConfig.frontPosition
    }
}