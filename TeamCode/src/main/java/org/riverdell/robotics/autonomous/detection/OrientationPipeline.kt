package org.riverdell.robotics.autonomous.detection

import android.util.Size
import com.acmerobotics.dashboard.FtcDashboard
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import io.liftgate.robotics.mono.subsystem.AbstractSubsystem
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName
import org.firstinspires.ftc.vision.VisionPortal
import org.riverdell.robotics.HypnoticRobot

/**
 * A simplified vision pipeline focused solely on detecting the orientation
 * of a colored object from an overhead camera view.
 *
 * When an angle is detected, this pipeline calls:
 *     robot.intake.setWrist(angle/355)
 *
 * @param opMode the current LinearOpMode instance.
 * @param robot  the robot instance containing the intake system.
 */
class OrientationPipeline(
    private val opMode: LinearOpMode
) : AbstractSubsystem()
{

    lateinit var portal: VisionPortal
    lateinit var detectionPipeline: OrientationDetectionPipeline

    var paused = false

    override fun start() {
        // No additional start logic needed.
    }

    override fun periodic() {
        if (!paused) {
            // Retrieve the detected angle (in degrees) from the pipeline.
            val angle = detectionPipeline.getDetectedAngle()
            if (angle != null) {
                // Call the intake function with the normalized angle.
            }
        }
    }

    override fun doInitialize() {
        detectionPipeline = OrientationDetectionPipeline()
        portal = VisionPortal.Builder()
            .setCamera(opMode.hardwareMap["webcam"] as WebcamName)
            .setCameraResolution(Size(1280, 960))
            .enableLiveView(false)
            .setAutoStopLiveView(true)
            .addProcessors(detectionPipeline)
            .setStreamFormat(VisionPortal.StreamFormat.MJPEG)
            .build()

        FtcDashboard.getInstance().startCameraStream(detectionPipeline, 30.0)
        pause()
    }

    fun pause() {
        portal.setProcessorEnabled(detectionPipeline, false)
        portal.stopStreaming()
        portal.stopLiveView()
        paused = true
    }

    fun resume() {
        portal.setProcessorEnabled(detectionPipeline, true)
        if (portal.cameraState != VisionPortal.CameraState.ERROR) {
            portal.resumeStreaming()
            portal.resumeLiveView()
        }
        paused = false
    }

    override fun dispose() {
        portal.close()
    }
}
