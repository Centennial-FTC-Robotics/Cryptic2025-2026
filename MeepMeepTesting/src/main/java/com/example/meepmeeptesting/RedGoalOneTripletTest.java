package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class RedGoalOneTripletTest {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(800);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                .setConstraints(50, 30, Math.toRadians(180), Math.toRadians(180), 18)
                .build();

        double t = 23.5;

        // MIRROR X:
        // X is negated: t*(-2.0) -> t*(2.0)
        // Y stays same: 2.4*t
        // Heading is reflected: 180 - 135 = 45 degrees
        Pose2d initialPose = new Pose2d(t * (2.0), 2.4 * t, Math.toRadians(45));

        // Define base variables (negated X compared to original)
        double ballX = (1.5) * t; // Original was -1.5 * t
        double ballY = 0.5 * t + 10; // Y stays exactly the same
        double scoreX = t; // Original was -t

        myBot.runAction(
                myBot.getDrive().actionBuilder(initialPose)

                        // 1. Shooting Position
                        // Original X: scoreX + 12. Mirrored X: scoreX - 12 (flip the offset too)
                        // Original Heading: 120. Mirrored Heading: 180 - 120 = 60
                        .strafeToLinearHeading(new Vector2d(scoreX - 12, ballY), Math.toRadians(60))

                        // Pause for shooting
                        .waitSeconds(0.6 * 3 + 0.6 + 0.6 * 3 + 1.0)

                        // 2. Intake Setup
                        // Original X: scoreX + 15 -> Mirrored: scoreX - 15
                        // Y stays same (ballY - 12)
                        // Original Heading: 180 -> Mirrored: 180 - 180 = 0
                        .strafeToLinearHeading(new Vector2d(scoreX - 15, ballY - 12), Math.toRadians(0))

                        .waitSeconds(0.2)

                        // 3. Intake
                        // Original X: ballX + 2 -> Mirrored: ballX - 2
                        // Heading: 0
                        .strafeToLinearHeading(new Vector2d(ballX - 2, ballY - 12), Math.toRadians(0))

                        .waitSeconds(0.2)
                        .waitSeconds(0.5)

                        // 4. Scan/Strafe
                        // Original X: ballX - 4 -> Mirrored: ballX + 4
                        .strafeToConstantHeading(new Vector2d(ballX + 4, ballY - 12))

                        .waitSeconds(0.5)

                        // 5. Scan/Strafe
                        // Original X: ballX - 10 -> Mirrored: ballX + 10
                        .strafeToConstantHeading(new Vector2d(ballX + 10, ballY - 12))

                        .waitSeconds(0.5)

                        .build()
        );

        meepMeep.setBackground(MeepMeep.Background.FIELD_INTO_THE_DEEP_OFFICIAL)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}