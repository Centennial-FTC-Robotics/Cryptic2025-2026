package org.Cryptic;

import org.Cryptic.Commands.BaseActions;
import org.Cryptic.Commands.SampleActions;
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
    public SampleActions sampleActions = new SampleActions();

    public int currentIndex = 0; // index at either intake/outtake positions

    public int currentIntakeIndex = 0;
    public int currentOuttakeIndex = 0;
    public int targetIndex; // index of motif to shoot

    public int[] currentBalls = {-1, -1, -1}; // 1 for green 0 for purple -1 for empty
    // currentBalls[i] is ball at intake if i/3 is the rotation of it

    public int motif = 21; // 21 for GPP, 22 for PGP, 23 for PPG

    public int[] targetPosition2 = {0, 8192*120/360, 8192*240/360, 8192*300/360, 8192*180/360, 8192*60/360, 0};
    public int[] targetPosition = {0, 8192/6, 8192*2/6, 8192*3/6, 8192*4/6, 8192*5/6};
    // intake0, outtake2, intake1, outtake0, intake2, outtake1
    public boolean rotating = false;
    public boolean rotatingIntake = false;
    public boolean rotatingOuttake = false;

    public double SPINDEXER_SPEED = 8000.0;
    public double SPINDEXER_MIN_SPEED = 0.08;

    public void initialize(LinearOpMode opmode) throws InterruptedException {

        for(Subsystem subsystem : subsystems) {
            subsystem.preInit(opmode, this);
        }
    }

}