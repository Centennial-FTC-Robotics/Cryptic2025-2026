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
// starting AWAY from goal
// ONLY PICKING UP 1 TRIPLET OF BALLS (CLOSEST)
@Config
@Autonomous(name="BlueOneTriplet")
public class BlueOneTriplet extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {

        Robot robot = new Robot();
        robot.initialize(this);

        double t = 23.5; // 23.5 inches per tile
        Pose2d initialPose = new Pose2d(t*(-0.5), (-2.6)*t, Math.toRadians(90));
        MecanumDrive drive = new MecanumDrive(hardwareMap, initialPose);

        double ballX = (-1.5)*t, ballY = -1.5*t; // coords of the first ball
        double scoreX = -0.5*t, scoreY = -2.6*t + 4; // where to score from, in a launch zone
        double tx = -3*t, ty = 3*t; // coords of xthe goal


        TrajectoryActionBuilder scorePreloaded = drive.actionBuilder(initialPose)
                .strafeToLinearHeading(new Vector2d(scoreX, scoreY), Math.toRadians(90))
                .stopAndAdd(robot.scoringActions.scanSpin(robot))
                .waitSeconds(0.5) // TESTING PURPOSES
                .stopAndAdd(robot.scoringActions.scanSpin(robot))
                .waitSeconds(0.5) // TESTING PURPOSES
                .stopAndAdd(robot.scoringActions.scanSpin(robot))
                .waitSeconds(0.5) // TESTING PURPOSES
                .stopAndAdd(robot.scoringActions.prepareShot(tx, ty, robot, drive, 0.42))
                .waitSeconds(1.0) //KEEP THIS
                .stopAndAdd(robot.scoringActions.launch(tx, ty, robot, drive))
                .waitSeconds(1.0)
                .stopAndAdd(robot.scoringActions.lowerScoop(robot))
                .waitSeconds(1.0)
                .stopAndAdd(robot.scoringActions.launch(tx, ty, robot, drive))
                .waitSeconds(1.0)
                .stopAndAdd(robot.scoringActions.lowerScoop(robot))
                .waitSeconds(1.0)
                .stopAndAdd(robot.scoringActions.launch(tx, ty, robot, drive))
                .waitSeconds(1.0)
                .stopAndAdd(robot.scoringActions.lowerScoop(robot))
                .stopAndAdd(robot.scoringActions.stopFlywheel(robot))
                .stopAndAdd(robot.scoringActions.zeroTurret(robot))
                .waitSeconds(1) // TESTING PURPOSES
                .stopAndAdd(robot.scoringActions.stopSpin(robot))
//                .stopAndAdd(robot.scoringActions.getMotif(robot))
                // .stopAndAdd(robot.sampleActions.positionToScore(robot))
                // .strafeToSplineHeading(new Vector2d(scoreX, ballY), Math.toRadians(225))
                // .stopAndAdd(robot.sampleActions.launchSample(robot))
                // .stopAndAdd(robot.sampleActions.reset(robot))
                ;



        TrajectoryActionBuilder moveOut = scorePreloaded.endTrajectory().fresh()
                .strafeToLinearHeading(new Vector2d(-2*t, -2.5*t), Math.toRadians(180))
                ;

        Action scorePreloadedA = scorePreloaded.build();
        Action moveOutA = moveOut.build();

        waitForStart();

        if (isStopRequested()) return;

        Actions.runBlocking(
                new ParallelAction(
                        new SequentialAction(

                                scorePreloadedA,
                                moveOutA
//                                clearRampA
                        ),
                        robot.scoringActions.robotUpdate(robot)
                )
        );
    }
}
