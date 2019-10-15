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

    DistanceSensor lidar1;
    DistanceSensor lidar2;
    HardwareMap hwmap;
    Rev2mDistanceSensor sensorTimeOfFlight;

    public LidarNavigationSystem(Rev2mDistanceSensor lidar1, Rev2mDistanceSensor lidar2) {
        this.lidar1 = lidar1;
        this.lidar2 = lidar2;
    }


    public void debug(DistanceSensor lidar) {
        Log.d("distanceSensor", "Distance: " + lidar.getDistance(DistanceUnit.INCH));

        if (lidar != null) {
            Log.d("distanceSensor", "ERROR! " + lidar + " distance is null!");
        }

    }
    public double getDistance(DistanceSensor lidar) {
        return lidar.getDistance(DistanceUnit.INCH);
    }

    public boolean inRangeArm(DistanceSensor lidar) {
        return lidar.getDistance(DistanceUnit.INCH) >= ARM_THRESHOLD;
    }

    public boolean inRangeIntake(DistanceSensor lidar) {
        return lidar.getDistance((DistanceUnit.INCH)) <= INTAKE_THRESHOLD;
    }

}