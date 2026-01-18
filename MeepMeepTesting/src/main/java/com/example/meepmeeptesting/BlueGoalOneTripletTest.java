package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class BlueGoalOneTripletTest {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(800);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                .setConstraints(50, 30, Math.toRadians(180), Math.toRadians(180), 18)
                .build();

        double t = 23.5;

        Pose2d initialPose = new Pose2d(t * (-2.0), 2.4 * t, Math.toRadians(135));

        double ballX = (-1.5) * t, ballY = 0.5 * t + 10;
        double scoreX = -t, scoreY = t + 6;

        myBot.runAction(
                myBot.getDrive().actionBuilder(initialPose)

                        // matches: .strafeToLinearHeading(new Vector2d(scoreX + 12, ballY), 120deg)
                        .strafeToLinearHeading(new Vector2d(scoreX + 12, ballY), Math.toRadians(120))

                        // your real auto does a bunch of scan/prepare/launch actions here
                        // in MeepMeep, just pause to represent the time spent shooting
                        .waitSeconds(0.6 * 3 + 0.6 + 0.6 * 3 + 1.0) // rough: scans + prepare + launches + extra

                        // matches intake3:
                        .strafeToLinearHeading(new Vector2d(scoreX + 15, ballY - 12), Math.toRadians(180))
                        .waitSeconds(0.2) // stand-in for intakeSpin
                        .strafeToLinearHeading(new Vector2d(ballX + 2, ballY - 12), Math.toRadians(180))
                        .waitSeconds(0.2) // stand-in for intake
                        .waitSeconds(0.5) // stand-in for scanSpin
                        .strafeToConstantHeading(new Vector2d(ballX - 4, ballY - 12))
                        .waitSeconds(0.5) // stand-in for scanSpin
                        .strafeToConstantHeading(new Vector2d(ballX - 10, ballY - 12))
                        .waitSeconds(0.5) // stand-in for scanSpin

                        // optional: clearRamp (you have it built but commented out in run)
                        // .strafeToLinearHeading(new Vector2d(-2.0 * t, -16), Math.toRadians(0))

                        .build()
        );

        meepMeep.setBackground(MeepMeep.Background.FIELD_INTO_THE_DEEP_OFFICIAL)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}
