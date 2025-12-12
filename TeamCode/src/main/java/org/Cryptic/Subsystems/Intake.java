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

    private static final double CPR = 145.6; // change later check with gobilda specs

    public DcMotorEx bandMotor;
    public Servo indexServo;

    public NormalizedColorSensor colorSensor;

    @Override
    public void init(LinearOpMode opmode) throws InterruptedException {
        colorSensor = opmode.hardwareMap.get(NormalizedColorSensor.class, "colorSensor");


        bandMotor = opmode.hardwareMap.get(DcMotorEx.class, "bandMotor");
        indexServo = opmode.hardwareMap.get(Servo.class, "indexServo");

        bandMotor.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);

        this.robot.currentIndex = 0;
        this.robot.currentBalls = new int[3];
        for (int i=0; i<3; ++i) this.robot.currentBalls[i] = -1;
    }



    public void indexIndexer() { // this is just half of the grabBall method.
        // basically, the way we are using grabBall, it always indexes everytime we click right trigger (and run grab ball) even if we miss
        // so if u look in TestTeleop, maybe we can auto index everything (based on motif too) just by clicing a button

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


        this.robot.currentBalls[this.robot.currentIndex] = (isGreen ? 1 : 0);
        // note that the one just added is the current index now

    }


    public void intaker(double rpm) { // color sensor is used

        double ticksPerSecond = rpm * CPR / 60.0;

        bandMotor.setVelocity(ticksPerSecond);

    }

    public void grabBall(double rpm) { // color sensor is used
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

        int step = 0;
        while (this.robot.currentBalls[this.robot.currentIndex] != -1 && step < 3) {
            step++;
            this.robot.currentIndex = (this.robot.currentIndex + 1) % 3;
        }
        if (step == 3) {
            System.out.println("There are no available slots");
        }
        indexServo.setPosition(this.robot.currentIndex / 3.0);

        // TODO actually control bandMotor to get ball
        double ticksPerSecond = rpm * CPR / 60.0;

        bandMotor.setVelocity(ticksPerSecond);

        this.robot.currentBalls[this.robot.currentIndex] = (isGreen ? 1 : 0);
        // note that the one just added is the current index now
    }



}
