package org.riverdell.robotics.autonomous.detection;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import org.firstinspires.ftc.vision.VisionProcessor;
import org.firstinspires.ftc.robotcore.external.stream.CameraStreamSource;
import org.firstinspires.ftc.robotcore.internal.camera.calibration.CameraCalibration;
import org.firstinspires.ftc.robotcore.external.function.Continuation;
import org.firstinspires.ftc.robotcore.external.function.Consumer;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.RotatedRect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import java.util.ArrayList;

public class OrientationDetectionPipeline implements CameraStreamSource, VisionProcessor {
    // Color thresholds for detecting a yellow object (adjust as needed)
    private final Scalar lowerBound = new Scalar(20, 100, 100);
    private final Scalar upperBound = new Scalar(30, 255, 255);

    private final Mat hsvMat = new Mat();
    private final Mat mask = new Mat();
    private final Mat processedMat = new Mat();

    // Detected angle (in degrees) or null if no valid object is found
    private Double detectedAngle = null;

    private Bitmap lastFrame;

    public OrientationDetectionPipeline() {
        // Constructor can remain empty.
    }

    @Override
    public void init(int width, int height, CameraCalibration calibration) {
        lastFrame = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
    }

    @Override
    public Object processFrame(Mat input, long captureTimeNanos) {
        detectedAngle = null; // Reset angle for each frame

        // Convert the image from RGB to HSV color space.
        Imgproc.cvtColor(input, hsvMat, Imgproc.COLOR_RGB2HSV);

        // Threshold the HSV image to obtain a mask for the yellow color.
        Core.inRange(hsvMat, lowerBound, upperBound, mask);

        // Apply morphological operations to reduce noise.
        Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new org.opencv.core.Size(5, 5));
        Imgproc.morphologyEx(mask, processedMat, Imgproc.MORPH_OPEN, kernel);
        Imgproc.morphologyEx(processedMat, processedMat, Imgproc.MORPH_CLOSE, kernel);

        // Find contours in the processed mask.
        ArrayList<MatOfPoint> contours = new ArrayList<>();
        Imgproc.findContours(processedMat, contours, new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

        double minArea = 500;    // Minimum area threshold (adjust as needed)
        double maxArea = 100000; // Maximum area threshold
        double largestArea = 0;
        MatOfPoint largestContour = null;
        for (MatOfPoint contour : contours) {
            double area = Imgproc.contourArea(contour);
            if (area > minArea && area < maxArea && area > largestArea) {
                largestArea = area;
                largestContour = contour;
            }
        }

        if (largestContour != null) {
            // Compute the minimum area rotated rectangle for the largest contour.
            MatOfPoint2f contour2f = new MatOfPoint2f(largestContour.toArray());
            RotatedRect rect = Imgproc.minAreaRect(contour2f);
            double angle = rect.angle;
            // Adjust the angle: if the width is less than height, add 90 degrees.
            if (rect.size.width < rect.size.height) {
                angle += 90;
            }
            detectedAngle = angle;
            // Annotate the input image with the detected angle.
            Imgproc.putText(input, "Angle: " + String.format("%.1f", angle) + " deg",
                    new Point(rect.center.x - 50, rect.center.y - 10),
                    Imgproc.FONT_HERSHEY_SIMPLEX, 1.5, new Scalar(0, 0, 0), 2);
            // Optionally, draw the rotated rectangle.
            Point[] points = new Point[4];
            rect.points(points);
            for (int i = 0; i < 4; i++) {
                Imgproc.line(input, points[i], points[(i + 1) % 4], new Scalar(0, 255, 0), 2);
            }
        }

        // Convert the annotated frame to a Bitmap for live streaming.
        Utils.matToBitmap(input, lastFrame);
        return input;
    }

    /**
     * Returns the detected angle (in degrees) or null if no object was detected.
     */
    public Double getDetectedAngle() {
        return detectedAngle;
    }

    @Override
    public void getFrameBitmap(Continuation<? extends Consumer<Bitmap>> continuation) {
        continuation.dispatch(bitmapConsumer -> bitmapConsumer.accept(lastFrame));
    }

    @Override
    public void onDrawFrame(Canvas canvas, int onscreenWidth, int onscreenHeight, float scaleBmpPxToCanvasPx, float scaleCanvasDensity, Object userContext) {
        // Optional: Implement additional drawing if needed.
    }
}
