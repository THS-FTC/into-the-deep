package org.riverdell.robotics.subsystems

import com.qualcomm.hardware.limelightvision.LLResult
import com.qualcomm.hardware.limelightvision.Limelight3A
import io.liftgate.robotics.mono.subsystem.AbstractSubsystem
import org.riverdell.robotics.HypnoticRobot
import kotlin.math.*

class LimelightManager(robot: HypnoticRobot) : AbstractSubsystem() {

    lateinit var currentResult: LLResult
    private var currentPipeline = LimelightState.Yellow
    var stopLimeIntaking = false

    // Offsets for the camera relative to the turret center (in mm)
    var lime_x_offset: Double = 0.0
    var lime_y_offset: Double = 0.0
    var lime_z_offset: Double = 100.0  // e.g., 100 mm above turret center

    var lime_pitch: Double = 10.0      // e.g., camera tilted downward 10°
    var lime_yaw: Double = 0.0

    // arm_length represents the maximum rail travel (in mm) for moving the full turret system forward.
    var arm_length: Double = 500.0

    // Telemetry variables.
    var estimatedTargetX: Double = 0.0
    var estimatedTargetY: Double = 0.0
    var estimatedTurretAngle: Double = 0.0

    // Acceptable class names for detection (e.g., "Yellow", "Red", etc.)
    var wantedClassIDs: List<String> = listOf("Yellow")

    // Access hardware from the robot.
    private val limelight = robot.hardware.limelight
    private val extension = robot.extension
    private val iv4b = robot.iv4b
    private val compositein = robot.compositein

    enum class LimelightState {
        Yellow, Blue, Red
    }

    fun init() {
        limelight.pipelineSwitch(currentPipeline.ordinal + 1)
        limelight.start()
        limelight.setPollRateHz(100)
    }

    override fun start() {
        // Optional start code.
    }

    override fun doInitialize() {
        // Optional initialization code.
    }

    fun turnOff() {
        stopLimeIntaking = true
    }

    /**
     * Main intaking function.
     * This method is blocking; it runs its own loop until turnOff() is called.
     */
    fun limeIntaking() {
        // Start the intake.
        compositein.setIntake(CompositeIntake.IntakeState.Intake)
        stopLimeIntaking = false

        val cameraHeight = lime_z_offset             // mm
        val baseServoValue = 0.5                      // Neutral servo value (turret arm "up")
        val turretArmLength = 300.0                   // mm (fixed turret arm length)
        val maxTurretAngle = 45.0                     // Maximum turret heading (degrees)

        while (!stopLimeIntaking) {
            currentResult = limelight.getLatestResult()

            if ((currentResult.tx == 0.0 && currentResult.ty == 0.0) ||
                currentResult.detectorResults.isEmpty()) {
                Thread.sleep(50)
                continue
            }

            // Select the first detection whose className is in wantedClassIDs.
            val chosenDetection = currentResult.detectorResults.firstOrNull { detection ->
                wantedClassIDs.any { it == detection.className }
            }
            if (chosenDetection == null) {
                Thread.sleep(50)
                continue
            }

            // Use global tx and ty as the detection's center.
            val detectionCenterTx = currentResult.tx
            val detectionCenterTy = currentResult.ty

            // Compute horizontal distance using the effective vertical angle.
            val effectiveVerticalAngle = abs(lime_pitch + detectionCenterTy)
            val effectiveVerticalAngleRadians = Math.toRadians(effectiveVerticalAngle)
            val horizontalDistance = cameraHeight / tan(effectiveVerticalAngleRadians)

            // Compute target coordinates in the camera frame.
            val centerTxRadians = Math.toRadians(detectionCenterTx)
            val targetXCamera = horizontalDistance * sin(centerTxRadians)
            val targetYCamera = horizontalDistance * cos(centerTxRadians)

            // Transform target coordinates from camera to turret coordinates.
            val cameraYawRadians = Math.toRadians(lime_yaw)
            val targetXTurret = targetXCamera * cos(cameraYawRadians) -
                    targetYCamera * sin(cameraYawRadians) + lime_x_offset
            val targetYTurret = targetXCamera * sin(cameraYawRadians) +
                    targetYCamera * cos(cameraYawRadians) + lime_y_offset

            // Compute turret heading from the lateral offset.
            val rawTurretAngleRadians = asin(targetXTurret.coerceIn(-turretArmLength, turretArmLength) / turretArmLength)
            var turretAngleDegrees = Math.toDegrees(rawTurretAngleRadians)
            turretAngleDegrees = turretAngleDegrees.coerceIn(-maxTurretAngle, maxTurretAngle)

            // Compute desired rail travel such that:
            // turretArmLength * cos(θ) + railTravel = targetYTurret.
            val desiredRailTravel = targetYTurret - turretArmLength * cos(Math.toRadians(turretAngleDegrees))
            val clampedRailTravel = desiredRailTravel.coerceIn(0.0, arm_length)

            // Map turret heading to diffy servo commands.
            val leftServoPosition: Double
            val rightServoPosition: Double
            if (turretAngleDegrees >= 0) {
                leftServoPosition = baseServoValue - (turretAngleDegrees / maxTurretAngle) * 0.25
                rightServoPosition = baseServoValue
            } else {
                leftServoPosition = baseServoValue
                rightServoPosition = baseServoValue - (abs(turretAngleDegrees) / maxTurretAngle) * 0.25
            }

            iv4b.leftDiffyRotate(leftServoPosition)
            iv4b.rightDiffyRotate(rightServoPosition)
            extension.extendToAndStayAt(clampedRailTravel.toInt())

            estimatedTargetX = targetXTurret
            estimatedTargetY = targetYTurret
            estimatedTurretAngle = turretAngleDegrees

            Thread.sleep(50)
        }
    }
}
