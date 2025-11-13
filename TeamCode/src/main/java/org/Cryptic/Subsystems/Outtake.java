package org.Cryptic.Subsystems;
import com.acmerobotics.roadrunner.Pose2d;

import com.acmerobotics.roadrunner.Rotation2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

import org.Cryptic.Subsystem;
import org.firstinspires.ftc.teamcode.MecanumDrive;

public class Outtake extends Subsystem {

    public DcMotorEx rotateMotor; // TODO christian is this for angling turret im assuming it is
    public Servo angleServo;
    public DcMotorEx powerMotor;

    public int motif;
    public int[] order; // TODO

    double tx = -72.0; // x location of field goal
    double ty = -72.0; // y location of field goal

    double height = 36.0; // height of goal
    double radius = 5.0; // radius of ball

    @Override
    public void init(LinearOpMode opmode) throws InterruptedException {
        // TODO
        powerMotor = opmode.hardwareMap.get(DcMotorEx.class, "powerMotor");
        angleServo = opmode.hardwareMap.get(Servo.class, "angleServo");
        rotateMotor = opmode.hardwareMap.get(DcMotorEx.class, "rotateMotor");

        powerMotor.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        rotateMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    public void autoUpdateAim(MecanumDrive Drive) {

        // tx = -72.0; // target x position change later
        // ty = -72.0; // target y position change later

        Drive.updatePoseEstimate();

        Pose2d currentPos = Drive.localizer.getPose();


        double dx = tx - currentPos.position.x;
        double dy = ty - currentPos.position.y;

        Rotation2d heading = currentPos.heading;


        double dist = Math.hypot(dx,dy);
        double launchAngle = Math.atan2((2 * height),dist);

        // set launch angle

        angleServo.setPosition((launchAngle / 90.0)); // test later


        // rotate motor
        // something to note, motor cannot rotate past a certain fixed degree (eg. 180) so if you wanted to go 181, you would have to go the other way
        // first get motor position using encoder





    }

    // via christian, the range of motion is pi one way pi the other
    // TODO how the heck do you program to turn the other way
    // this returns a value between pi and -pi then
    public double encoderToRadians(double encoderValue) {
        // TODO calibrate? or counts per revolution
        double CPR = 10.0;
        return encoderValue/CPR * 2 * Math.PI;
    }

    public int radiansToEncoder(double radians) {
        double CPR = 10.0;
        double revolutions = radians / (2 * Math.PI);
        return (int) (CPR * revolutions);
    }

    public void launch(MecanumDrive Drive) { // using georgy's math

        Drive.updatePoseEstimate();

        // angling turret to point to goal
        Pose2d currentPos = Drive.localizer.getPose();
        double robotAngle = Drive.localizer.getPose().heading.log();
        double turretAngle = rotateMotor.getCurrentPosition();
        turretAngle = encoderToRadians(turretAngle);

        double dx = tx - currentPos.position.x;
        double dy = ty - currentPos.position.y;
        // tan(robotAngle+turretAngle) = dy/dx
        double targetAngle = Math.atan2(dy, dx);
        // correct if targetAngle is negative of what it should be
        if (Math.sin(targetAngle) * dy < 0) {
            targetAngle = -targetAngle;
        }
        targetAngle -= robotAngle;
        // want to rotate turret to targetAngle; either targetAngle clockwise or 2pi-targetAngle counterclockwise
        // the signs should be taken care of
        rotateMotor.setTargetPosition(radiansToEncoder(targetAngle));


        // angling launching mechanism
        double dist = Math.hypot(dx,dy);

        double launchAngle = Math.atan2((2 * height),dist);

        double rpm = (30 / (Math.PI * radius)) * Math.sqrt(2 * 9.81 * height) / Math.sin(launchAngle);


        // TODO using encoders set motor to use specificed RPM

    }


}
