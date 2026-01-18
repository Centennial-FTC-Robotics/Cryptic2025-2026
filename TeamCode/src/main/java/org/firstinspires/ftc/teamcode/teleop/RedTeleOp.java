package org.firstinspires.ftc.teamcode.teleop;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.arcrobotics.ftclib.gamepad.ToggleButtonReader;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;


import org.Cryptic.Robot;
import org.Cryptic.Subsystems.Outtake;
import org.firstinspires.ftc.teamcode.MecanumDrive;

import java.util.Arrays;

@TeleOp(name = "RedTeleOp")
public class RedTeleOp extends LinearOpMode {

    public static double GOAL_X = -72.0;
    public static double GOAL_Y = 72.0;

    //@Override
    public void runOpMode() throws InterruptedException {
        Robot robot = new Robot();
        robot.initialize(this);

        GamepadEx drivePad = new GamepadEx(gamepad1);
        GamepadEx intakePad = new GamepadEx(gamepad2);

        ToggleButtonReader bReader = new ToggleButtonReader(
                drivePad, GamepadKeys.Button.B
        );

        ToggleButtonReader bIntakeReader = new ToggleButtonReader(
                intakePad, GamepadKeys.Button.B
        );

        ToggleButtonReader toggleAutoAim = new ToggleButtonReader(
                intakePad, GamepadKeys.Button.LEFT_BUMPER
        );

        FtcDashboard dashboard = FtcDashboard.getInstance();

        boolean autoAimMode = true;

        DcMotorEx leftFront = hardwareMap.get(DcMotorEx.class, "leftFront");
        DcMotorEx leftBack = hardwareMap.get(DcMotorEx.class, "leftBack");
        DcMotorEx rightBack = hardwareMap.get(DcMotorEx.class, "rightBack");
        DcMotorEx rightFront = hardwareMap.get(DcMotorEx.class, "rightFront");

        DcMotorEx bandMotor = hardwareMap.get(DcMotorEx.class, "bandMotor");
        DcMotorEx encoder = hardwareMap.get(DcMotorEx.class, "spinEncoder");
        bandMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        encoder.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        // encoder.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        encoder.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        ColorSensor colorSensor = hardwareMap.get(ColorSensor.class, "colorSensor");

        Servo indexServo = hardwareMap.get(Servo.class, "indexServo");
        Servo transferServo = hardwareMap.get(Servo.class, "transferServo");

        Servo angleServo = hardwareMap.get(Servo.class, "angleServo");

        double ANGLE_ONE = 0.65;
        double ANGLE_TWO = 0.5;


        DcMotorEx powerMotor = hardwareMap.get(DcMotorEx.class, "powerMotor");

        DcMotorEx rotateMotor = hardwareMap.get(DcMotorEx.class,"rotateMotor");
        rotateMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        leftFront.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.FLOAT);
        leftBack.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.FLOAT);
        rightBack.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.FLOAT);
        rightFront.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.FLOAT);
        // rightBack.setDirection(DcMotorEx.Direction.FORWARD);

        boolean cooldown = false;

        // if needed:
        // rightBack.setDirection(DcMotorEx.Direction.FORWARD);

        double CPR = 145.6;

        double ticksPerSecond = 850 * CPR / 60.0;

        boolean servoIsTop = false; // starts at bottom
        Pose2d initialPose = new Pose2d(-(24.0*3-6.5), -(24.0*3-6.5) + 10.0, 180); // -9.0 to adjust odo? TODO CHANGE LATER

        robot.dt.drivebase.localizer.update();
        robot.dt.drivebase.localizer.setPose(initialPose);

        // angleServo.setPosition(0.0);
        if (FtcDashboard.getInstance() != null) {
            telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        }

        waitForStart();

        boolean rotating = false;
        int outtakePos = 0;

        while (opModeIsActive()) {

            rotateMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

            TelemetryPacket packet = new TelemetryPacket();

            drivePad.readButtons();
            intakePad.readButtons();
            bReader.readValue();
            bIntakeReader.readValue();
            toggleAutoAim.readValue();

            robot.intake.update();
            robot.outtake.update();


            if (intakePad.wasJustPressed(GamepadKeys.Button.LEFT_BUMPER)) {

            }


            // drivepad control intake
            if (gamepad1.right_trigger >= 0.4) {
                robot.intake.intakeBall(850);
            } else {
                robot.intake.intakeBall(0.0);
            }

            if (intakePad.wasJustPressed(GamepadKeys.Button.X)) {
                robot.rotatingIntake = true;
            }
            if (robot.rotatingIntake) {
                robot.intake.rotateToVacantSpot();
            }

            if (intakePad.wasJustPressed(GamepadKeys.Button.Y)) {
                outtakePos = robot.outtake.calculateOuttakeSlot();
                robot.rotatingOuttake = true;
            }
            if (robot.rotatingOuttake) {
                robot.outtake.rotateToOuttakeSlot(outtakePos);
            }

            if (intakePad.wasJustPressed(GamepadKeys.Button.DPAD_LEFT)) {
                robot.intake.scanBallColor();
            }

            if (intakePad.wasJustPressed(GamepadKeys.Button.DPAD_UP)) {
                robot.dt.drivebase.updatePoseEstimate();
                double dx = GOAL_X - robot.dt.drivebase.localizer.getPose().position.x;
                double dy = GOAL_Y - robot.dt.drivebase.localizer.getPose().position.y;
                robot.outtake.aimRotateMotor(dx, dy, robot.dt.drivebase);
                robot.outtake.executeLaunchSpeed(Math.sqrt(dx * dx + dy * dy));
            }

            // transfer servo
            if (intakePad.wasJustPressed(GamepadKeys.Button.DPAD_RIGHT)) {
                robot.outtake.moveTransfer();
            }

            if (intakePad.wasJustPressed(GamepadKeys.Button.DPAD_DOWN)) {
                robot.outtake.stopFlywheel();
            }
            // RESET THE POSE IF WRONG, MOVE TO CORNER
            if (drivePad.wasJustPressed(GamepadKeys.Button.DPAD_DOWN)) {
                robot.dt.drivebase.localizer.setPose(initialPose);
            }

            leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            leftBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            rightBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            robot.dt.drivebase.setDrivePowers(new PoseVelocity2d(
                    new Vector2d(
                            -gamepad1.left_stick_y,
                            -gamepad1.left_stick_x
                    ),
                    -gamepad1.right_stick_x
            ));

            telemetry.addData("LF", robot.dt.drivebase.leftFront.getPower());
            telemetry.addData("LB", robot.dt.drivebase.leftBack.getPower());
            telemetry.addData("RF", robot.dt.drivebase.rightFront.getPower());
            telemetry.addData("RB", robot.dt.drivebase.rightBack.getPower());
            telemetry.addData("intake trigger", gamepad1.right_trigger);

            // NormalizedRGBA colors = colorSensor.getNormalizedColors();

            telemetry.addData("red: ",colorSensor.red());
            telemetry.addData("green: ",colorSensor.green());
            telemetry.addData("blue: ",colorSensor.blue());
            telemetry.addData("currentIntakeIndex, currentBalls: ", robot.currentIntakeIndex+", "+ Arrays.toString(robot.currentBalls));
            telemetry.addData("currentIndex", robot.currentIndex);
            telemetry.addData("motif: ", robot.motif);
            telemetry.addData("Encoder", encoder.getCurrentPosition());
            telemetry.addData("rotating", rotating);
            telemetry.addData("rotatingIntake", robot.rotatingIntake);
            telemetry.addData("outtake po", outtakePos);
            robot.dt.drivebase.updatePoseEstimate();// For RoadRunner 1.0
            Pose2d currentPose = robot.dt.drivebase.localizer.getPose();
            telemetry.addData("x", currentPose.position.x);
            telemetry.addData("y", currentPose.position.y);
            telemetry.addData("heading", Math.toDegrees(currentPose.heading.toDouble())+" degrees");
            telemetry.addData("turret heading", robot.outtake.rotateMotor.getCurrentPosition());
            telemetry.addData("turret target position", Math.toDegrees(robot.outtake.encoderToRadians(robot.outtake.rotateMotor.getTargetPosition())));
            telemetry.addData("turret power", robot.outtake.rotateMotor.getPower());
            telemetry.addData("shooter power", robot.outtake.powerMotor.getVelocity()); // -1700

            /*
            double max;
            double axial = -gamepad1.left_stick_y;  // Note: pushing stick forward gives negative value
            double lateral = gamepad1.left_stick_x;
            double yaw = gamepad1.right_stick_x;

            // Combine the joystick requests for each axis-motion to determine each wheel's power.
            // Set up a variable for each drive wheel to save the power level for telemetry.
            double leftFrontDOUBLE = axial + lateral + yaw;
            double rightFrontDOUBLE = axial - lateral - yaw;
            double leftBackDOUBLE = axial - lateral + yaw;
            double rightBackDOUBLE = axial + lateral - yaw;

            // Normalize the values so no wheel power exceeds 100%
            // This ensures that the robot maintains the desired motion.
            max = Math.max(Math.abs(leftFrontDOUBLE), Math.abs(rightFrontDOUBLE));
            max = Math.max(max, Math.abs(leftBackDOUBLE));
            max = Math.max(max, Math.abs(rightBackDOUBLE));

            if (max > 1.0) {
                leftFrontDOUBLE /= max;
                rightFrontDOUBLE /= max;
                leftBackDOUBLE /= max;
                rightBackDOUBLE /= max;
            }

            leftFront.setPower(leftFrontDOUBLE * 0.25);
            rightFront.setPower(rightFrontDOUBLE * 0.25);
            leftBack.setPower(leftBackDOUBLE * 0.25);
            rightBack.setPower(rightBackDOUBLE * 0.25);

*/

            dashboard.sendTelemetryPacket(packet);
            telemetry.update();


        }
    }
}