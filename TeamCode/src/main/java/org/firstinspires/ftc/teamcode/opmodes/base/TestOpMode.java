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
        stopRequested = false;
        this.msStuckDetectInit = 20000;
        this.msStuckDetectInitLoop = 20000;
        EnumMap<DriveSystem.MotorNames, DcMotor> driveMap = new EnumMap<>(DriveSystem.MotorNames.class);
        for (DriveSystem.MotorNames name : DriveSystem.MotorNames.values()) {
            driveMap.put(name, hardwareMap.get(DcMotor.class, name.toString()));
        }
        driveSystem = new DriveSystem(driveMap, hardwareMap.get(BNO055IMU.class, "imu"));

    }
}

