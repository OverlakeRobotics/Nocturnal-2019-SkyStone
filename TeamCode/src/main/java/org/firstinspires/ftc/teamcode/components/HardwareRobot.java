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

import java.util.EnumMap;

public class HardwareRobot {
        /* Public OpMode members. */
        public DcMotor leftDrive = null;
        public DcMotor rightDrive = null;
        public DcMotor leftArm = null;

        /* local OpMode members. */
        HardwareMap mHardwareMap = null;
        private ElapsedTime period  = new ElapsedTime();

        /* Constructor */
        public HardwarePushbot(){

        }

    protected DriveSystem driveSystem;
    protected EnumMap<Vuforia.CameraChoice, WebcamName> camMap;
    protected Vuforia vuforia;
    protected VuforiaTrackable skystone;
    protected VuforiaTrackable rearPerimeter;

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

        int cameraMonitorViewId = mHardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        vuforia = setCamera(Vuforia.CameraChoice.CAM_RIGHT, cameraMonitorViewId);
        DistanceSensor distanceSensor2;
        DistanceSensor distanceSensor3;
        ColorSensor colorSensor;

    }
}
