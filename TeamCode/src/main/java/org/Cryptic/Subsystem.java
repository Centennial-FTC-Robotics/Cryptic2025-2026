package org.Cryptic;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

public abstract class Subsystem {

    public Robot robot;

    public void preInit(LinearOpMode opmode, Robot robot) throws InterruptedException {
        this.robot = robot;
        init(opmode);
    }

    public void update() {}

    public abstract void init(LinearOpMode opmode) throws InterruptedException;

}