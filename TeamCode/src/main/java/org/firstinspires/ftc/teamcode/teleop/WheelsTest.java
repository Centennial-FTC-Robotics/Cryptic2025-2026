package org.firstinspires.ftc.teamcode.teleop;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.arcrobotics.ftclib.gamepad.ToggleButtonReader;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;


import org.Cryptic.Robot;

@TeleOp(name = "WheelsTest")
public class WheelsTest extends LinearOpMode {

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


        DcMotor leftFront = hardwareMap.get(DcMotorEx.class, "leftFront");
        DcMotor leftBack = hardwareMap.get(DcMotorEx.class, "leftBack");
        DcMotor rightBack = hardwareMap.get(DcMotorEx.class, "rightBack");
        DcMotor rightFront = hardwareMap.get(DcMotorEx.class, "rightFront");

        leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


        // maybe delete
        for (DcMotor m : new DcMotor[]{leftFront, leftBack, rightFront, rightBack}) {
            m.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            m.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);  // or RUN_USING_ENCODER, just be consistent
            m.setPower(0);
        }



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
                robot.intake.intakeBall(850); // setup rpm later and constnats
            }



            telemetry.addData("RF", robot.dt.drivebase.rightFront.getPower());
            telemetry.addData("RB", robot.dt.drivebase.rightBack.getPower());
            telemetry.addData("RF", robot.dt.drivebase.rightFront.getPower());
            telemetry.addData("RB", robot.dt.drivebase.rightBack.getPower());




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
