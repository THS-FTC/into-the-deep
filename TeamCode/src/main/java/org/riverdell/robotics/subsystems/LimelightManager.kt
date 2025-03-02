package org.riverdell.robotics.subsystems

import com.qualcomm.hardware.limelightvision.LLResult
import com.qualcomm.hardware.limelightvision.Limelight3A
import io.liftgate.robotics.mono.subsystem.AbstractSubsystem
import org.riverdell.robotics.CompositeIntakeInterface
import org.riverdell.robotics.ExtensionInterface
import org.riverdell.robotics.IV4BInterface
import kotlin.math.abs
import kotlin.math.asin
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.tan

class LimelightManager(
    private val limelight: Limelight3A,
    private val extension: ExtensionInterface,
    private val iv4b: IV4BInterface,
    private val compositein: CompositeIntakeInterface
) : AbstractSubsystem() {

    lateinit var currentResult: LLResult
    private var currentPipeline = LimelightState.Yellow
    var stopLimeIntake = false

    // Offsets for the camera relative to the turret center (in mm)
    var lime_x_offset: Double = 0.0
    var lime_y_offset: Double = 0.0
    var lime_z_offset: Double = 100.0  // Example: 100 mm above turret center

    var lime_pitch: Double = 10.0      // e.g., camera tilted down 10°
    var lime_yaw: Double = 0.0

    // The maximum allowed rail travel (in mm)
    var arm_length: Double = 500.0

    // Telemetry variables
    var estimatedTargetX: Double = 0.0
    var estimatedTargetY: Double = 0.0
    var estimatedTurretAngle: Double = 0.0

    // List of acceptable class names for detection
    var wantedClassIDs: List<String> = listOf("Yellow")

    enum class LimelightState {
        Yellow, Blue, Red
    }

    fun init() {
        limelight.pipelineSwitch(currentPipeline.ordinal + 1)
        limelight.start()
        limelight.setPollRateHz(100)
    }

    override fun start() {}
    override fun doInitialize() {}

    fun turnOff() {
        stopLimeIntake = true
    }

    /**
     * Main processing loop.
     * This is a blocking method that continuously polls the Limelight and
     * computes turret heading and rail travel based on the target's global tx and ty.
     */
    fun limeIntaking() {
        compositein.setIntake(CompositeIntakeInterface.IntakeState.Intake)
        stopLimeIntake = false

        val cameraHeight = lime_z_offset             // mm
        val baseServoValue = 0.5                      // Neutral servo value
        val turretArmLength = 300.0                   // mm (fixed turret arm length)
        val maxTurretAngle = 45.0                     // Maximum turret heading in degrees

        while (!stopLimeIntake) {
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

            // Use the global tx and ty as the detection's center.
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

            // Transform to turret coordinates.
            val cameraYawRadians = Math.toRadians(lime_yaw)
            val targetXTurret = targetXCamera * cos(cameraYawRadians) -
                    targetYCamera * sin(cameraYawRadians) + lime_x_offset
            val targetYTurret = targetXCamera * sin(cameraYawRadians) +
                    targetYCamera * cos(cameraYawRadians) + lime_y_offset

            // Compute turret heading using arcsin(targetXTurret / turretArmLength)
            val rawTurretAngleRadians = asin(targetXTurret.coerceIn(-turretArmLength, turretArmLength) / turretArmLength)
            var turretAngleDegrees = Math.toDegrees(rawTurretAngleRadians)
            turretAngleDegrees = turretAngleDegrees.coerceIn(-maxTurretAngle, maxTurretAngle)

            // Compute desired rail travel (forward movement)
            val desiredRailTravel = targetYTurret - turretArmLength * cos(Math.toRadians(turretAngleDegrees))
            val clampedRailTravel = desiredRailTravel.coerceIn(0.0, arm_length)

            // Map turret heading to diffy servo commands.
            val leftServoPosition: Double
            val rightServoPosition: Double
            if (turretAngleDegrees >= 0) { // turning right: lower left servo
                leftServoPosition = baseServoValue - (turretAngleDegrees / maxTurretAngle) * 0.25
                rightServoPosition = baseServoValue
            } else { // turning left: lower right servo
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
