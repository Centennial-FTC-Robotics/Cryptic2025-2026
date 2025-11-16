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

    public DcMotorEx rotateMotor; // turret rotation motor
    public Servo angleServo;
    public DcMotorEx powerMotor;  // shooter flywheel

    public int motif;
    public int[] order; // TODO

    double tx = -72.0; // x location of field goal
    double ty = -72.0; // y location of field goal

    double height = 36.0; // height of goal
    double radius = 5.0;  // radius of ball (be consistent with your units)


    private static final double CPR = 500.0; // change later check with gobilda specs






    @Override
    public void init(LinearOpMode opmode) throws InterruptedException {
        powerMotor = opmode.hardwareMap.get(DcMotorEx.class, "powerMotor");
        angleServo = opmode.hardwareMap.get(Servo.class, "angleServo");
        rotateMotor = opmode.hardwareMap.get(DcMotorEx.class, "rotateMotor");

        powerMotor.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        rotateMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);



        rotateMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rotateMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        powerMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        powerMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }



    public void autoUpdateAim(MecanumDrive Drive) {

        // tx = -72.0; // target x position change later
        // ty = -72.0; // target y position change later

        Drive.updatePoseEstimate();

        Pose2d currentPos = Drive.localizer.getPose();

        double dx = tx - currentPos.position.x;

        double dy = ty - currentPos.position.y;

        Rotation2d heading = currentPos.heading;
        double headingRad = heading.log(); // radians

        double dist = Math.hypot(dx, dy);
        double launchAngle = Math.atan2((2 * height), dist);



        angleServo.setPosition((launchAngle / Math.toRadians(90.0))); // test later

        int turretEncoderPosition = rotateMotor.getCurrentPosition();


        double turretAngleRad = encoderToRadians(turretEncoderPosition);

    }

    // via christian, the range of motion is pi one way pi the other
    // this returns a value between pi and -pi then (once CPR is correct)
    public double encoderToRadians(double encoderValue) {
        return (encoderValue / CPR) * 2 * Math.PI;
    }

    public int radiansToEncoder(double radians) {
        double CPR = 537.6; // must match above

        double revolutions = radians / (2 * Math.PI);
        return (int) (CPR * revolutions);
    }

    public void launch(MecanumDrive Drive) { // using georgy's math

        Drive.updatePoseEstimate();

        Pose2d currentPos = Drive.localizer.getPose();


        double robotAngle = Drive.localizer.getPose().heading.log(); // radians
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



        // want to rotate turret to targetAngle
        rotateMotor.setTargetPosition(radiansToEncoder(targetAngle));

        rotateMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rotateMotor.setPower(0.4);

        // we should prob tune power

        double dist = Math.hypot(dx, dy);
        double launchAngle = Math.atan2((2 * height), dist);


        double rpm = (30 / (Math.PI * radius)) * Math.sqrt(2 * 9.81 * height) / Math.sin(launchAngle);




        double ticksPerSecond = rpm * CPR / 60.0;
        powerMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        powerMotor.setVelocity(ticksPerSecond); // not seyting up manual pid for now ...



    }
}
