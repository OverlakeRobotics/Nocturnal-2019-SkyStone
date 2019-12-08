package org.firstinspires.ftc.teamcode.opmodes.autonomous;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.components.ArmSystem;
import org.firstinspires.ftc.teamcode.components.DriveSystem;
import org.firstinspires.ftc.teamcode.components.DriveSystemAutonomous;
import org.firstinspires.ftc.teamcode.components.IntakeSystem;
import org.firstinspires.ftc.teamcode.components.LatchSystem;
import org.firstinspires.ftc.teamcode.components.LightSystem;
import org.firstinspires.ftc.teamcode.components.Tensorflow;
import org.firstinspires.ftc.teamcode.components.Vuforia;
import org.firstinspires.ftc.teamcode.opmodes.base.BaseOpMode;

import java.util.EnumMap;

public abstract class BaseAutonomous extends OpMode {
    DistanceSensor distanceCenter;
    DistanceSensor distanceOutside;
    DriveSystemAutonomous.Direction centerDirection;
    DriveSystemAutonomous.Direction outsideDirection;
    DriveSystemAutonomous driveSystem;
    LatchSystem latchSystem;
    ArmSystem armSystem;
    LightSystem lightSystem;
    IntakeSystem intakeSystem;
    Tensorflow tensorflow;
    ColorSensor colorSensor;
    Team currentTeam;

    public enum Team {
        RED, BLUE
    }

    public void init(BaseStateMachine.Team team) {
        this.msStuckDetectInit = 20000;
        this.msStuckDetectInitLoop = 20000;
        EnumMap<DriveSystemAutonomous.MotorNames, DcMotor> driveMap = new EnumMap<>(DriveSystemAutonomous.MotorNames.class);
        for(DriveSystemAutonomous.MotorNames name : DriveSystemAutonomous.MotorNames.values()){
            driveMap.put(name,hardwareMap.get(DcMotor.class, name.toString()));
        }
        driveSystem = new DriveSystemAutonomous(driveMap, hardwareMap.get(BNO055IMU.class, "imu"));

        EnumMap<LatchSystem.Latch, Servo> latchMap = new EnumMap<>(LatchSystem.Latch.class);
        for(LatchSystem.Latch name : LatchSystem.Latch.values()){
            latchMap.put(name,hardwareMap.get(Servo.class, name.toString()));
        }
        latchSystem = new LatchSystem(latchMap);

        lightSystem = new LightSystem(hardwareMap.get(DigitalChannel.class, "right_light"), hardwareMap.get(DigitalChannel.class, "left_light"));

        EnumMap<IntakeSystem.MotorNames, DcMotor> intakeMap = new EnumMap<>(IntakeSystem.MotorNames.class);
        for(IntakeSystem.MotorNames name : IntakeSystem.MotorNames.values()){
            intakeMap.put(name,hardwareMap.get(DcMotor.class, name.toString()));
        }
        intakeSystem = new IntakeSystem(intakeMap, hardwareMap.get(Servo.class, "BOTTOM_INTAKE"));

        EnumMap<ArmSystem.ServoNames, Servo> servoEnumMap = new EnumMap<ArmSystem.ServoNames, Servo>(ArmSystem.ServoNames.class);
        servoEnumMap.put(ArmSystem.ServoNames.GRIPPER, hardwareMap.get(Servo.class, "GRIPPER"));
        servoEnumMap.put(ArmSystem.ServoNames.ELBOW, hardwareMap.get(Servo.class, "ELBOW"));
        servoEnumMap.put(ArmSystem.ServoNames.WRIST, hardwareMap.get(Servo.class, "WRIST"));
        servoEnumMap.put(ArmSystem.ServoNames.PIVOT, hardwareMap.get(Servo.class, "PIVOT"));
        armSystem = new ArmSystem(
                servoEnumMap,
                hardwareMap.get(DcMotor.class, "SLIDER_MOTOR"),
                hardwareMap.get(DigitalChannel.class, "SLIDER_SWITCH"));

        lightSystem.on();
        if (team == BaseStateMachine.Team.RED) {
            distanceCenter = hardwareMap.get(DistanceSensor.class, "FRONTLEFTLIDAR");
            distanceOutside = hardwareMap.get(DistanceSensor.class, "FRONTRIGHTLIDAR");
            centerDirection = DriveSystem.Direction.LEFT;
            outsideDirection = DriveSystem.Direction.RIGHT;
        } else {
            distanceCenter = hardwareMap.get(DistanceSensor.class, "FRONTRIGHTLIDAR");
            distanceOutside = hardwareMap.get(DistanceSensor.class, "FRONTLEFTLIDAR");
            centerDirection = DriveSystem.Direction.RIGHT;
            outsideDirection = DriveSystem.Direction.LEFT;
        }
        tensorflow = new Tensorflow(hardwareMap, Vuforia.CameraChoice.WEBCAM1);
        colorSensor = hardwareMap.get(ColorSensor.class, "COLORSENSOR");
        currentTeam = team;
    }
}
