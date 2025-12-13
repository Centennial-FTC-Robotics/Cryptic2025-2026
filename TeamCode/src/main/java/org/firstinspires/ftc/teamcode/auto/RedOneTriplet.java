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


// assuming RED alliance
// starting AWAY from goal
// ONLY PICKING UP 1 TRIPLET OF BALLS (CLOSEST)
public class RedOneTriplet extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {

        Robot robot = new Robot();
        robot.initialize(this);

        double t = 23.5; // 23.5 inches per tile
        // Pose2d initialPose = new Pose2d((t*(-2) - 4), (t*2 + 6), Math.toRadians(315));
        Pose2d initialPose = new Pose2d(t*(0.5), -2.6*t, Math.toRadians(90));
        MecanumDrive drive = new MecanumDrive(hardwareMap, initialPose);

        double ballX = 1.5*t, ballY = -1.5*t; // coords of the first ball
        double scoreX = 0, scoreY = t*2; // where to score from, in a launch zone
        double tx = 3*t, ty = 3*t; // coords of the goal


        TrajectoryActionBuilder driveToAprilTag = drive.actionBuilder(initialPose)
                .strafeToLinearHeading(new Vector2d(0, t), Math.toRadians(90))
                .stopAndAdd(robot.sampleActions.getMotif(robot))
                // .stopAndAdd(robot.sampleActions.positionToScore(robot))
                // .strafeToSplineHeading(new Vector2d(scoreX, ballY), Math.toRadians(225))
                // .stopAndAdd(robot.sampleActions.launchSample(robot))
                // .stopAndAdd(robot.sampleActions.reset(robot))
                ;

        TrajectoryActionBuilder launch = driveToAprilTag.endTrajectory().fresh()
                .splineToLinearHeading(new Pose2d(new Vector2d(ballX, ballY), Math.toRadians(0)), Math.toRadians(0))
                .stopAndAdd(robot.sampleActions.grabBall(robot))
                .strafeToConstantHeading(new Vector2d(ballX + 5, ballY))
                .stopAndAdd(robot.sampleActions.grabBall(robot))
                .strafeToConstantHeading(new Vector2d(ballX + 10, ballY))
                .stopAndAdd(robot.sampleActions.grabBall(robot))
                .strafeToConstantHeading(new Vector2d(scoreX, scoreY))
                .stopAndAdd(robot.sampleActions.aimAtGoal(tx, ty, robot))
                .stopAndAdd(robot.sampleActions.launch(scoreX,scoreY,robot))
                .stopAndAdd(robot.sampleActions.launch(scoreX,scoreY,robot))
                .stopAndAdd(robot.sampleActions.launch(scoreX,scoreY,robot))
                ;

        TrajectoryActionBuilder clearRamp = launch.endTrajectory().fresh()
                .splineToLinearHeading(new Pose2d(new Vector2d(2.5*t, 0), Math.toRadians(0)), 0)
                ;

        Action driveToAprilTagA = driveToAprilTag.build();
        Action launchA = launch.build();
        Action clearRampA = clearRamp.build();

        Actions.runBlocking(
                new ParallelAction(
                        new SequentialAction(

                                driveToAprilTagA,
                                launchA,
                                clearRampA
                        )
                )
        );
    }
}
