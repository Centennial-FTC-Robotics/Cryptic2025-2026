package org.firstinspires.ftc.teamcode.auto;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.Cryptic.Robot;
import org.firstinspires.ftc.teamcode.MecanumDrive;
import org.firstinspires.ftc.teamcode.ThreeDeadWheelLocalizer;

public class SampleAuto extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {

        Robot robot = new Robot();
        robot.initialize(this);

        double t = 23.5;
        Pose2d initialPose = new Pose2d((t*1.5 - 2.75), (t*2.5 + 2.75), Math.toRadians(180));
        MecanumDrive drive = new MecanumDrive(hardwareMap, initialPose);

        double inPerTick = 1;


        double scoreX = -1, scoreY = -1; // TODO coords of the goal

        TrajectoryActionBuilder driveToScore = drive.actionBuilder(initialPose)
                .stopAndAdd(robot.sampleActions.positionToScore(robot))
                .strafeToSplineHeading(new Vector2d(scoreX, scoreY), Math.toRadians(225))
                // .stopAndAdd(robot.sampleActions.launchSample(robot))
                // .stopAndAdd(robot.sampleActions.reset(robot))
        ;

        TrajectoryActionBuilder firstSample = driveToScore.endTrajectory().fresh()
                .strafeToSplineHeading(new Vector2d(48, 46), Math.toRadians(270))
                .stopAndAdd(robot.sampleActions.intakeSample(robot));
    }
}
