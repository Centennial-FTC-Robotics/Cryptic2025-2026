package org.Cryptic;

import org.Cryptic.Commands.BaseActions;
import org.Cryptic.Commands.SampleActions;
import org.Cryptic.Subsystems.Camera;
import org.Cryptic.Subsystems.Drivetrain;
import org.Cryptic.Subsystems.IMU;
import org.Cryptic.Subsystems.Outtake;
import org.Cryptic.Subsystems.Intake;
import org.xml.sax.helpers.NamespaceSupport;

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
            imu,
            baseActions,
            camera
    };
    public SampleActions sampleActions = new SampleActions();

    public int currentIndex; // index of the intake i think

    public int[] currentBalls;

    public int motif;
    public void initialize(LinearOpMode opmode) throws InterruptedException {

        for(Subsystem subsystem : subsystems) {
            subsystem.preInit(opmode, this);
        }
    }
}