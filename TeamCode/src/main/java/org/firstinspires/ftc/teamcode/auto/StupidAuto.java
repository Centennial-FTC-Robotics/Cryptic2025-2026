package org.firstinspires.ftc.teamcode.auto;

import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

import org.Cryptic.Robot;
import org.Cryptic.Subsystems.Outtake;

@Autonomous(name = "StupidAuto")
public class StupidAuto extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {

        Robot robot = new Robot();
        robot.initialize(this);

        DcMotorEx bandMotor = hardwareMap.get(DcMotorEx.class, "bandMotor");
        bandMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        ColorSensor colorSensor = hardwareMap.get(ColorSensor.class, "colorSensor");

        Servo indexServo = hardwareMap.get(Servo.class, "indexServo");
        Servo transferServo = hardwareMap.get(Servo.class, "transferServo");
        Servo angleServo = hardwareMap.get(Servo.class, "angleServo");

        DcMotorEx powerMotor = hardwareMap.get(DcMotorEx.class, "powerMotor");

        // double ANGLE_ONE = 0.65;

        int DRIVE_TIME_MS = 1000;

        int INDEX_SETTLE_MS = 1000;     // give index servo time to actually move
        int FLYWHEEL_SPINUP_MS = 1000;  // time to reach speed before feeding
        int TRANSFER_UP_MS = 1000;      // how long transfer stays up to feed/shoot
        int TRANSFER_DOWN_MS = 1000;    // time to retract + let ball clear

        waitForStart();
        if (isStopRequested()) return;

        // indexServo.setPosition(0.0);
        robot.intake.encoderSpin(5);
        transferServo.setPosition(Outtake.rest);
        angleServo.setPosition(0.0);

        sleep(200);

        robot.dt.drivebase.setDrivePowers(new PoseVelocity2d(
                new Vector2d(-1, 0),  // keep your sign; flip if wrong direction
                0
        ));
        sleep(DRIVE_TIME_MS);
        robot.dt.drivebase.setDrivePowers(new PoseVelocity2d(new Vector2d(0, 0), 0));
        sleep(150);

        powerMotor.setPower(-1.0); // might need to adjust
        sleep(FLYWHEEL_SPINUP_MS);

        sleep(INDEX_SETTLE_MS);

        transferServo.setPosition(Outtake.liftUp);
        sleep(TRANSFER_UP_MS);

        transferServo.setPosition(Outtake.rest);
        sleep(TRANSFER_DOWN_MS);

        // indexServo.setPosition(0.5);
        robot.intake.encoderSpin(3);
        sleep(INDEX_SETTLE_MS);

        transferServo.setPosition(Outtake.liftUp);
        sleep(TRANSFER_UP_MS);

        transferServo.setPosition(Outtake.rest);
        sleep(TRANSFER_DOWN_MS);

        // indexServo.setPosition(0.84);
        robot.intake.encoderSpin(1);
        sleep(INDEX_SETTLE_MS);

        transferServo.setPosition(Outtake.liftUp);
        sleep(TRANSFER_UP_MS);

        transferServo.setPosition(Outtake.rest);
        sleep(TRANSFER_DOWN_MS);

        powerMotor.setPower(0.0);
        sleep(100);

        robot.dt.drivebase.setDrivePowers(new PoseVelocity2d(
                new Vector2d(0, -1),
                0
        ));
        sleep(1000);
        robot.dt.drivebase.setDrivePowers(new PoseVelocity2d(new Vector2d(0, 0), 0)); // STOP
    }
}

