package org.Cryptic.Commands;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;

import org.Cryptic.Robot;

public class ScoringActions {

    private long startTime;

    private void initTime(){
        startTime = System.currentTimeMillis();
    }
    public boolean hasBeenTime(int milli){
        return System.currentTimeMillis() - startTime >= milli;
    }

    public class intake implements Action {
        private boolean initialized = false;

        private Robot robot;

        public intake(Robot robot) {
            this.robot = robot;
        }

        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            if (!initialized) {
                initTime();
                initialized = true;
            }
            robot.intake.intakeAuto(850);

            return (!hasBeenTime(300));
        }
    }

    public Action intake(Robot robot) {
        return new intake(robot);
    }
    public class scanSpin implements Action {
        private boolean initialized = false;
        private Robot robot;

        public scanSpin(Robot robot) {
            this.robot = robot;
        }

        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            if (!initialized) {
                initialized = true;
                robot.intake.scanSpinAuto(); // adjust RPM later
                // initTime();
            }
            return false;
            // return (!hasBeenTime(300));
        }
    }

    public Action scanSpin(Robot robot) {
        return new scanSpin(robot);
    }

    public class stopSpin implements Action {
        private boolean initialized = false;
        private Robot robot;

        public stopSpin(Robot robot) {
            this.robot = robot;
        }

        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            if (!initialized) {
                initialized = true;
                robot.intake.stopSpinAuto(); // adjust RPM later
                // initTime();
            }
            return false;
            // return (!hasBeenTime(300));
        }
    }

    public Action stopSpin(Robot robot) {
        return new stopSpin(robot);
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
                robot.outtake.launchAuto(tx, ty, robot.dt.drivebase);
//                initTime();
            }
//          return (!hasBeenTime(300));
            return false;
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
