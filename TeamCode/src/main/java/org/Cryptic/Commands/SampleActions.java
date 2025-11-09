package org.Cryptic.Commands;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;

import org.Cryptic.Robot;

public class SampleActions {

    private long startTime;

    private void initTime(){
        startTime = System.currentTimeMillis();
    }
    public boolean hasBeenTime(int mili){
        return System.currentTimeMillis() - startTime >= mili;
    }
    public class grabBall implements Action {
        private boolean initialized = false;
        private Robot robot;

        public grabBall(Robot robot) {
            this.robot = robot;
        }

        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            if (!initialized) {
                initialized = true;
                robot.intake.grabBall(10); // TODO what power
                initTime();
            }
            return (!hasBeenTime(300));
        }
    }

    public Action grabBall(Robot robot) {
        return new grabBall(robot);
    }
}
