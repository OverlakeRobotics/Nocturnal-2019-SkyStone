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
import org.firstinspires.ftc.teamcode.components.HardwareRobot;
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
    protected HardwareRobot robot;
    protected int cameraViewId;

    public void init(){
        robot = new HardwareRobot();
        robot.init(hardwareMap);

        driveSystem = robot.driveSystem;
        camMap = robot.camMap;
        vuforia = robot.vuforia;
        cameraViewId = robot.cameraViewId;
        // DistanceSensor distanceSensor2;
        // DistanceSensor distanceSensor3;
        // ColorSensor colorSensor;
    }

    protected Vuforia setCamera(CameraChoice cameraChoice){
        vuforia = new Vuforia(camMap.get(cameraChoice), cameraViewId);
        skystone = vuforia.targetsSkyStone.get(0);
        return vuforia;
    }
}
