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
    public boolean hasBeenTime(int milli){
        return System.currentTimeMillis() - startTime >= milli;
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
                robot.intake.grabBall();
                initTime();
            }
            return (!hasBeenTime(300));
        }
    }

    public Action grabBall(Robot robot) {
        return new grabBall(robot);
    }

    public class getMotif implements Action {

        private boolean initialized = false;
        private Robot robot;

        public getMotif(Robot robot) { this.robot = robot; }

        public boolean run(@NonNull TelemetryPacket packet) {
            if (!initialized) {
                initialized = true;
                robot.camera.getMotif();
                initTime();
            }
            return (!hasBeenTime(300));
        }
    }

    public Action getMotif(Robot robot) {
        return new getMotif(robot);
    }

    public class launch implements Action {
        private boolean initialized = false;
        private Robot robot;

        public launch(Robot robot) { this.robot = robot; }

        public boolean run(@NonNull TelemetryPacket packet) {
            if (!initialized) {
                initialized = true;
                robot.outtake.launch(robot.dt.drivebase);
                initTime();
            }
            return (!hasBeenTime(300));
        }
    }

    public Action launch(Robot robot) {
        return new launch(robot);
    }
}
