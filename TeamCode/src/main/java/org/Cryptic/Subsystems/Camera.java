package org.Cryptic.Subsystems;

import android.util.Size;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.Cryptic.Subsystem;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraName;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
import org.firstinspires.ftc.vision.opencv.ImageRegion;
Apr

public class Camera extends Subsystem {

    public static AprilTagProcessor colorSensor;

    public void init(LinearOpMode opMode) {
        colorSensor = new Apri.Builder()
            .setRoi(ImageRegion.asUnityCenterCoordinates(-0.1, 0.1, 0.1, -0.1))
            .setSwatches(
                    PredominantColorProcessor.Swatch.WHITE,
                    PredominantColorProcessor.Swatch.GREEN,
                    PredominantColorProcessor.Swatch.PURPLE)
            .build();
        VisionPortal portal = new VisionPortal.Builder()
                .addProcessor(colorSensor)
                .setCameraResolution(new Size(320, 240))
                .setCamera(opMode.hardwareMap.get(WebcamName.class, "Webcam 1"))
                .build();
    }

    public int getMotif() {
        PredominantColorProcessor.Result res = colorSensor.getAnalysis();
    }
}
