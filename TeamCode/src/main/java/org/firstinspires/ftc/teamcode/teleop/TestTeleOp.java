package org.firstinspires.ftc.teamcode.teleop;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.arcrobotics.ftclib.gamepad.ToggleButtonReader;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;


import org.Cryptic.Robot;

@TeleOp(name = "TestTeleOp")
public class TestTeleOp extends LinearOpMode {

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
        DcMotorEx rightFront = hardwareMap.get(DcMotorEx.class, "rightFront");
        DcMotorEx leftBack = hardwareMap.get(DcMotorEx.class, "leftBack");
        DcMotorEx rightBack = hardwareMap.get(DcMotorEx.class, "leftFront");



        waitForStart();
        while (opModeIsActive()) {
            TelemetryPacket packet = new TelemetryPacket();

            drivePad.readButtons();
            intakePad.readButtons();
            bReader.readValue();
            bIntakeReader.readValue();
            toggleAutoAim.readValue();

            robot.intake.update();
            robot.outtake.update();

            if (intakePad.wasJustPressed(GamepadKeys.Button.LEFT_BUMPER) && autoAimMode) {
                autoAimMode = false;
            } else if (intakePad.wasJustPressed(GamepadKeys.Button.LEFT_BUMPER) && !autoAimMode) {
                autoAimMode = true;
            }

            if (autoAimMode) {
                if (gamepad2.left_trigger >= 0.15) {
                    robot.outtake.autoUpdateAim(72.0, -72.0, robot.dt.drivebase);
                }
            } else {
                if (gamepad2.dpad_left) {
                    robot.outtake.manuallyUpdateAim(1000); // find rpm later
                } else if (gamepad2.dpad_right) {
                    robot.outtake.manuallyUpdateAim(-1000); // change rpm later
                }
            }

            if (gamepad2.right_trigger >= 0.15) {
                robot.intake.grabBall(1000); // setup rpm later and constnats
            }


            robot.dt.drivebase.setDrivePowers(new PoseVelocity2d(
                    new Vector2d(
                            -gamepad1.left_stick_y * 0.75,
                            -gamepad1.left_stick_x * 0.75
                    ),
                    -gamepad1.right_stick_x
            ));




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


            // Outtake actually launch
            if (drivePad.wasJustPressed(GamepadKeys.Button.LEFT_BUMPER)) {
                robot.outtake.launch(72.0,-72.0,robot.dt.drivebase);
            }

            // Read motif
            if (drivePad.wasJustPressed(GamepadKeys.Button.A)) {
                robot.camera.getMotif();
            }
        }
    }
}
