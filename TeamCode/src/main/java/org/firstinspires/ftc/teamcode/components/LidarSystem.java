package org.firstinspires.ftc.teamcode.components;

import android.util.*;

import com.qualcomm.robotcore.hardware.DistanceSensor;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class LidarSystem {

    private static final int ARM_THRESHOLD = 200;
    private static final int INTAKE_THRESHOLD = 2;

    DistanceSensor lidar;
    DistanceSensor lidar2;

    public LidarSystem(DistanceSensor distanceSensor) {
        this.lidar = distanceSensor;
        this.lidar2 = distanceSensor;
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