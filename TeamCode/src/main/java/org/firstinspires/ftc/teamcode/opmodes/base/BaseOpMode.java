package org.firstinspires.ftc.teamcode.opmodes.base;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DistanceSensor;

import org.firstinspires.ftc.robotcore.external.hardware.camera.Camera;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.teamcode.components.DriveSystem;
import org.firstinspires.ftc.teamcode.components.IMUSystem;
import org.firstinspires.ftc.teamcode.components.Vuforia;
import org.firstinspires.ftc.teamcode.components.Vuforia.CameraChoice;

import java.util.EnumMap;

public abstract class BaseOpMode extends OpMode {

    protected DriveSystem driveSystem;
    protected EnumMap<Vuforia.CameraChoice, WebcamName> camMap;
    protected Vuforia vuforia;
    protected VuforiaTrackable skystone;
    protected VuforiaTrackable rearPerimeter;

    public void init(){
        EnumMap<DriveSystem.MotorNames, DcMotor> driveMap = new EnumMap<>(DriveSystem.MotorNames.class);
        for(DriveSystem.MotorNames name : DriveSystem.MotorNames.values()){
            driveMap.put(name,hardwareMap.get(DcMotor.class, name.toString()));
        }
        driveSystem = new DriveSystem(driveMap, hardwareMap.get(BNO055IMU.class, "imu"));
        camMap = new EnumMap<>(Vuforia.CameraChoice.class);

        for(Vuforia.CameraChoice name : Vuforia.CameraChoice.values()){
            camMap.put(name,hardwareMap.get(WebcamName.class, name.toString()));
        }

        vuforia = setCamera(Vuforia.CameraChoice.CAM_RIGHT);
        DistanceSensor distanceSensor2;
        DistanceSensor distanceSensor3;
        ColorSensor colorSensor;


    }

    protected Vuforia setCamera(CameraChoice cameraChoice){
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        vuforia = new Vuforia(camMap.get(cameraChoice), cameraMonitorViewId);
        skystone = vuforia.targetsSkyStone.get(0);
        return vuforia;
    }
}
