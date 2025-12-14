package org.Cryptic.Subsystems;

import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

import org.Cryptic.Subsystem;
import org.firstinspires.ftc.teamcode.MecanumDrive;
import org.firstinspires.ftc.vision.apriltag.AprilTagPoseFtc;

public class Outtake extends Subsystem {
    public DcMotorEx rotateMotor; // turret rotation motor
    public Servo angleServo;

    public Servo indexServo;

    public Servo transferServo;

    public static final double liftUp = 0.3; // TODO

    public static final double rest = 0.55;

    public DcMotorEx powerMotor;  // shooter flywheel

    double height = 54; // height of back goalpost
    double radius = 5.0;  // radius of ball (be consistent with your units)

    private static final double CPR = 145.6 ; // https://www.gobilda.com/content/spec_sheets/5202-2402-0005_spec_sheet.pdf

    @Override
    public void init(LinearOpMode opmode) throws InterruptedException {
        powerMotor = opmode.hardwareMap.get(DcMotorEx.class, "powerMotor");
        angleServo = opmode.hardwareMap.get(Servo.class, "angleServo");
        rotateMotor = opmode.hardwareMap.get(DcMotorEx.class, "rotateMotor");
        indexServo = opmode.hardwareMap.get(Servo.class, "indexServo");
        transferServo = opmode.hardwareMap.get(Servo.class, "transferServo");

        powerMotor.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        rotateMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        rotateMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rotateMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        rotateMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE); // take away later


        powerMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        powerMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        this.robot.targetIndex = 0;

        transferServo.setPosition(rest); // TODO
    }

    // helper method that properly angles the angleServo to theta
    // where theta is such that
    /*     /    ^ (height)  \
    *     /                  \
    *    /                    \
    *   / (angle here theta)   \
    *  /______(this length dist)\
     */
    public void aimAngleServo(double dist) {
        double launchAngle = Math.atan2((2 * height), dist);

        double servoPosition = launchAngle / Math.PI; // maps [0, π] to [0, 1]
        servoPosition = Math.max(0.0, Math.min(1.0, servoPosition));

        angleServo.setPosition(servoPosition); // test later
    }

    // this code makes it so that turret is angled birds-eye view
    /*     * (if this is target which is dx,dy away)
    *    /
    *   /
    *  /
    * / (this is how turret should be pointing)
     */
    public void aimRotateMotor(double dx, double dy, MecanumDrive drive) {
        double robotAngle = drive.localizer.getPose().heading.log(); // radians
        // double turretAngle = rotateMotor.getCurrentPosition();
        // turretAngle = encoderToRadians(turretAngle);
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
    }

    // this angles the turret so that in birds eye viw we're aiming the goal
    // and that angleServo angles it so that the ball follows the right path
    public void autoUpdateAim(double tx, double ty, MecanumDrive Drive) {
        Drive.updatePoseEstimate();
        Pose2d currentPos = Drive.localizer.getPose();

        double dx = tx - currentPos.position.x, dy = ty - currentPos.position.y;
        double dist = Math.hypot(dx, dy) - 3;

        aimAngleServo(dist);
        aimRotateMotor(dx, dy, Drive);
    }

    // via christian, the range of motion is pi one way pi the other
    // this returns a value between pi and -pi then (once CPR is correct)
    // for this to work, turrent encoder 0 should be aligned with "robot facing forward"
    public double encoderToRadians(double encoderValue) {
        return (encoderValue / CPR) * 2 * Math.PI;
    }

    public int radiansToEncoder(double radians) {
        double revolutions = radians / (2 * Math.PI);
        return (int) (CPR * revolutions);
    }

    public void manuallyUpdateAim(double rpm) { // if auto update fails, click left bumper to
        rotateMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        double ticksPerSecond = rpm * CPR / 60.0;
        rotateMotor.setVelocity(ticksPerSecond);
    }

    public void aimRotateMotorAprilTag(boolean blueTeam) {
        int aprilTagId = blueTeam ? 20 : 24;
        AprilTagPoseFtc offset = robot.camera.getGoalOffset(blueTeam);
        // . <--> apriltag --> offset.x
        // distance from camera to apriltag --> offset.y
        double angleToMove = Math.atan2(offset.x, offset.y);
        double position = encoderToRadians(rotateMotor.getCurrentPosition()) + angleToMove;
        rotateMotor.setTargetPosition(radiansToEncoder(position));
        rotateMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rotateMotor.setPower(0.4);
    }

    public void executeLaunchSpeed(double dist) {
        double launchAngle = Math.atan2(2*height, dist);
        double vel = Math.sqrt(2.0 * 9.81 * height) / Math.sin(launchAngle);
        double rpm = (30.0 / (Math.PI * radius)) * vel;
        double ticksPerSecond = rpm * CPR / 60.0;

        powerMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        powerMotor.setVelocity(ticksPerSecond);
    }

    // returns index of the ball shot
    public void prepareBallShot() {
        // 21 for GPP, 22 for PGP, 23 for PPG
        int target = ((this.robot.motif - 21) == robot.targetIndex) ? 1 : 0;
        int step = 0;
        int shootIndex = 0;
        while (this.robot.currentBalls[shootIndex] != target && step < 3) {
            step++;
            shootIndex = (shootIndex + 1) % 3;
        }
        double pos = shootIndex / 3.0 + 0.5; if (pos > 1) pos = pos - 1;
        this.robot.currentBalls[shootIndex] = -1;

        indexServo.setPosition(pos);
        // TODO does bandMotor need to move as well?

    }

    // to be used in conjunction with aimRotateMotorAprilTag
    // modified so that it will launch all balls it can
    public void launchAprilTag(boolean blueTeam) {
        AprilTagPoseFtc offset = robot.camera.getGoalOffset(blueTeam);
        if (offset == null) {
            // no tag detected  then don't try to shoot
            return;
        }
        double horizontalDist = offset.y - 3.0;

        int numBalls = 3;
        while (numBalls > 0) {
            for (int i=0; i<3; ++i) if (this.robot.currentBalls[i] != -1) numBalls++;

            prepareBallShot();
            aimRotateMotorAprilTag(blueTeam);
            aimAngleServo(horizontalDist);
            executeLaunchSpeed(horizontalDist);

            this.robot.targetIndex = (this.robot.targetIndex + 1) % 3;
            transferServo.setPosition(rest);
        }

        this.robot.currentIndex = 0;
        indexServo.setPosition(0.0);
    }



    // tx is x location of field goal and ty is y location
    public void launch(double tx, double ty, MecanumDrive Drive) { // using georgy's math
        int numBalls = 3;
        while (numBalls > 0) {
            for (int i=0; i<3; ++i) numBalls += (this.robot.currentBalls[i] == -1 ? 0 : 1);
            prepareBallShot();

            Drive.updatePoseEstimate();
            Pose2d currentPos = Drive.localizer.getPose();
            double dx = tx - currentPos.position.x;
            double dy = ty - currentPos.position.y;
            // we should prob tune power
            double dist = Math.hypot(dx, dy) - 3;

            prepareBallShot();
            transferServo.setPosition(liftUp); // TODO
            aimRotateMotor(dx, dy, Drive);
            aimAngleServo(dist);
            executeLaunchSpeed(dist);
            this.robot.targetIndex = (this.robot.targetIndex + 1) % 3;
        }

        this.robot.currentIndex = 0;
        indexServo.setPosition(0.0);
    }
}
