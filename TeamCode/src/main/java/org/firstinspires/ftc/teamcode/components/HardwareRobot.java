package org.firstinspires.ftc.teamcode.components;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.teamcode.opmodes.base.BaseOpMode;

import java.util.EnumMap;

public class HardwareRobot {
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
        public HardwareRobot(){

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
        vuforia = setCamera(Vuforia.CameraChoice.CAM_RIGHT);
        // DistanceSensor distanceSensor2;
        // DistanceSensor distanceSensor3;
        // ColorSensor colorSensor;

    }

    protected Vuforia setCamera(Vuforia.CameraChoice cameraChoice){
        vuforia = new Vuforia(camMap.get(cameraChoice), cameraViewId);
        skystone = vuforia.targetsSkyStone.get(0);
        return vuforia;
    }
}
