package org.firstinspires.ftc.teamcode.components;

import android.util.*;

import com.qualcomm.hardware.rev.Rev2mDistanceSensor;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

import java.util.EnumMap;

public class LidarNavigationSystem {

    private static final int ARM_THRESHOLD = 200;
    private static final int INTAKE_THRESHOLD = 2;

    public enum SensorNames {
        LEFT, RIGHT, BACK
    }

    public EnumMap<SensorNames, Rev2mDistanceSensor> lidars;



    public LidarNavigationSystem(EnumMap<SensorNames, Rev2mDistanceSensor> lidars) {
        this.lidars = lidars;
    }


    public void debug(DistanceSensor lidar) {
        Log.d("distanceSensor", "Distance: " + lidar.getDistance(DistanceUnit.INCH));

        if (lidar != null) {
            Log.d("distanceSensor", "ERROR! " + lidar + " distance is null!");
        }

    }

    // when passing a parameter into methods below,
    // do something like "SensorNames.get(LEFT)"
    // or else i will break into your house and eat your food
    // - jack (long hair man)

    public double getDistance(Rev2mDistanceSensor lidar) {
        return lidar.getDistance(DistanceUnit.INCH);
    }

    public boolean inRangeArm(Rev2mDistanceSensor lidar) {
        return lidar.getDistance(DistanceUnit.INCH) >= ARM_THRESHOLD;
    }

    public boolean inRangeIntake(Rev2mDistanceSensor lidar) {
        return lidar.getDistance((DistanceUnit.INCH)) <= INTAKE_THRESHOLD;
    }

}