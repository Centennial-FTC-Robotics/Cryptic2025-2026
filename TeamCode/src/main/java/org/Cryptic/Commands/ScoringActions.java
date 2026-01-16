package org.Cryptic.Commands;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;

import org.Cryptic.Robot;
import org.firstinspires.ftc.teamcode.MecanumDrive;

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

        private MecanumDrive drive;

        public launch(double tx, double ty, Robot robot, MecanumDrive drive) {
            this.robot = robot;
            this.tx = tx;
            this.ty = ty;
            this.drive = drive;
        }

        public boolean run(@NonNull TelemetryPacket packet) {
            if (!initialized) {
                initialized = true;
                robot.outtake.launchAuto(tx, ty, drive);
//                initTime();
            }
//          return (!hasBeenTime(300));
            return false;
        }
    }


    public Action launch(double tx, double ty, Robot robot, MecanumDrive drive) {
        return new launch(tx, ty, robot, drive);
    }

    public class prepareShot implements Action {
        private final Robot robot;
        private final double tx, ty;
        private boolean initialized = false;
        private long startTime;

        private MecanumDrive drive;

        public prepareShot(double tx, double ty, Robot robot, MecanumDrive drive) {
            this.robot = robot;
            this.tx = tx;
            this.ty = ty;
            this.drive = drive;
        }

        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            if (!initialized) {
                initialized = true;
                startTime = System.currentTimeMillis();
            }

            robot.outtake.autoUpdateAim(tx, ty, drive);

            return System.currentTimeMillis() - startTime < 5000; // adjust later
        }
    }

    public Action prepareShot(double tx, double ty, Robot robot, MecanumDrive drive) {
        return new prepareShot(tx, ty, robot, drive);
    }


    public class stopFlywheel implements Action {
        private final Robot robot;
        private boolean initialized = false;
        private long startTime;

        public stopFlywheel(Robot robot) {
            this.robot = robot;
        }

        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            if (!initialized) {
                initialized = true;
                startTime = System.currentTimeMillis();
            }

            robot.outtake.stopFlywheel();

            return false;
        }
    }

    public Action stopFlywheel(Robot robot) {
        return new stopFlywheel(robot);
    }

    public class lowerScoop implements Action {
        private Robot robot;
        private boolean initialized = false;
        public lowerScoop(Robot robot) {
            this.robot = robot;
        }

        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            if (!initialized) {
                initialized = true;
                robot.outtake.moveTransfer();
            }
            return false;
        }
    }

    public Action lowerScoop(Robot robot) {
        return new lowerScoop(robot);
    }
    public class robotUpdate implements Action {
        private boolean initialized = false;
        private Robot robot;

        public robotUpdate (Robot robot) {
            this.robot = robot;
        }

        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            if (!initialized) {
                initialized = true;
            }
            robot.intake.update();
            robot.outtake.update();

            // MUST BE SET TO TRUE IF YOU WANT IT TO RUN ALL THE TIME
            return true;
        }
    }

    public Action robotUpdate(Robot robot) {
        return new robotUpdate(robot);
    }
}
