package org.firstinspires.ftc.teamcode.auto;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.Cryptic.Robot;
import org.firstinspires.ftc.teamcode.MecanumDrive;
import org.firstinspires.ftc.teamcode.ThreeDeadWheelLocalizer;

public class SampleAuto extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {

        Robot robot = new Robot();
        robot.initialize(this);

        double t = 24;
        Pose2d initialPose = new Pose2d((t*1 - 4), (t*5 + 6), Math.toRadians(315));
        MecanumDrive drive = new MecanumDrive(hardwareMap, initialPose);

        double inPerTick = 1;

        double ballX = t+6, ballY = 3*t+6; // TODO coords of the goal
        double scoreX = t*3, scoreY = t*5;

        TrajectoryActionBuilder driveToScore = drive.actionBuilder(initialPose)
                .strafeToLinearHeading(new Vector2d(ballX, ballY), Math.toRadians(60))
                .stopAndAdd(robot.sampleActions.getMotif(robot))
                // .stopAndAdd(robot.sampleActions.positionToScore(robot))
                // .strafeToSplineHeading(new Vector2d(scoreX, ballY), Math.toRadians(225))
                // .stopAndAdd(robot.sampleActions.launchSample(robot))
                // .stopAndAdd(robot.sampleActions.reset(robot))
        ;

        TrajectoryActionBuilder firstLaunch = driveToScore.endTrajectory().fresh()
                .strafeToSplineHeading(new Vector2d(ballX, ballY + (21 - robot.motif)*t), Math.toRadians(180))
                .stopAndAdd(robot.sampleActions.grabBall(robot))
                .strafeToConstantHeading(new Vector2d(ballX - 5, ballY + (21 - robot.motif)*t))
                .stopAndAdd(robot.sampleActions.grabBall(robot))
                .strafeToConstantHeading(new Vector2d(ballX - 10, ballY + (21 - robot.motif)*t))
                .stopAndAdd(robot.sampleActions.grabBall(robot))
                .strafeToConstantHeading(new Vector2d(scoreX, scoreY))
                .stopAndAdd(robot.sampleActions.autoUpdateAim(robot))
                // TODO implement .stopAndAdd(robot.sampleActions.selectBall(robot))
                .stopAndAdd(robot.sampleActions.launch(robot));

        Action driveToScoreA = driveToScore.build();
        Action firstLaunchA = firstLaunch.build();

        Actions.runBlocking(
                new ParallelAction(
                        new SequentialAction(
                                driveToScoreA,
                                firstLaunchA
                        )
                )
        );
    }
}
