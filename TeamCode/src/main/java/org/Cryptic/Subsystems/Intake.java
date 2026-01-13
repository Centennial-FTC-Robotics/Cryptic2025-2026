package org.Cryptic.Subsystems;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
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

    public DcMotorEx encoder;
    int spindexerStep = 0;

    public double bandSpeed = 0;

    @Override
    public void init(LinearOpMode opmode) throws InterruptedException {
        colorSensor = opmode.hardwareMap.get(ColorSensor.class, "colorSensor");


        bandMotor = opmode.hardwareMap.get(DcMotorEx.class, "bandMotor");
        indexServo = opmode.hardwareMap.get(Servo.class, "indexServo");

        encoder = opmode.hardwareMap.get(DcMotorEx.class, "spinEncoder");
        // encoder.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        // encoder.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        bandMotor.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        indexServo.setPosition(0.5);
    }

    public void rotateToVacantSpot() {
        int step = 0;
        while (this.robot.currentBalls[this.robot.currentIntakeIndex] != -1 && step < 3) {
            step++;
            this.robot.currentIntakeIndex = (this.robot.currentIntakeIndex + 1) % 3;
        }
        bandSpeed = 500 * CPR / 60.0;
        bandMotor.setVelocity(bandSpeed);
        if (robot.rotatingIntake) encoderSpin(this.robot.currentIntakeIndex*2);
        bandSpeed = 0;
        if (!robot.rotatingIntake) bandMotor.setVelocity(bandSpeed);
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
        this.robot.currentBalls[this.robot.currentIntakeIndex] = (isGreen ? 1 : 0);
    }

    public void intakeBall(double rpm) { // color sensor is used
        bandSpeed = rpm * CPR / 60.0;
        bandMotor.setVelocity(bandSpeed);

    }

    // overall method
    public void intakeComplete(double rpm) { // color sensor is used
        intakeBall(rpm);
        scanBallColor();
        rotateToVacantSpot();
    }

    public void intakeAuto(double rpm) {
        intakeBall(rpm);
    }

    public void scanSpinAuto() {
        //bandSpeed = 0
//        bandMotor.setVelocity(bandSpeed);
        scanBallColor();
        rotateToVacantSpotAuto();
    }

    public void stopSpinAuto() {
        bandSpeed = 0.0;
        bandMotor.setVelocity(bandSpeed);
    }

    public void encoderSpin(int pos) {
        if (!robot.rotatingIntake) {
            indexServo.setPosition(0.5);
        }

        int current = encoder.getCurrentPosition();
        int error;
        bandSpeed = 100 * CPR / 60.0;
        bandMotor.setVelocity(bandSpeed);
        if (pos > this.robot.currentIndex) {
            // our goal is positive
            error = this.robot.targetPosition[pos] - current; // always positive, unless going from

            if (error <= 0) {
                spindexerStep++;
                indexServo.setPosition(0.5); // stop
                this.robot.rotating = false;
                this.robot.rotatingIntake = false;
                this.robot.currentIndex = pos;
            } else {
                double power = Math.max(robot.SPINDEXER_MIN_SPEED, error / robot.SPINDEXER_SPEED);
                indexServo.setPosition(0.5 + power * 0.5);
            }
        } else if (pos < this.robot.currentIndex) {
            // our goal is negative
            error = current - this.robot.targetPosition[pos];
            if (error <= 0) {
                spindexerStep++;
                indexServo.setPosition(0.5); // stop
                this.robot.rotating = false;
                this.robot.rotatingIntake = false;
                this.robot.currentIndex = pos;
            } else {
                double power = Math.max(robot.SPINDEXER_MIN_SPEED, error / robot.SPINDEXER_SPEED);
                indexServo.setPosition(0.5 - power * 0.5);
            }
        } else {
            this.robot.rotating = false;
            this.robot.rotatingIntake = false;
        }
    }

    // random ahh auto implementation for indexing
    // in Actions.runBlocking(), you run until false is returned for auto
    // trying to mimick the telop
    public void rotateToVacantSpotAuto() {
        int step = 0;
        while (this.robot.currentBalls[this.robot.currentIntakeIndex] != -1 && step < 3) {
            step++;
            this.robot.currentIntakeIndex = (this.robot.currentIntakeIndex + 1) % 3;
        }

//        if (robot.rotatingIntake) encoderSpin(this.robot.currentIntakeIndex*2);
//        if (!robot.rotatingIntake) bandMotor.setVelocity(0);
        robot.rotatingIntake = true;
        while (robot.rotatingIntake) {
            encoderSpin(this.robot.currentIntakeIndex*2);
        }
//        bandSpeed = 0;
//        bandMotor.setVelocity(bandSpeed);
    }

    public void update() {
        bandMotor.setVelocity(bandSpeed);
    }
}
