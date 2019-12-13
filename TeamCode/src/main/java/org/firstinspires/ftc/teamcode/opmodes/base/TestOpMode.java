package org.firstinspires.ftc.teamcode.opmodes.base;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import org.firstinspires.ftc.teamcode.components.DriveSystem;

import java.util.EnumMap;

public abstract class TestOpMode extends OpMode {

    protected DriveSystem driveSystem;
    private boolean stopRequested;

    public void init() {

    }

    @Override
    public void loop() {
        driveSystem.driveToPosition(2000, DriveSystem.Direction.RIGHT, 1.0);
    }
}

