package org.firstinspires.ftc.teamcode.components;

import android.util.*;

import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.components.base.System;

import com.qualcomm.hardware.rev.Rev2mDistanceSensor;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.components.scale.ExponentialRamp;
import org.firstinspires.ftc.teamcode.components.scale.Point;
import org.firstinspires.ftc.teamcode.components.scale.Ramp;
import org.firstinspires.ftc.teamcode.components.base.System;
import org.firstinspires.ftc.teamcode.components.ColorSystem;
import org.firstinspires.ftc.teamcode.components.IMUSystem;

public class LidarSystem {

    private static final int ARM_THRESHOLD = 200;
    private static final int INTAKE_THRESHOLD = 2;

    private DistanceSensor lidar;
    private DistanceSensor lidar2;
    HardwareMap hwmap;


    public LidarSystem(OpMode opMode, DistanceSensor distanceSensor) {
        this.hwmap = opMode.hardwareMap;
        this.lidar = distanceSensor;
        this.lidar2 = distanceSensor;
        initSystem();
    }
    public void initSystem() {
        lidar = hwmap.get(DistanceSensor.class, "sensor_range");
        lidar2 = hwmap.get(DistanceSensor.class, "sensor_range2");
    }

    public double getDistance1() {
        return lidar.getDistance(DistanceUnit.INCH);
    }

    public double getDistance2() {
        return lidar2.getDistance(DistanceUnit.INCH);
    }

    public void debug() {
        Log.d("distanceSensor", "Lidar 1 Distance: " + lidar.getDistance(DistanceUnit.INCH));
        Log.d("distanceSensor", "Lidar 2 Distance: " + lidar2.getDistance(DistanceUnit.INCH));

        if (lidar != null) {
            Log.d("distanceSensor", "ERROR! Lidar 1 distance is null!");
        }

        if (lidar2 != null) {
            Log.d("distanceSensor", "ERROR! Lidar 2 distance is null!");
        }

    }

    public boolean inRangeArmLidar1() {
        return lidar.getDistance(DistanceUnit.INCH) >= ARM_THRESHOLD;
    }

    public boolean inRangeArmLidar2() {
        return lidar2.getDistance(DistanceUnit.INCH) >= ARM_THRESHOLD;
    }

    public boolean inRangeIntakeLidar1() {
        return lidar.getDistance((DistanceUnit.INCH)) <= INTAKE_THRESHOLD;
    }

    public boolean inRangeIntakeLidar2() {
        return lidar2.getDistance((DistanceUnit.INCH)) <= INTAKE_THRESHOLD;
    }

}