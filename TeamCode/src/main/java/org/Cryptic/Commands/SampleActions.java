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
    public class intakeComplete implements Action {
        private boolean initialized = false;
        private Robot robot;

        public intakeComplete(Robot robot) {
            this.robot = robot;
        }

        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            if (!initialized) {
                initialized = true;
                robot.intake.intakeComplete(1000); // adjust RPM later
                initTime();
            }
            return (!hasBeenTime(300));
        }
    }

    public Action intakeComplete(Robot robot) {
        return new intakeComplete(robot);
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
        private final Robot robot;
        private final double tx, ty;

        public launch(double tx, double ty, Robot robot) {
            this.robot = robot;
            this.tx = tx;
            this.ty = ty;
        }

        public boolean run(@NonNull TelemetryPacket packet) {
            if (!initialized) {
                initialized = true;
                robot.outtake.launch(tx, ty, robot.dt.drivebase);
                initTime();
            }
            return (!hasBeenTime(300));
        }
    }


    public Action launch(double tx, double ty, Robot robot) {
        return new launch(tx, ty, robot);
    }



    public class aimAtGoal implements Action {
        private final Robot robot;
        private final double tx, ty;
        private boolean initialized = false;
        private long startTime;

        public aimAtGoal(double tx, double ty, Robot robot) {
            this.robot = robot;
            this.tx = tx;
            this.ty = ty;
        }

        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            if (!initialized) {
                initialized = true;
                startTime = System.currentTimeMillis();
            }

            robot.outtake.autoUpdateAim(tx, ty, robot.dt.drivebase);

            return System.currentTimeMillis() - startTime < 5000; // adjust later
        }
    }

    public Action aimAtGoal(double tx, double ty, Robot robot) {
        return new aimAtGoal(tx, ty, robot);
    }



}
