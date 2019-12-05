package org.firstinspires.ftc.teamcode.tests;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.components.ArmSystem;

import java.util.EnumMap;

@TeleOp(name = "Just The Arm System", group="TeleOp")
public class JustTheArmSystem extends OpMode {
    public ArmSystem armSystem;
    public void init() {
        EnumMap<ArmSystem.ServoNames, Servo> servoEnumMap = new EnumMap<ArmSystem.ServoNames, Servo>(ArmSystem.ServoNames.class);
        servoEnumMap.put(ArmSystem.ServoNames.GRIPPER, hardwareMap.get(Servo.class, "GRIPPER"));
        servoEnumMap.put(ArmSystem.ServoNames.ELBOW, hardwareMap.get(Servo.class, "ELBOW"));
        servoEnumMap.put(ArmSystem.ServoNames.WRIST, hardwareMap.get(Servo.class, "WRIST"));
        servoEnumMap.put(ArmSystem.ServoNames.PIVOT, hardwareMap.get(Servo.class, "PIVOT"));
        armSystem = new ArmSystem(
                servoEnumMap,
                hardwareMap.get(DcMotor.class, "SLIDER_MOTOR"),
                hardwareMap.get(DigitalChannel.class, "SLIDER_SWITCH"));
    }

    public void loop() {
        /*
    public String run(boolean home, boolean capstone, boolean west, boolean east, boolean north, boolean south,
                      boolean up, boolean down, boolean gripperButton, boolean assist,
                      double sliderSpeed, double armSpeed, boolean fastMode) {
         */
        if (gamepad2.x) {
            armSystem.moveHome();
            return;
        } else if (gamepad2.y) {
            armSystem.moveCapstone();
        } else if (gamepad2.dpad_left) {
            armSystem.moveWest();
        } else if (gamepad2.dpad_right) {
            armSystem.moveEast();
        } else if (gamepad2.dpad_up) {
            armSystem.moveNorth();
        } else if (gamepad2.dpad_down) {
            armSystem.moveSouth();
        } else if (gamepad2.right_bumper) {
            armSystem.moveUp(1);
        } else if (gamepad2.left_bumper) {
            armSystem.moveDown(1);
        } else if (gamepad2.a) {
            armSystem.moveGripper();
        }
    }
}
