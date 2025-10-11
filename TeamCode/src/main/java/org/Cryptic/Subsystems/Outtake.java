package org.Cryptic.Subsystems;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

import org.Cryptic.Subsystem;

public class Outtake extends Subsystem {

    public Servo angleServo;
    public DcMotorEx powerMotor;

    @Override
    public void init(LinearOpMode opmode) throws InterruptedException {
        // TODO
        powerMotor = opmode.hardwareMap.get(DcMotorEx.class, "powerMotor");
        angleServo = opmode.hardwareMap.get(Servo.class, "angleServo");

    }

    public void outtakeSample () {
        // TODO
    }
}
