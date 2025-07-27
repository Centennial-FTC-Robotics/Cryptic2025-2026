package org.Cryptic;

import org.Cryptic.Commands.BaseActions;
import org.Cryptic.Commands.SpecimenActions;
import org.Cryptic.Commands.SampleActions;
import org.Cryptic.Commands.SpecimenCommands;
import org.Cryptic.Subsystems.ClawArm;
import org.Cryptic.Subsystems.Drivetrain;
import org.Cryptic.Subsystems.DrivetrainNoRR;
import org.Cryptic.Subsystems.IMU;
import org.Cryptic.Subsystems.IntakeSlides;
import org.Cryptic.Subsystems.Outtake;
import org.Cryptic.Subsystems.VerticalSlides;
import org.Cryptic.Subsystems.Intake;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

public class Robot {
    public Drivetrain dt = new Drivetrain();
    public VerticalSlides verticalSlides = new VerticalSlides();
    public Intake intake = new Intake();
    public IntakeSlides intakeSlides = new IntakeSlides();
    public ClawArm clawArm = new ClawArm();
    public Outtake outtake = new Outtake(clawArm);
    public IMU imu = new IMU();
    public DrivetrainNoRR dtNoRR = new DrivetrainNoRR();
    public SpecimenCommands specimenCommands = new SpecimenCommands();
    public BaseActions baseActions = new BaseActions();
    public SpecimenActions specimenActions = new SpecimenActions();
    public SampleActions sampleActions = new SampleActions();

    public Subsystem[] subsystems = new Subsystem[] {
            dt,
            clawArm,
            // outtake depends on clawArm
            outtake,
            intake,
            verticalSlides,
            // init slides after intake so intake pitch servos can move up in time
            intakeSlides,
            imu,
            specimenCommands,
            baseActions,
            specimenActions,
            sampleActions
    };

    public void initialize(LinearOpMode opmode) throws InterruptedException {

        for(Subsystem subsystem : subsystems) {
            subsystem.preInit(opmode, this);
        }
    }
}