package org.Cryptic.Subsystems;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ColorSensor;

import android.graphics.Color;


// https://docs.revrobotics.com/rev-crossover-products/sensors/color-sensor/application-examples#onbot-java

import org.Cryptic.Subsystem;

public class Intake extends Subsystem {

    private static final double CPR = 145.6; // change later check with gobilda specs

    public DcMotorEx bandMotor;
    public Servo indexServo;

    public ColorSensor colorSensor;

    @Override
    public void init(LinearOpMode opmode) throws InterruptedException {
        colorSensor = opmode.hardwareMap.get(ColorSensor.class, "colorSensor");


        bandMotor = opmode.hardwareMap.get(DcMotorEx.class, "bandMotor");
        indexServo = opmode.hardwareMap.get(Servo.class, "indexServo");
        indexServo.setPosition(0.0);

        bandMotor.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
    }

    public void rotateToVacantSpot() {
        int step = 0;
        while (this.robot.currentBalls[this.robot.currentIndex] != -1 && step < 3) {
            step++;
            this.robot.currentIndex = (this.robot.currentIndex + 1) % 3;
        }

        bandMotor.setVelocity(500 * CPR / 60.0);
        indexServo.setPosition(this.robot.currentIndex / 3.0 * 10/14);
        // bandMotor.setVelocity(0);
        // CHECK THAT INDEXSERVO CAN MOVE
        // dont need to set power for indexServo
    }

    public void scanBallColor() {
        boolean isGreen = false;
        // NormalizedRGBA res = colorSensor.getNormalizedColors();
        float[] hsv = new float[3];
        Color.RGBToHSV((int) (colorSensor.red()), (int) (colorSensor.green()), (int) (colorSensor.blue()), hsv);
        // only hue, hsv[0], matters
        int purpleHue = 270;
        int greenHue = 120;
        if (Math.abs(hsv[0] - purpleHue) > Math.abs(hsv[0] - greenHue)) {
            isGreen = true;
        }
        this.robot.currentBalls[this.robot.currentIndex] = (isGreen ? 1 : 0);
    }

    public void intakeBall(double rpm) { // color sensor is used
        double ticksPerSecond = rpm * CPR / 60.0;
        bandMotor.setVelocity(ticksPerSecond);
    }

    // overall method
    public void intakeComplete(double rpm) { // color sensor is used
        intakeBall(rpm);
        scanBallColor();
        rotateToVacantSpot();
    }

}
