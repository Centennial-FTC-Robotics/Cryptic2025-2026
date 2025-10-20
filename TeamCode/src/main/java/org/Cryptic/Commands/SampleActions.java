package org.Cryptic.Commands;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;

import org.Cryptic.Robot;

public class SampleActions {

    public class positionToScore implements Action {
        private boolean initialized = false;
        private Robot robot = new Robot();

        public positionToScore(Robot robot) {
            this.robot = robot;
        }

        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            if (!initialized) {
                initialized = true;
                robot.outtake.outtakeSample();
            }
        }
    }

    public Action positionToScore(Robot robot) {
        return new positionToScore(robot);
    }
}
