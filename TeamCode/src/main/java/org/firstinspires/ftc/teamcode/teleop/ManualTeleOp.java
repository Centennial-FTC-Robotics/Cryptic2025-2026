package org.firstinspires.ftc.teamcode.teleop;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
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

import java.util.Arrays;

@TeleOp(name = "ManualTeleOp")
public class ManualTeleOp extends LinearOpMode {

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
        bandMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

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

        boolean cooldown = false;

        double CPR = 145.6;

        double ticksPerSecond = 850 * CPR / 60.0;

        double SERVO_BOTTOM = 0.5;
        double SERVO_TOP = 0.25;

        boolean servoIsTop = false; // starts at bottom

        waitForStart();
        long startTime;
        indexServo.setPosition(0.01);
        transferServo.setPosition(SERVO_BOTTOM);

        angleServo.setPosition(0.0);

        double angleServoPos = 0.0; // starting position
        double SERVO_STEP = 0.0035;



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


            if (gamepad1.right_trigger >= 0.7 && gamepad2.left_trigger >= 0.7) {
                bandMotor.setPower(0.0);
            } else if (gamepad1.right_trigger >= 0.7) {
                robot.intake.intakeBall(850); // setup rpm later and constnats
            }  else if (gamepad2.left_trigger >= 0.7) {
                robot.intake.intakeBall(-850); // setup rpm later and constnats
            } else {
                bandMotor.setPower(0.0);
            }

            if (gamepad2.left_bumper) {
                angleServo.setPosition(ANGLE_ONE);
                angleServoPos = ANGLE_ONE;
            } else if (gamepad2.right_bumper) {
                angleServo.setPosition(ANGLE_TWO);
                angleServoPos = ANGLE_TWO;
            }




            if (gamepad1.left_trigger >= 0.7) {
                //powerMotor.setVelocity(ticksPerSecond);
                powerMotor.setPower(-1.0);
            } else {
                powerMotor.setPower(0.0);
            }
/*
            if (drivePad.wasJustPressed(GamepadKeys.Button.Y)) {
                robot.intake.rotateToVacantSpot();
            }


            if (drivePad.wasJustPressed(GamepadKeys.Button.DPAD_DOWN)) {
                robot.intake.scanBallColor();
            }

 */

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

            // NormalizedRGBA colors = colorSensor.getNormalizedColors();

            telemetry.addData("red: ",colorSensor.red());
            telemetry.addData("green: ",colorSensor.green());
            telemetry.addData("blue: ",colorSensor.blue());
            telemetry.addData("currentIndex, currentBalls: ", robot.currentIndex+", "+ Arrays.toString(robot.currentBalls));
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


            // Read motif
            if (drivePad.wasJustPressed(GamepadKeys.Button.A)) {
                robot.camera.getMotif();
            }

            dashboard.sendTelemetryPacket(packet);
            telemetry.update();


        }
    }
}