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
    final double SLIDER_SPEED = 1;
    public void init() {
        EnumMap<ArmSystem.ServoNames, Servo> servoEnumMap = new EnumMap<ArmSystem.ServoNames, Servo>(ArmSystem.ServoNames.class);
        servoEnumMap.put(ArmSystem.ServoNames.GRIPPER, hardwareMap.get(Servo.class, "GRIPPER"));
        servoEnumMap.put(ArmSystem.ServoNames.ELBOW, hardwareMap.get(Servo.class, "ELBOW"));
        servoEnumMap.put(ArmSystem.ServoNames.WRIST, hardwareMap.get(Servo.class, "WRIST"));
        servoEnumMap.put(ArmSystem.ServoNames.PIVOT, hardwareMap.get(Servo.class, "PIVOT"));
        armSystem = new ArmSystem(servoEnumMap, hardwareMap.get(DcMotor.class, "SLIDER_MOTOR"));
    }

    // These boolean variables are to make sure the controls don't get spammed.
    // They need to be out here, even though it's kinda ugly, because ArmSystem doesn't get access
    // to the controller input.
    private boolean m_up, m_down, m_gripper;
    public void loop() {
        if (armSystem.isHoming()) {
            return;
        } else if (gamepad2.x) {
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
        }

        if (gamepad2.a && !m_gripper) {
            armSystem.toggleGripper();
            m_gripper = true;
        } else if (!gamepad2.a) {
            m_gripper = false;
        }

        if (gamepad2.right_bumper && !m_up) {
            armSystem.setSliderHeight(armSystem.targetHeight + 1);
            m_up = true;
        } else if (!gamepad2.right_bumper) {
            m_up = false;
        }

        if (gamepad2.left_bumper && !m_down) {
            armSystem.setSliderHeight(armSystem.targetHeight - 1);
            m_down = true;
        } else if (!gamepad2.left_bumper) {
            m_down = false;
        }
        //telemetry.addData("Target height: ", armSystem);

        armSystem.updateHeight(SLIDER_SPEED);
    }

    public void stop() {
        armSystem.stop();
    }
}
