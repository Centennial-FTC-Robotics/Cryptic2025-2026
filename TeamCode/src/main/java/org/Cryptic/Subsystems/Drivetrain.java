package org.Cryptic.Subsystems;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.Cryptic.Subsystem;
import org.firstinspires.ftc.teamcode.MecanumDrive;

public class Drivetrain extends Subsystem {

    public MecanumDrive drivebase;

    public LinearOpMode opmode;

    public void init(LinearOpMode opMode) {
        drivebase = new MecanumDrive(opMode.hardwareMap, new Pose2d(0, 0, 0));
    }

    public void drive(double drive, double strafe, double turn, double speedMult) {
        drivebase.setDrivePowers(new PoseVelocity2d(
                new Vector2d(
                        -(drive * speedMult), // initially negative
                        -(strafe * speedMult)
                ),
                -(turn * speedMult)
        ));
    }
}
