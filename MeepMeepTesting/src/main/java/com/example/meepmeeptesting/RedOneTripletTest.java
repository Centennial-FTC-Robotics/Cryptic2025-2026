package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class RedOneTripletTest {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(800);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                .setConstraints(50, 30, Math.toRadians(180), Math.toRadians(180), 15)
                .build();

        double t = 23.5; // 23.5 inches per tile
        Pose2d initialPose = new Pose2d(t * (0.5), (-2.6) * t, Math.toRadians(90));

        double scoreX = 0.5 * t;
        double scoreY = -2.6 * t + 4;

        myBot.runAction(
                myBot.getDrive().actionBuilder(initialPose)
                        // Score Preloaded trajectory
                        .strafeToLinearHeading(new Vector2d(scoreX, scoreY), Math.toRadians(90))
                        // Simulate all the scoring actions
                        // 3x scanSpin (0.5s each) + prepareShot + 3x launch cycles
                        // scanSpin: 1.5s total
                        // prepareShot wait: 1.0s
                        // launch cycle 1: launch (1.0s) + lowerScoop (1.0s) = 2.0s
                        // launch cycle 2: launch (1.0s) + lowerScoop (1.0s) = 2.0s
                        // launch cycle 3: launch (1.0s) + lowerScoop (1.0s) = 2.0s
                        // stopFlywheel + zeroTurret + stopSpin: 1.0s
                        // Total: 1.5 + 1.0 + 2.0 + 2.0 + 2.0 + 1.0 = 9.5s
                        .waitSeconds(9.5)

                        // Move Out trajectory
                        .strafeToLinearHeading(new Vector2d(2 * t, -2.5 * t), Math.toRadians(0))
                        .build()
        );

        meepMeep.setBackground(MeepMeep.Background.FIELD_INTO_THE_DEEP_OFFICIAL)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}