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

import org.Cryptic.Robot;

@TeleOp(name = "TestTeleOpRed")
public class TestTeleOpRed extends LinearOpMode {

    @Override
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
                    robot.outtake.autoUpdateAim(-72.0, -72.0, robot.dt.drivebase);
                }
            } else {
                if (gamepad2.dpad_left) {
                    robot.outtake.manuallyUpdateAim(1000); // find rpm later
                } else if (gamepad2.dpad_right) {
                    robot.outtake.manuallyUpdateAim(-1000); // change rpm later
                }
            }

            if (gamepad2.right_trigger >= 0.15) {
                robot.intake.intakeComplete(850); // setup rpm later and constnats
            }

            robot.dt.drivebase.setDrivePowers(new PoseVelocity2d(
                    new Vector2d(
                            -gamepad1.left_stick_y,
                            -gamepad1.left_stick_x
                    ),
                    -gamepad1.right_stick_x
            ));


            // Read motif
            if (drivePad.wasJustPressed(GamepadKeys.Button.A)) {
                robot.camera.getMotif();
            }
        }
    }
}