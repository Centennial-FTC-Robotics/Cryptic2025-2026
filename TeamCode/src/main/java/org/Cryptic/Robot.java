package org.Cryptic;

import org.Cryptic.Commands.BaseActions;
import org.Cryptic.Commands.ScoringActions;
import org.Cryptic.Subsystems.Camera;
import org.Cryptic.Subsystems.Drivetrain;
import org.Cryptic.Subsystems.IMU;
import org.Cryptic.Subsystems.Outtake;
import org.Cryptic.Subsystems.Intake;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

public class Robot {
    public Drivetrain dt = new Drivetrain();
    public Intake intake = new Intake();

    public Outtake outtake = new Outtake();
    public IMU imu = new IMU();

    public Camera camera = new Camera();

    public BaseActions baseActions = new BaseActions();

    public Subsystem[] subsystems = new Subsystem[] {
            dt,
            intake,
            outtake,
            imu,
            baseActions,
            camera
    };
    public ScoringActions scoringActions = new ScoringActions();

    public int currentIndex = 0; // index at either intake/outtake positions

    public int currentIntakeIndex = 0;
    public int currentOuttakeIndex = 0;
    public int targetIndex; // index of motif to shoot

    public int[] currentBalls = {-1, -1, -1}; // 1 for green 0 for purple -1 for empty
    // currentBalls[i] is ball at intake if i/3 is the rotation of it

    public int motif = 21; // 21 for GPP, 22 for PGP, 23 for PPG

    public final int encoderTicks = 6600;
    public final int[] targetPosition = {0, encoderTicks/6, encoderTicks*2/6, encoderTicks*3/6, encoderTicks*4/6, encoderTicks*5/6};
    // intake0, outtake2, intake1, outtake0, intake2, outtake1
    public boolean rotating = false;
    public boolean rotatingIntake = false;
    public boolean rotatingOuttake = false;

    public double SPINDEXER_SPEED = 10_000.0;
    public double SPINDEXER_MIN_SPEED = 0.07;

    public double currentTurretRadians;

    public void initialize(LinearOpMode opmode) throws InterruptedException {

        for(Subsystem subsystem : subsystems) {
            subsystem.preInit(opmode, this);
        }
    }

}