package org.firstinspires.ftc.teamcode.components;

import android.util.*;

import com.qualcomm.hardware.rev.Rev2mDistanceSensor;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class LidarNavigationSystem {

    private static final int ARM_THRESHOLD = 200;
    private static final int INTAKE_THRESHOLD = 2;

    DistanceSensor lidar;
    DistanceSensor lidar2;
    HardwareMap hwmap;
    Rev2mDistanceSensor sensorTimeOfFlight;

    public LidarNavigationSystem(OpMode opMode) {
        this.hwmap = opMode.hardwareMap;
        initSystem();
    }

    public void initSystem() {
        lidar = hwmap.get(DistanceSensor.class, "sensor_range1");
        lidar2 = hwmap.get(DistanceSensor.class, "sensor_range2");
        sensorTimeOfFlight = (Rev2mDistanceSensor)lidar;
    }

    public void debug() {
        Log.d("distanceSensor", "Distance: " + lidar.getDistance(DistanceUnit.INCH));

        if (lidar != null) {
            Log.d("distanceSensor", "ERROR! Lidar 1 distance is null!");
        }

    }
    public double getDistance1() {
        return lidar.getDistance(DistanceUnit.INCH);
    }

    public double getDistance2() {
        return lidar2.getDistance(DistanceUnit.INCH);
    }

    public boolean inRangeArm() {
        return lidar.getDistance(DistanceUnit.INCH) >= ARM_THRESHOLD;
    }

    public boolean inRangeIntake() {
        return lidar.getDistance((DistanceUnit.INCH)) <= INTAKE_THRESHOLD;
    }

}