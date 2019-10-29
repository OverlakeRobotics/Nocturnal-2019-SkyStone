package org.firstinspires.ftc.teamcode.components;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;


import java.util.EnumMap;

public class Hardware {
        /* Public OpMode members. */
        public DriveSystem driveSystem;
        public EnumMap<Vuforia.CameraChoice, WebcamName> camMap;
        public Vuforia vuforia;
        public int cameraViewId;
        public VuforiaTrackable skystone;
        // public VuforiaTrackable rearPerimeter;
        /* local OpMode members. */
        HardwareMap mHardwareMap = null;

        /* Constructor */
        public Hardware(){

        }

    public void init(HardwareMap phwMap){
        mHardwareMap = phwMap;
        EnumMap<DriveSystem.MotorNames, DcMotor> driveMap = new EnumMap<>(DriveSystem.MotorNames.class);
        for(DriveSystem.MotorNames name : DriveSystem.MotorNames.values()){
            driveMap.put(name, mHardwareMap.get(DcMotor.class, name.toString()));
        }
        driveSystem = new DriveSystem(driveMap, mHardwareMap.get(BNO055IMU.class, "imu"));
        camMap = new EnumMap<>(Vuforia.CameraChoice.class);

        for(Vuforia.CameraChoice name : Vuforia.CameraChoice.values()){
            camMap.put(name, mHardwareMap.get(WebcamName.class, name.toString()));
        }

        cameraViewId = mHardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", mHardwareMap.appContext.getPackageName());
        // DistanceSensor distanceSensor2;
        // DistanceSensor distanceSensor3;
        // ColorSensor colorSensor;

    }
}
