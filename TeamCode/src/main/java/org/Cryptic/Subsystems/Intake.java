package org.Cryptic.Subsystems;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;

import android.graphics.Color;


// https://docs.revrobotics.com/rev-crossover-products/sensors/color-sensor/application-examples#onbot-java

import org.Cryptic.Subsystem;

public class Intake extends Subsystem {


    public DcMotorEx bandMotor;
    public Servo indexServo;

    public NormalizedColorSensor colorSensor;

    public int currentIndex;
    public int[] currentBalls;

    public static double currentServoPos = 0.0;

    @Override
    public void init(LinearOpMode opmode) throws InterruptedException {
        colorSensor = opmode.hardwareMap.get(NormalizedColorSensor.class, "colorSensor");


        bandMotor = opmode.hardwareMap.get(DcMotorEx.class, "bandMotor");
        indexServo = opmode.hardwareMap.get(Servo.class, "indexSero");

        bandMotor.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);

        currentIndex = 0;
        currentBalls = new int[3];

        this.robot.currentIndex = currentIndex;
        this.robot.currentBalls = currentBalls;
    }

    public void grabBall(double power) {

    }

    public void registerBall() {// color sensor is used

        boolean isGreen;

        NormalizedRGBA res = colorSensor.getNormalizedColors();
        float[] hsv = new float[3];
        Color.RGBToHSV((int) (res.red * 256), (int) (res.green * 256), (int) (res.blue * 256), hsv);
        // only hue, hsv[0], matters
        int purpleHue = 270;
        int greenHue = 120;
        if (Math.abs(hsv[0] - purpleHue) > Math.abs(hsv[0] - greenHue)) {
            isGreen = true;
        } else {
            isGreen = false;
        }

        if (currentServoPos >= 0.3) {
            currentServoPos = 0.0;
            currentIndex = 0;
        } else {
            currentServoPos += (1.0 / 3.0);
            currentIndex++;
        }

        indexServo.setPosition(currentServoPos);
        currentBalls[currentIndex] = (isGreen ? 1 : 0);
    }

}
