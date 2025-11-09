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

    public int[] currentBalls;
    public int[] motifBalls;

    public DcMotorEx bandMotor;
    public Servo indexServo;

    public NormalizedColorSensor colorSensor;

    // add color sensor

    public static double currentServoPos = 0.0;

    @Override
    public void init(LinearOpMode opmode) throws InterruptedException {
        colorSensor = opmode.hardwareMap.get(NormalizedColorSensor.class, "colorSensor");



        bandMotor = opmode.hardwareMap.get(DcMotorEx.class, "bandMotor");
        indexServo = opmode.hardwareMap.get(Servo.class, "indexSero");

        bandMotor.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);

    }



    public void grabBall(double power) {



    }

    public void registerBall() {// color sensor to be used


        //int currentColor =


        if (currentServoPos >= 0.3) {
            currentServoPos = 0.0;
        } else {
            currentServoPos += (1.0 / 3.0);
        }

        indexServo.setPosition(currentServoPos);
    }



    public void indexBalls(int[] currentBalls, int[] motifBalls) {





    }


}
