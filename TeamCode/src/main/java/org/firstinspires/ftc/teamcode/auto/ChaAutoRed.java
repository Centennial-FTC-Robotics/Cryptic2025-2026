package org.firstinspires.ftc.teamcode.auto;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.Cryptic.Commands.SampleActions;
import org.Cryptic.Robot;
import org.firstinspires.ftc.teamcode.MecanumDrive;


// assuming blue alliance
public class ChaAutoRed extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {

        Robot robot = new Robot();
        robot.initialize(this);

        double t = 23.5;
        Pose2d initialPose = new Pose2d((t*1 - 4), (t*5 + 6), Math.toRadians(315));
        MecanumDrive drive = new MecanumDrive(hardwareMap, initialPose);

        double inPerTick = 1;

        double ballX = t+6, ballY = 3*t+6; // TODO coords of the goal
        double scoreX = t*3, scoreY = t*5;

        // 21 for GPP, 22 for PGP, 23 for PPG

        TrajectoryActionBuilder scoop = drive.actionBuilder(initialPose)
                .stopAndAdd(robot.sampleActions.getMotif(robot))
                .splineToLinearHeading(new Pose2d(t * -1,(1.5 * t) * (robot.motif - 21 + 1),Math.toRadians(180)), Math.toRadians(270));


        Action scoopToScore = scoop.build();

        Actions.runBlocking(
                new ParallelAction(
                        new SequentialAction(
                                scoopToScore
                        )
                )
        );
    }
}