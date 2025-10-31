package org.Cryptic.Subsystems;
import com.acmerobotics.roadrunner.Pose2d;

import com.acmerobotics.roadrunner.Rotation2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

import org.Cryptic.Subsystem;
import org.firstinspires.ftc.teamcode.MecanumDrive;

public class Outtake extends Subsystem {

    public DcMotorEx rotateMotor;
    public Servo angleServo;
    public DcMotorEx powerMotor;

    public int motif;
    public int[] order; // TODO

    @Override
    public void init(LinearOpMode opmode) throws InterruptedException {
        // TODO
        powerMotor = opmode.hardwareMap.get(DcMotorEx.class, "powerMotor");
        angleServo = opmode.hardwareMap.get(Servo.class, "angleServo");
        rotateMotor = opmode.hardwareMap.get(DcMotorEx.class, "rotateMotor");
    }

    public void launchHigh() {
        // TODO(); figure out odometry, angles, where we are
    }

    public void launchStraight() {
        // TODO;
    }

    public void outtakeSamples() {
        // motif is where the purple ball is
        // order is the loadout of the current balls
        // to figure out order, keep a color sensor

        int green = 0;
        int purple = 0;

        for (int i=0; i<3; ++i) {
            if (order[i] == 0) green++;
            else if (order[i] == 1) purple++;
        }

        if (green != 1 || purple != 2) return;

        int gi = -1;
        int m = motif;
        for (int i=0; i<3; ++i) if (order[i] == 0) gi = i;
        // cases:
        // gpp
        // m=1 easy, m=2 high straight high m=3 high straight straight
        // pgp
        // m=1 high straight high m=2 easy m=3 straight high straight
        // ppg
        // m=1 high high straight m=2 straight high straight m=3 easy
        if (gi == motif-1) {
            // just fire all three as is
            launchStraight();
            launchStraight();
            launchStraight();
        } else if (gi == 0) {
            if (m == 2) {
                launchHigh();
                launchStraight();
                launchHigh();
            } else if (m == 3) {
                launchHigh();
                launchStraight();
                launchStraight();
            }
        } else if (gi == 1) {
            if (m == 1) {
                launchHigh();
                launchStraight();
                launchHigh();
            } else if (m == 3) {
                launchStraight();
                launchHigh();
                launchStraight();
            }
        } else {
            if (m == 1) {
                launchHigh();
                launchHigh();
                launchStraight();
            } else if (m == 2) {
                launchStraight();
                launchHigh();
                launchStraight();
            }
        }
    }



    public void autoUpdateAim(MecanumDrive Drive) {

        double tx = -72.0; // target x position change later
        double ty = -72.0; // target y position change later

        Drive.updatePoseEstimate();

        Pose2d currentPos = Drive.localizer.getPose();

        double dx = tx - currentPos.position.x;
        double dy = ty - currentPos.position.y;

        Rotation2d heading = currentPos.heading;
        double headingDeg = heading.log();

        double aimTargetInRadians = Math.atan2(dy,dx);

        // not sure if we need heading (where front of robot is facing) depending on if we have full 360 range of motion. ts might get weird


    }


}
