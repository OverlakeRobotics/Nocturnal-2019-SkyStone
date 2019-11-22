package org.firstinspires.ftc.teamcode.opmodes.base;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.components.ArmSystem;
import org.firstinspires.ftc.teamcode.components.DriveSystem;
import org.firstinspires.ftc.teamcode.components.LatchSystem;
import org.firstinspires.ftc.teamcode.components.SpinnySystem;
import org.firstinspires.ftc.teamcode.components.Tensorflow;
import org.firstinspires.ftc.teamcode.components.Vuforia.CameraChoice;

import java.util.EnumMap;

public abstract class BaseOpMode extends OpMode {

    protected DriveSystem driveSystem;
    protected LatchSystem latchSystem;
    protected SpinnySystem spinnySystem;
    protected Tensorflow tensorflow;
    protected ArmSystem armSystem;
    private boolean stopRequested;

    public void init(){
        stopRequested = false;
//        this.msStuckDetectInit = 20000;
//        this.msStuckDetectInitLoop = 20000;
        EnumMap<DriveSystem.MotorNames, DcMotor> driveMap = new EnumMap<>(DriveSystem.MotorNames.class);
        for(DriveSystem.MotorNames name : DriveSystem.MotorNames.values()){
            driveMap.put(name,hardwareMap.get(DcMotor.class, name.toString()));
        }
        driveSystem = new DriveSystem(driveMap, hardwareMap.get(BNO055IMU.class, "imu"));

        latchSystem = new LatchSystem(hardwareMap.get(Servo.class, "latchLeft"), hardwareMap.get(Servo.class, "latchRight"));

        EnumMap<IntakeSystem.MotorNames, DcMotor> intakeMap = new EnumMap<>(IntakeSystem.MotorNames.class);
        for(IntakeSystem.MotorNames name : IntakeSystem.MotorNames.values()){
            intakeMap.put(name,hardwareMap.get(DcMotor.class, name.toString()));
        }
        intakeSystem = new IntakeSystem(intakeMap);

//        EnumMap<ArmSystem.ServoNames, Servo> servoEnumMap = new EnumMap<ArmSystem.ServoNames, Servo>(ArmSystem.ServoNames.class);
//        servoEnumMap.put(ArmSystem.ServoNames.GRIPPER, hardwareMap.get(Servo.class, "gripper"));
//        servoEnumMap.put(ArmSystem.ServoNames.ELBOW, hardwareMap.get(Servo.class, "elbow"));
//        servoEnumMap.put(ArmSystem.ServoNames.WRIST, hardwareMap.get(Servo.class, "wrist"));
//        servoEnumMap.put(ArmSystem.ServoNames.PIVOT, hardwareMap.get(Servo.class, "pivot"));
//        armSystem = new ArmSystem(
//                servoEnumMap,
//                hardwareMap.get(DcMotor.class, "slider_motor"),
//                hardwareMap.get(DigitalChannel.class, "slider_switch"), false);

    }

    protected void setCamera(CameraChoice cameraChoice) {
        tensorflow = new Tensorflow(hardwareMap, CameraChoice.WEBCAM1);
    }
}
