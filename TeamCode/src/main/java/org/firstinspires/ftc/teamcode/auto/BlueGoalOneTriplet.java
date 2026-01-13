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


// assuming blue alliance
// starting at goal
// ONLY PICKING UP 1 TRIPLET OF BALLS (CLOSEST)
// NOTE: CENTER OF ROBOT IS TOP LEFT
@Config
@Autonomous(name="BlueGoalOneTriplet")
public class BlueGoalOneTriplet extends LinearOpMode {

//    private boolean isBlue = true;
//
//    public Pose2d mapPose(Pose2d pose) {
//        if (blue) return pose;
//        return new Pose2d(pose.position.x, )
//    }

    @Override
    public void runOpMode() throws InterruptedException {

        Robot robot = new Robot();
        robot.initialize(this);

        double t = 23.5; // 23.5 inches per tile
        // Pose2d initialPose = new Pose2d((t*(-2) - 4), (t*2 + 6), Math.toRadians(315));
        Pose2d initialPose = new Pose2d(t*(-2.0), 2.4*t, Math.toRadians(135));

        MecanumDrive drive = new MecanumDrive(hardwareMap, initialPose);
        // MecanumDrive drive = robot.dt.drivebase;

        double ballX = (-1.5)*t + 9, ballY = 0.5*t + 12; // coords of the first ball
        double scoreX = -t, scoreY = t + 6; // where to score from, in a launch zone
        double tx = -3*t, ty = 3*t; // coords of the goal

        // robot.dt.drivebase.localizer.setPose(initialPose);

        TrajectoryActionBuilder scorePreloaded = drive.actionBuilder(initialPose)
                .strafeToLinearHeading(new Vector2d(scoreX + 12, scoreY), Math.toRadians(60))
                .waitSeconds(0.2) // TESTING PURPOSES
                .strafeToLinearHeading(new Vector2d(scoreX + 36, ballY), Math.toRadians(120))
                .waitSeconds(0.2) // TESTING PURPOSES
                .turnTo(Math.toRadians(180))
                .waitSeconds(0.2) // TESTING PURPOSES
                // .stopAndAdd(robot.sampleActions.getMotif(robot))
                // .stopAndAdd(robot.sampleActions.positionToScore(robot))
                // .strafeToSplineHeading(new Vector2d(scoreX, ballY), Math.toRadians(225))
                // .stopAndAdd(robot.sampleActions.launchSample(robot))
                // .stopAndAdd(robot.sampleActions.reset(robot))
                ;

        TrajectoryActionBuilder launch = scorePreloaded.endTrajectory().fresh()
                .splineToLinearHeading(new Pose2d(new Vector2d(ballX, ballY), Math.toRadians(180)), Math.toRadians(180))
                .stopAndAdd(robot.scoringActions.intake(robot))
                .stopAndAdd(robot.scoringActions.scanSpin(robot))
                .strafeToConstantHeading(new Vector2d(ballX - 5, ballY))
                .waitSeconds(0.2) // TESTING PURPOSES
//                .stopAndAdd(robot.sampleActions.intakeComplete(robot))
                .strafeToConstantHeading(new Vector2d(ballX - 10, ballY))
                .waitSeconds(0.2) // TESTING PURPOSES
//                .stopAndAdd(robot.sampleActions.intakeComplete(robot))
                .strafeToConstantHeading(new Vector2d(scoreX, scoreY))
//                .stopAndAdd(robot.sampleActions.aimAtGoal(tx, ty, robot))
//                .stopAndAdd(robot.sampleActions.launch(scoreX,scoreY,robot))
//                .stopAndAdd(robot.sampleActions.launch(scoreX,scoreY,robot))
//                .stopAndAdd(robot.sampleActions.launch(scoreX,scoreY,robot))
                ;

        TrajectoryActionBuilder clearRamp = launch.endTrajectory().fresh()
                .splineToLinearHeading(new Pose2d(new Vector2d(-2.9*t, 0), Math.toRadians(180)), Math.toRadians(180))
                ;

        Action scorePreloadedA = scorePreloaded.build();
        Action launchA = launch.build();
        Action clearRampA = clearRamp.build();

        waitForStart();

        if (isStopRequested()) return;

        Actions.runBlocking(
                new ParallelAction(
                        new SequentialAction(

                                scorePreloadedA,
                                launchA,
                                clearRampA
                        )
                )
        );

    }
}
