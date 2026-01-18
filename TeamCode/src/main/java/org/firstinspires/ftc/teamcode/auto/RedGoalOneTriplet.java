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
@Autonomous(name="RedGoalOneTriplet")
public class RedGoalOneTriplet extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        Robot robot = new Robot();
        robot.initialize(this);

        double t = 23.5;
        // MIRROR X: (t*-2) became (t*2), heading 135 became 45
        Pose2d initialPose = new Pose2d(t * 2.0, 2.4 * t, Math.toRadians(45));

        MecanumDrive drive = new MecanumDrive(hardwareMap, initialPose);

        // MIRROR X: ballX is positive, scoreX is positive
        double ballX = 1.5 * t, ballY = 0.5 * t + 10;
        double scoreX = t;

        // Target Goal: Since we mirrored across X=0, the goal is now at positive X
        double tx = 3 * t, ty = 3 * t;

        TrajectoryActionBuilder scorePreloaded = drive.actionBuilder(initialPose)
                // MIRROR: (scoreX + 12) becomes (scoreX - 12), Heading 120 -> 60
                .strafeToLinearHeading(new Vector2d(scoreX - 12, ballY), Math.toRadians(60))
                .waitSeconds(0.6)

                .stopAndAdd(robot.scoringActions.scanSpin(robot))
                .waitSeconds(0.6)
                .stopAndAdd(robot.scoringActions.scanSpin(robot))
                .waitSeconds(0.6)
                .stopAndAdd(robot.scoringActions.scanSpin(robot))
                .waitSeconds(0.6)
                // Note: prepareShot and launch use tx, ty which we set to positive 3t
                .stopAndAdd(robot.scoringActions.prepareShot(tx, ty, robot, drive, 0.3))
                .waitSeconds(0.6)
                .stopAndAdd(robot.scoringActions.launch(tx, ty, robot, drive))
                .waitSeconds(0.6)
                .stopAndAdd(robot.scoringActions.lowerScoop(robot))
                .waitSeconds(0.6)
                .stopAndAdd(robot.scoringActions.launch(tx, ty, robot, drive))
                .waitSeconds(0.6)
                .stopAndAdd(robot.scoringActions.lowerScoop(robot))
                .waitSeconds(0.6)
                .stopAndAdd(robot.scoringActions.launch(tx, ty, robot, drive))
                .waitSeconds(0.6)
                .stopAndAdd(robot.scoringActions.lowerScoop(robot))
                .stopAndAdd(robot.scoringActions.stopFlywheel(robot))
                .stopAndAdd(robot.scoringActions.zeroTurret(robot))
                .waitSeconds(1);

        TrajectoryActionBuilder intake3 = scorePreloaded.endTrajectory().fresh()
                // MIRROR: (scoreX + 15) becomes (scoreX - 15). Heading 180 -> 0
                .strafeToLinearHeading(new Vector2d(scoreX - 15, ballY - 12), Math.toRadians(0))
                .stopAndAdd(robot.scoringActions.intakeSpin(robot))

                // MIRROR: (ballX + 2) becomes (ballX - 2)
                .strafeToLinearHeading(new Vector2d(ballX - 2, ballY - 12), Math.toRadians(0))
                .stopAndAdd(robot.scoringActions.intake(robot))
                .stopAndAdd(robot.scoringActions.scanSpin(robot))
                .waitSeconds(0.5)

                // MIRROR: (ballX - 4) becomes (ballX + 4)
                .strafeToConstantHeading(new Vector2d(ballX + 4, ballY - 12))
                .stopAndAdd(robot.scoringActions.scanSpin(robot))
                .waitSeconds(0.5)

                // MIRROR: (ballX - 10) becomes (ballX + 10)
                .strafeToConstantHeading(new Vector2d(ballX + 10, ballY - 12))
                .stopAndAdd(robot.scoringActions.scanSpin(robot))
                .waitSeconds(0.5)
                .stopAndAdd(robot.scoringActions.stopSpin(robot));

        TrajectoryActionBuilder clearRamp = intake3.endTrajectory().fresh()
                .strafeToLinearHeading(new Vector2d(2.0 * t, -16), Math.toRadians(0));

        Action scorePreloadedA = scorePreloaded.build();
        Action intakeA = intake3.build();

        waitForStart();
        if (isStopRequested()) return;

        Actions.runBlocking(
                new ParallelAction(
                        new SequentialAction(
                                scorePreloadedA,
                                intakeA
                        ),
                        robot.scoringActions.robotUpdate(robot)
                )
        );
    }
}