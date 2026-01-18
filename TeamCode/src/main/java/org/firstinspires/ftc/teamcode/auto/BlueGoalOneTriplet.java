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

        double ballX = (-1.5)*t, ballY = 0.5*t+10; // coords of the first ball
        double scoreX = -t, scoreY = t + 6; // where to score from, in a launch zone
        double tx = -3*t, ty = 3*t; // coords of the goal

        // robot.dt.drivebase.localizer.setPose(initialPose);

        TrajectoryActionBuilder scorePreloaded = drive.actionBuilder(initialPose)
//                .strafeToLinearHeading(new Vector2d(scoreX + 12, scoreY), Math.toRadians(60))
//                .waitSeconds(0.5) // TESTING PURPOSES
                .strafeToLinearHeading(new Vector2d(scoreX + 12, ballY), Math.toRadians(120))
                .waitSeconds(0.6) // TESTING PURPOSES

                //start shooting
                .stopAndAdd(robot.scoringActions.scanSpin(robot))
                .waitSeconds(0.6) // TESTING PURPOSES
                .stopAndAdd(robot.scoringActions.scanSpin(robot))
                .waitSeconds(0.6) // TESTING PURPOSES
                .stopAndAdd(robot.scoringActions.scanSpin(robot))
                .waitSeconds(0.6) // TESTING PURPOSES
                .stopAndAdd(robot.scoringActions.prepareShot(tx, ty, robot, drive, 0.3))
                .waitSeconds(0.6) //KEEP THIS
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

                .waitSeconds(1) // TESTING PURPOSES
                // .stopAndAdd(robot.scoringActions.getMotif(robot))
                // .stopAndAdd(robot.scoringActions.positionToScore(robot))
                // .strafeToSplineHeading(new Vector2d(scoreX, ballY), Math.toRadians(225))
                // .stopAndAdd(robot.scoringActions.launchSample(robot))
                // .stopAndAdd(robot.scoringActions.reset(robot))
                ;

        TrajectoryActionBuilder intake3 = scorePreloaded.endTrajectory().fresh()
                .strafeToLinearHeading(new Vector2d(scoreX + 15, ballY - 12), Math.toRadians(180))
                .stopAndAdd(robot.scoringActions.intakeSpin(robot))
                //.splineToLinearHeading(new Pose2d(new Vector2d(ballX, ballY), Math.toRadians(180)), Math.toRadians(180))
                .strafeToLinearHeading(new Vector2d(ballX+2, ballY - 12), Math.toRadians(180))
                .stopAndAdd(robot.scoringActions.intake(robot))
                .stopAndAdd(robot.scoringActions.scanSpin(robot))
                .waitSeconds(0.5) // TESTING PURPOSES
                .strafeToConstantHeading(new Vector2d(ballX - 4, ballY - 12))
                .stopAndAdd(robot.scoringActions.scanSpin(robot))
                .waitSeconds(0.5) // TESTING PURPOSES
                .strafeToConstantHeading(new Vector2d(ballX - 10, ballY - 12))
                .stopAndAdd(robot.scoringActions.scanSpin(robot))
                .waitSeconds(0.5) // TESTING PURPOSES
                .stopAndAdd(robot.scoringActions.stopSpin(robot))
//                .strafeToConstantHeading(new Vector2d(scoreX, scoreY))
//                SCORE BALLS IF WE WANT
//                .waitSeconds(0.5)
//                .stopAndAdd(robot.scoringActions.prepareShot(tx, ty, robot, drive))
//                .waitSeconds(3)
//                .stopAndAdd(robot.scoringActions.launch(tx, ty, robot, drive))
//                .waitSeconds(0.5)
//                .stopAndAdd(robot.scoringActions.lowerScoop(robot))
//                .stopAndAdd(robot.scoringActions.stopFlywheel(robot))
                ;
//                .stopAndAdd(robot.scoringActions.prepareShot(tx, ty, robot))
//                .stopAndAdd(robot.scoringActions.launch(scoreX,scoreY,robot))
//                .stopAndAdd(robot.scoringActions.launch(scoreX,scoreY,robot))
//                .stopAndAdd(robot.scoringActions.launch(scoreX,scoreY,robot))
                ;

        TrajectoryActionBuilder clearRamp = intake3.endTrajectory().fresh()
                //.splineToLinearHeading(new Pose2d(new Vector2d(-2.0*t, -8), Math.toRadians(180)), Math.toRadians(180))
                .strafeToLinearHeading(new Vector2d(-2.0*t, -16), Math.toRadians(0))
                ;

        Action scorePreloadedA = scorePreloaded.build();
        Action intakeA = intake3.build();
        Action clearRampA = clearRamp.build();

        waitForStart();

        if (isStopRequested()) return;

        Actions.runBlocking(
                new ParallelAction(
                        new SequentialAction(

                                scorePreloadedA,
                                intakeA
//                                clearRampA
                        ),
                        robot.scoringActions.robotUpdate(robot)
                )
        );

    }
}
