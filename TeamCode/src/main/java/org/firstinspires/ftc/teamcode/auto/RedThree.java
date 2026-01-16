package org.firstinspires.ftc.teamcode.auto;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.Cryptic.Robot;
import org.firstinspires.ftc.teamcode.MecanumDrive;


// assuming RED alliance
// starting AWAY from goal
@Config
@Autonomous(name="RedThree")
public class RedThree extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {

        Robot robot = new Robot();
        robot.initialize(this);

        double t = 23.5; // 23.5 inches per tile
        Pose2d initialPose = new Pose2d(t*(0.5), -2.6*t, Math.toRadians(90));
        MecanumDrive drive = new MecanumDrive(hardwareMap, initialPose);

        double ballX = (1.5)*t, ballY = -(1.5)*t; // coordinates of the first ball
        double scoreX = t, scoreY = t*2; // where to score from, in a launch zone
        double tx = 3*t, ty = 3*t; // coords of the goal


        TrajectoryActionBuilder driveToAprilTag = drive.actionBuilder(initialPose)
                .strafeToLinearHeading(new Vector2d(scoreX, scoreY), Math.toRadians(120))
                .stopAndAdd(robot.scoringActions.getMotif(robot))
                // .stopAndAdd(robot.sampleActions.positionToScore(robot))
                // .strafeToSplineHeading(new Vector2d(scoreX, ballY), Math.toRadians(225))
                // .stopAndAdd(robot.sampleActions.launchSample(robot))
                // .stopAndAdd(robot.sampleActions.reset(robot))
                ;

        double motifIndex = robot.motif - 21;
        double motifIndex2 = (robot.motif+1)%3;
        double motifIndex3 = (robot.motif+2)%3;

        TrajectoryActionBuilder launch = driveToAprilTag.endTrajectory().fresh()
                .splineToLinearHeading(new Pose2d(new Vector2d(ballX, ballY + motifIndex*t), Math.toRadians(0)), Math.toRadians(270))
                .stopAndAdd(robot.scoringActions.scanSpin(robot))
                .strafeToConstantHeading(new Vector2d(ballX + 5, ballY + motifIndex*t))
                .stopAndAdd(robot.scoringActions.scanSpin(robot))
                .strafeToConstantHeading(new Vector2d(ballX + 10, ballY + motifIndex*t))
                .stopAndAdd(robot.scoringActions.scanSpin(robot))
                .strafeToConstantHeading(new Vector2d(scoreX, scoreY))
                .stopAndAdd(robot.scoringActions.prepareShot(tx, ty, robot, drive))
                .stopAndAdd(robot.scoringActions.launch(scoreX, scoreY, robot, drive))
                .stopAndAdd(robot.scoringActions.launch(scoreX, scoreY, robot, drive))
                .stopAndAdd(robot.scoringActions.launch(scoreX, scoreY, robot, drive))
                // do for other balls
                .splineToLinearHeading(new Pose2d(new Vector2d(ballX, ballY + motifIndex2*t), Math.toRadians(0)), Math.toRadians(270))
                .stopAndAdd(robot.scoringActions.scanSpin(robot))
                .strafeToConstantHeading(new Vector2d(ballX + 5, ballY + motifIndex2*t))
                .stopAndAdd(robot.scoringActions.scanSpin(robot))
                .strafeToConstantHeading(new Vector2d(ballX + 10, ballY + motifIndex2*t))
                .stopAndAdd(robot.scoringActions.scanSpin(robot))
                .strafeToConstantHeading(new Vector2d(scoreX, scoreY))
                .stopAndAdd(robot.scoringActions.prepareShot(tx, ty, robot, drive))
                .stopAndAdd(robot.scoringActions.launch(scoreX, scoreY, robot, drive))
                .stopAndAdd(robot.scoringActions.launch(scoreX, scoreY, robot, drive))
                .stopAndAdd(robot.scoringActions.launch(scoreX, scoreY, robot, drive))
                // last one
                .splineToLinearHeading(new Pose2d(new Vector2d(ballX, ballY + motifIndex3*t), Math.toRadians(0)), Math.toRadians(270))
                .stopAndAdd(robot.scoringActions.scanSpin(robot))
                .strafeToConstantHeading(new Vector2d(ballX + 5, ballY + motifIndex3*t))
                .stopAndAdd(robot.scoringActions.scanSpin(robot))
                .strafeToConstantHeading(new Vector2d(ballX + 10, ballY + motifIndex3*t))
                .stopAndAdd(robot.scoringActions.scanSpin(robot))
                .strafeToConstantHeading(new Vector2d(scoreX, scoreY))
                .stopAndAdd(robot.scoringActions.prepareShot(tx, ty, robot, drive))
                .stopAndAdd(robot.scoringActions.launch(scoreX, scoreY, robot, drive))
                .stopAndAdd(robot.scoringActions.launch(scoreX, scoreY, robot, drive))
                .stopAndAdd(robot.scoringActions.launch(scoreX, scoreY, robot, drive))
                // push the gate
                .splineToLinearHeading(new Pose2d(new Vector2d(2.5*t, 0), Math.toRadians(0)), Math.toRadians(0))
                ;



        Action driveToAprilTagA = driveToAprilTag.build();
        Action launchA = launch.build();

        Actions.runBlocking(
                new ParallelAction(
                        new SequentialAction(

                                driveToAprilTagA,
                                launchA
                        )
                )
        );
    }
}