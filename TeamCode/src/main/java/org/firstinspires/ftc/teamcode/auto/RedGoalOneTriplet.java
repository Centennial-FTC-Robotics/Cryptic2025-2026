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


// assuming red alliance
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

        double t = 23.5; // 23.5 inches per tile
        // Flipped over x=0: x -> -x, angle 135° -> 45°
        Pose2d initialPose = new Pose2d(t*(2.0), 2.4*t, Math.toRadians(45));

        MecanumDrive drive = new MecanumDrive(hardwareMap, initialPose);

        // Flipped coordinates: x -> -x
        double ballX = (1.5)*t, ballY = 0.5*t+10;
        double scoreX = t, scoreY = t + 6;
        double tx = 3*t, ty = 3*t;

        TrajectoryActionBuilder scorePreloaded = drive.actionBuilder(initialPose)
                // Flipped: 120° -> 60°
                .strafeToLinearHeading(new Vector2d(scoreX - 12, ballY), Math.toRadians(60))
                .waitSeconds(0.5) // TESTING PURPOSES

                //start shooting
                .stopAndAdd(robot.scoringActions.scanSpin(robot))
                .waitSeconds(0.5) // TESTING PURPOSES
                .stopAndAdd(robot.scoringActions.scanSpin(robot))
                .waitSeconds(0.5) // TESTING PURPOSES
                .stopAndAdd(robot.scoringActions.scanSpin(robot))
                .waitSeconds(0.5) // TESTING PURPOSES
                .stopAndAdd(robot.scoringActions.prepareShot(tx, ty, robot, drive, 0.3))
                .waitSeconds(0.5) //KEEP THIS
                .stopAndAdd(robot.scoringActions.launch(tx, ty, robot, drive))
                .waitSeconds(0.5)
                .stopAndAdd(robot.scoringActions.lowerScoop(robot))
                .waitSeconds(0.5)
                .stopAndAdd(robot.scoringActions.launch(tx, ty, robot, drive))
                .waitSeconds(0.5)
                .stopAndAdd(robot.scoringActions.lowerScoop(robot))
                .waitSeconds(0.5)
                .stopAndAdd(robot.scoringActions.launch(tx, ty, robot, drive))
                .waitSeconds(0.5)
                .stopAndAdd(robot.scoringActions.lowerScoop(robot))
                .stopAndAdd(robot.scoringActions.stopFlywheel(robot))
                .stopAndAdd(robot.scoringActions.zeroTurret(robot))

                .waitSeconds(1) // TESTING PURPOSES
                ;

        TrajectoryActionBuilder intake3 = scorePreloaded.endTrajectory().fresh()
                // Flipped: 180° -> 0° (or 360°, using 0°)
                .strafeToLinearHeading(new Vector2d(scoreX - 15, ballY - 12), Math.toRadians(0))
                .stopAndAdd(robot.scoringActions.intakeSpin(robot))
                .strafeToLinearHeading(new Vector2d(ballX-2, ballY - 12), Math.toRadians(0))
                .stopAndAdd(robot.scoringActions.intake(robot))
                .stopAndAdd(robot.scoringActions.scanSpin(robot))
                .waitSeconds(0.5) // TESTING PURPOSES
                .strafeToConstantHeading(new Vector2d(ballX + 4, ballY - 12))
                .stopAndAdd(robot.scoringActions.scanSpin(robot))
                .waitSeconds(0.5) // TESTING PURPOSES
                .strafeToConstantHeading(new Vector2d(ballX + 10, ballY - 12))
                .stopAndAdd(robot.scoringActions.scanSpin(robot))
                .waitSeconds(0.5) // TESTING PURPOSES
                .stopAndAdd(robot.scoringActions.stopSpin(robot))
                ;

        TrajectoryActionBuilder clearRamp = intake3.endTrajectory().fresh()
                // Flipped: 180° -> 0°
                .strafeToLinearHeading(new Vector2d(2.0*t, -16), Math.toRadians(0))
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