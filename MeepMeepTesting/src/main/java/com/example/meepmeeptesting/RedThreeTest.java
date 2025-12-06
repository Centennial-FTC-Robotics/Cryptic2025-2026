package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class RedThreeTest {
    public static void main(String[] args) {
        // Test out auto
        MeepMeep meepMeep = new MeepMeep(800);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(50, 30, Math.toRadians(180), Math.toRadians(180), 18)
                .build();

        double t = 23.5;
        Pose2d initialPose = new Pose2d(t*(0.5), -2.6*t, Math.toRadians(90));

        double ballX = (1.5)*t, ballY = -(1.5)*t; // coordinates of the first ball
        double scoreX = 0, scoreY = t*2; // where to score from, in a launch zone
        // double tx = -3*t, ty = 3*t; // coordinates of the goal
        double motifIndex = 0;
        double motifIndex2 = 1;
        double motifIndex3 = 2;

        myBot.runAction(myBot.getDrive().actionBuilder(initialPose)
                .waitSeconds(1)
                .strafeToLinearHeading(new Vector2d(0, t), Math.toRadians(90))
                .waitSeconds(0.5)
                .splineToLinearHeading(new Pose2d(new Vector2d(ballX, ballY + motifIndex*t), Math.toRadians(0)), Math.toRadians(270))
                .waitSeconds(0.5)
                .strafeToConstantHeading(new Vector2d(ballX + 5, ballY + motifIndex*t))
                .waitSeconds(0.5)
                .strafeToConstantHeading(new Vector2d(ballX + 10, ballY + motifIndex*t))
                .waitSeconds(0.5)
                .strafeToConstantHeading(new Vector2d(scoreX, scoreY))
                .waitSeconds(0.5)
                // second
                .splineToLinearHeading(new Pose2d(new Vector2d(ballX, ballY + motifIndex2*t), Math.toRadians(0)), Math.toRadians(270))
                .waitSeconds(0.5)
                .strafeToConstantHeading(new Vector2d(ballX + 5, ballY + motifIndex2*t))
                .waitSeconds(0.5)
                .strafeToConstantHeading(new Vector2d(ballX + 10, ballY + motifIndex2*t))
                .waitSeconds(0.5)
                .strafeToConstantHeading(new Vector2d(scoreX, scoreY))
                .waitSeconds(0.5)
                // last one
                .splineToLinearHeading(new Pose2d(new Vector2d(ballX, ballY + motifIndex3*t), Math.toRadians(0)), Math.toRadians(270))
                .waitSeconds(0.5)
                .strafeToConstantHeading(new Vector2d(ballX + 5, ballY + motifIndex3*t))
                .waitSeconds(0.5)
                .strafeToConstantHeading(new Vector2d(ballX + 10, ballY + motifIndex3*t))
                .waitSeconds(0.5)
                .strafeToConstantHeading(new Vector2d(scoreX, scoreY))
                .waitSeconds(0.5)
                // push the gate
                .splineToLinearHeading(new Pose2d(new Vector2d(2.5*t, 0), Math.toRadians(0)), Math.toRadians(0))
                .build());

        meepMeep.setBackground(MeepMeep.Background.FIELD_INTO_THE_DEEP_OFFICIAL)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}