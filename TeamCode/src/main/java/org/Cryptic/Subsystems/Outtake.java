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

    public DcMotorEx bandMotor;

    public DcMotorEx encoder;
    public static final double liftUp = 0.29; // TODO

    public static final double rest = 0.5;

    public DcMotorEx powerMotor;  // shooter flywheel

    double height = 54; // height of back goalpost
    double radius = 5.0;  // radius of ball (be consistent with your units)

    int spindexerStep = 0;

    public boolean transferUp = false;

    private static final double CPR_ROTATE = 145.6 * 40/7; // https://www.gobilda.com/content/spec_sheets/5202-2402-0005_spec_sheet.pdf
    public static final double CPR_LAUNCH = 145.6;
    public static final double CPR_BAND = 145.6;
    // 28 to 160

    public static final double LAUNCH_ANGLE = 47.52; // DEGREES
    public static final double LAUNCH_POS = 0.2;

    @Override
    public void init(LinearOpMode opmode) throws InterruptedException {
        powerMotor = opmode.hardwareMap.get(DcMotorEx.class, "powerMotor");
        angleServo = opmode.hardwareMap.get(Servo.class, "angleServo");
        rotateMotor = opmode.hardwareMap.get(DcMotorEx.class, "rotateMotor");
        indexServo = opmode.hardwareMap.get(Servo.class, "indexServo");
        transferServo = opmode.hardwareMap.get(Servo.class, "transferServo");
        bandMotor = opmode.hardwareMap.get(DcMotorEx.class, "bandMotor");
        encoder = opmode.hardwareMap.get(DcMotorEx.class, "spinEncoder");

        powerMotor.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        rotateMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        rotateMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rotateMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        rotateMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE); // take away later


        powerMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        powerMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        this.robot.targetIndex = 0;

        transferServo.setPosition(rest);
        angleServo.setPosition(LAUNCH_POS); // 47.52 degrees
    }

    // helper method that properly angles the angleServo to theta
    // where theta is such that
    /*     /    ^ (height)  \
    *     /                  \
    *    /                    \
    *   / (angle here theta)   \
    *  /______(this length dist)\
     */
//    public void aimAngleServo(double dist) {
//        double launchAngle = Math.atan2((2 * height), dist);
//
//        double servoPosition = launchAngle / Math.PI; // maps [0, π] to [0, 1]
//        servoPosition = Math.max(0.0, Math.min(1.0, servoPosition));
//
//        angleServo.setPosition(servoPosition); // TODO test later
//    }

    // this code makes it so that turret is angled birds-eye view
    /*     * (if this is target which is dx,dy away)
    *    /
    *   /
    *  /
    * / (this is how turret should be pointing)
     */
    public void aimRotateMotor(double dx, double dy, MecanumDrive drive) { // difference  between current pos and goal
        double robotAngle = drive.localizer.getPose().heading.toDouble();
        double fieldTargetAngle = Math.atan2(dy, dx);
//
//        int testAngle = 5000;
//        rotateMotor.setTargetPosition(testAngle);
//        rotateMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        // Relative angle the turret needs to be at
        double relativeTargetAngle = fieldTargetAngle - robotAngle;

        // Get current turret angle in radians
        robot.currentTurretRadians = encoderToRadians(rotateMotor.getCurrentPosition());

        // CRITICAL: Find the shortest path (so it doesn't spin 350 degrees)
        double deltaAngle = Math.atan2(Math.sin(relativeTargetAngle - robot.currentTurretRadians),
                Math.cos(relativeTargetAngle - robot.currentTurretRadians));

        double finalTargetRadians = robot.currentTurretRadians + deltaAngle;

        rotateMotor.setTargetPosition(radiansToEncoder(finalTargetRadians));
        rotateMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rotateMotor.setPower(0.2); // TODO testing purposes
    }

    // this angles the turret so that in birds eye viw we're aiming the goal
    // and that angleServo angles it so that the ball follows the right path
    public void autoUpdateAim(double tx, double ty, MecanumDrive Drive) {
        Drive.updatePoseEstimate();
        Pose2d currentPos = Drive.localizer.getPose();

        double dx = tx - currentPos.position.x, dy = ty - currentPos.position.y;
        double dist = Math.hypot(dx, dy) - 3;

        // aimAngleServo(dist);
        aimRotateMotor(dx, dy, Drive);
    }

    // via christian, the range of motion is pi one way pi the other
    // this returns a value between pi and -pi then (once CPR is correct)
    // for this to work, turrent encoder 0 should be aligned with "robot facing forward"
    public double encoderToRadians(double encoderValue) {
        return (encoderValue / CPR_ROTATE) * 2 * Math.PI;
    }

    public int radiansToEncoder(double radians) {
        double revolutions = radians / (2 * Math.PI);
        return (int) (CPR_ROTATE * revolutions);
    }

    public void manuallyUpdateAim(double rpm) { // if auto update fails, click left bumper to
        rotateMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        double ticksPerSecond = rpm * CPR_ROTATE / 60.0;
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
        rotateMotor.setPower(0.2);
    }

    public void executeLaunchSpeed(double dist) {
        // double launchAngle = Math.atan2(2*height, dist);
        // double vel = Math.sqrt(2.0 * 9.81 * height) / Math.sin(launchAngle); units: meters/s;
        double vel = Math.sqrt(386.09 * dist * dist /
                (2 * Math.pow(Math.cos(Math.toRadians(LAUNCH_ANGLE)), 2) *
                        (dist * Math.tan(Math.toRadians(LAUNCH_ANGLE)) - height))); // inches per second
        // in/s * t/rev * rev/in = in/s * 145.6 * 1/(circumference)
        double tps = vel * CPR_LAUNCH * 3.77953;

        powerMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        powerMotor.setVelocity(-tps);
    }

    public void stopFlywheel() {
        powerMotor.setVelocity(0);
    }

    public void moveTransfer() {
        if (transferUp) {
            transferServo.setPosition(rest);
        } else {
            transferServo.setPosition(liftUp);
        }
        transferUp = !transferUp;
    }

    public int calculateOuttakeSlot() {
        int target = ((this.robot.motif - 21) == robot.targetIndex) ? 1 : 0;
        int step = 0;
        int shootIndex = 0;
        while (this.robot.currentBalls[shootIndex] != target && step < 3) {
            step++;
            shootIndex = (shootIndex + 1) % 3;
        }
        // double pos = shootIndex / 3.0 + 0.5; if (pos > 1) pos = pos - 1;
        this.robot.currentBalls[shootIndex] = -1;

        return (shootIndex*2+3)%6;
    }

    // returns index of the ball shot
    public void prepareBallShot() {
        int po = calculateOuttakeSlot();
        bandMotor.setVelocity(500 * CPR_BAND / 60.0);
        if (robot.rotatingOuttake) encoderSpin(po); // since pos is all 6 intake/outtake positions
        if (!robot.rotatingOuttake) bandMotor.setVelocity(0.0);
    }

    public void prepareBallShotAuto() {
        int po = calculateOuttakeSlot();
        bandMotor.setVelocity(500 * CPR_BAND / 60.0);
        robot.rotatingOuttake = true;
        while (robot.rotatingOuttake) {
            encoderSpin(po);
        }
        bandMotor.setVelocity(0.0);
    }


    public void resetTurret(MecanumDrive Drive) {
        Drive.updatePoseEstimate();
        double robotAngle = Drive.localizer.getPose().heading.toDouble();


        rotateMotor.setTargetPosition(radiansToEncoder(robotAngle));
        rotateMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rotateMotor.setPower(0.2); // TODO testing purposes

        robot.currentTurretRadians = 0;
    }

    public void rotateToOuttakeSlot(int po) {
        bandMotor.setVelocity(500 * CPR_BAND / 60.0);
        if (robot.rotatingOuttake) encoderSpin(po); // since pos is all 6 intake/outtake positions
        if (!robot.rotatingOuttake) bandMotor.setVelocity(0.0);
    }

    // to be used in conjunction with aimRotateMotorAprilTag
    // modified so that it will launch all balls it can
//    public void launchAprilTag(boolean blueTeam) {
//        AprilTagPoseFtc offset = robot.camera.getGoalOffset(blueTeam);
//        if (offset == null) {
//            // no tag detected  then don't try to shoot
//            return;
//        }
//        double horizontalDist = offset.y - 3.0;
//
//        prepareBallShot();
//        transferServo.setPosition(liftUp);
//        aimRotateMotorAprilTag(blueTeam);
//        aimAngleServo(horizontalDist);
//        executeLaunchSpeed(horizontalDist);
//
//        this.robot.targetIndex = (this.robot.targetIndex + 1) % 3;
//        transferServo.setPosition(rest);
//
//        this.robot.currentIndex = 0;
//    }



    // tx is x location of field goal and ty is y location
    // DO NOT RUN THIS IN A TELEOP
    public void launch(double tx, double ty, MecanumDrive Drive) { // using georgy's math
        Drive.updatePoseEstimate();
        Pose2d currentPos = Drive.localizer.getPose();
        double dx = tx - currentPos.position.x;
        double dy = ty - currentPos.position.y;
        double dist = Math.hypot(dx, dy) - 3;

        prepareBallShot();
        transferServo.setPosition(liftUp); // TODO
        aimRotateMotor(dx, dy, Drive);
        // aimAngleServo(dist);
        executeLaunchSpeed(dist);
        transferServo.setPosition(rest);
        this.robot.targetIndex = (this.robot.targetIndex + 1) % 3;

        this.robot.currentIndex = 0;
        bandMotor.setVelocity(0);
    }

    public void launchAuto(double tx, double ty, MecanumDrive Drive) {
        Drive.updatePoseEstimate();
        Pose2d currentPos = Drive.localizer.getPose();
        double dx = tx - currentPos.position.x;
        double dy = ty - currentPos.position.y;
        double dist = Math.hypot(dx, dy) - 3;

        prepareBallShotAuto();
        transferServo.setPosition(liftUp);
        aimRotateMotor(dx, dy, Drive);
    }

    public void encoderSpin(int pos) {
        if (!robot.rotatingOuttake) {
            indexServo.setPosition(0.5);
        }

        int current = encoder.getCurrentPosition();
        int error;
        bandMotor.setVelocity(100 * CPR_BAND / 60);
        if (pos > this.robot.currentIndex) {
            // our goal is positive
            error = this.robot.targetPosition[pos] - current; // always positive, unless going from

            if (error <= 0) {
                spindexerStep++;
                indexServo.setPosition(0.5); // stop
                this.robot.rotating = false;
                this.robot.rotatingOuttake = false;
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
                this.robot.rotatingOuttake = false;
                this.robot.currentIndex = pos;
            } else {
                double power = Math.max(robot.SPINDEXER_MIN_SPEED, error / robot.SPINDEXER_SPEED);
                indexServo.setPosition(0.5 - power * 0.5);
            }
        } else {
            this.robot.rotating = false;
            this.robot.rotatingOuttake = false;
        }
    }

    public boolean rotateToOuttakeSlotAuto(int po) {
        robot.rotatingOuttake = true;

        bandMotor.setVelocity(500 * CPR_BAND / 60.0);
        encoderSpin(po);

        if (!robot.rotatingOuttake) {
            bandMotor.setVelocity(0.0);
            indexServo.setPosition(0.5);
            return true;
        }
        return false;
    }
}
