package org.riverdell.robotics.subsystems

import com.qualcomm.hardware.limelightvision.LLResult
import com.qualcomm.hardware.limelightvision.LLResultTypes
import io.liftgate.robotics.mono.subsystem.AbstractSubsystem
import org.riverdell.robotics.HypnoticRobot
import kotlin.math.tan


class LimelightManager(private val robot: HypnoticRobot) : AbstractSubsystem() {

    lateinit var currentResult: LLResult
    private var currentPipeline = LimelightState.Yellow
    var lastTx: Double = 0.0
    var lastTy: Double = 0.0
    var stopLimeIntake = true

    //In reference to the turret center
    var lime_x_offset: Double = 0.0 //unit mm
    var lime_y_offset: Double = 0.0 //unit mm
    var lime_z_offset: Double = 0.0 //unit mm

    var lime_pitch: Double = 0.0 //unit degrees
    var lime_yaw: Double = 0.0 //unit degrees

    var arm_length: Double = 0.0 //unit degrees
    var extendo_unit_to_in: Double = 0.2 //unit (inches/unit)


    private val limelight = robot.hardware.limelight

    enum class LimelightState {
        Yellow, Blue, Red
    }

    fun init() {
        limelight.pipelineSwitch(currentPipeline.ordinal + 1)
        limelight.start()
        limelight.setPollRateHz(100)
    }

    fun Extend(position: Int) = robot.extension.extendToAndStayAt(position)
    fun SetLeftDiffy(position: Double) = robot.iv4b.leftDiffyRotate(position)
    fun SetRightDiffy(position: Double) = robot.iv4b.rightDiffyRotate(position)

    fun setCurrentPipeline(newPipeline: LimelightState) {
        currentPipeline = newPipeline
        limelight.pipelineSwitch(currentPipeline.ordinal + 1)
    }

    fun limeIntaking() {
        // Start the intake mechanism.
        robot.compositein.setIntake(CompositeIntake.IntakeState.Intake)
        var stopLimeIntake = false
        // === Calibration & Geometry Constants ===
        // The camera’s vertical offset (height) above the turret’s center (in mm)
        val cameraHeight = lime_z_offset
        // Base diffy servo value when the turret arm is in its neutral (upward) position.
        val baseServoValue = 0.5
        // Conversion factor for degrees to servo position increment.
        val conversionFactor = 0.00111111
        // Fixed length of the turret's arm (in mm)
        val turretArmLength = 300.0
        // Horizontal offsets of the camera relative to the turret’s stationary center.
        val cameraOffsetX = lime_x_offset // lateral offset (mm)
        val cameraOffsetY = lime_y_offset // forward offset (mm)

        while (!stopLimeIntake) {
            // Get the latest vision result.
            currentResult = limelight.getLatestResult()

            // If no target is detected (tx and ty are zero), skip this cycle.
            if (currentResult.tx == 0.0 && currentResult.ty == 0.0) {
                Thread.sleep(50)
                continue
            }

            // --- Step 1. Compute horizontal distance from the camera to the floor ---
            // effectiveVerticalAngle = |camera pitch + measured ty|
            // (Make sure angles are set so that the downward angle is positive for this calculation)
            val effectiveVerticalAngle = kotlin.math.abs(lime_pitch + currentResult.ty)
            val effectiveVerticalAngleRadians = Math.toRadians(effectiveVerticalAngle)
            // Using the camera height above the turret center to compute horizontal distance.
            val horizontalDistance = cameraHeight / kotlin.math.tan(effectiveVerticalAngleRadians)

            // --- Step 2. Compute target coordinates in the camera frame (horizontal plane) ---
            val txRadians = Math.toRadians(currentResult.tx)
            val targetXCamera = horizontalDistance * kotlin.math.sin(txRadians)
            val targetYCamera = horizontalDistance * kotlin.math.cos(txRadians)

            // --- Step 3. Transform target coordinates from camera to turret coordinates ---
            // Rotate the camera coordinates by the camera's yaw and add the camera's horizontal offsets.
            val cameraYawRadians = Math.toRadians(lime_yaw)
            val targetXTurret =
                targetXCamera * kotlin.math.cos(cameraYawRadians) - targetYCamera * kotlin.math.sin(
                    cameraYawRadians
                ) + cameraOffsetX
            val targetYTurret =
                targetXCamera * kotlin.math.sin(cameraYawRadians) + targetYCamera * kotlin.math.cos(
                    cameraYawRadians
                ) + cameraOffsetY

            // --- Step 4. Solve for turret arm angle (A) and extension (E) ---
            // Ensure that targetXTurret is within the reachable range of the turret arm.
            val clampedRatio =
                targetXTurret.coerceIn(-turretArmLength, turretArmLength) / turretArmLength
            val turretAngleRadians = kotlin.math.asin(clampedRatio)
            val turretAngleDegrees = Math.toDegrees(turretAngleRadians)

            // Compute the required extension so that:
            // extension + turretArmLength*cos(A) = targetYTurret
            val desiredExtension =
                targetYTurret - turretArmLength * kotlin.math.cos(turretAngleRadians)
            // Clamp the extension value to the allowed range of your mechanism.
            val clampedExtension = desiredExtension.coerceIn(0.0, arm_length)

            // --- Step 5. Command turret rotation using diffy servos ---
            // 0.5 is the neutral value corresponding to 90° (arm pointing up).
            val servoAngleOffset = turretAngleDegrees - 90.0
            SetLeftDiffy(baseServoValue + servoAngleOffset * conversionFactor)
            SetRightDiffy(baseServoValue - servoAngleOffset * conversionFactor)

            // Command the extension.
            Extend(clampedExtension.toInt())

            // Wait 50 milliseconds before the next update.
            Thread.sleep(50)
        }
    }

    /**
     * Placeholder exit condition. Replace with your actual logic.
     */
    fun turnOff() {
        var stopLimeIntake = false
    }


    override fun start() {

    }

    override fun doInitialize() {

    }
}