package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.components.ArmSystem;
import org.firstinspires.ftc.teamcode.opmodes.base.BaseOpMode;

import java.util.EnumMap;

@TeleOp(name = "Real Teleop", group="TeleOp")
public class DriveTeleop extends BaseOpMode {
    
    public void loop(){
        float rx = (float) Math.pow(gamepad1.right_stick_x, 5);
        float lx = (float) Math.pow(gamepad1.left_stick_x, 3);
        float ly = (float) Math.pow(gamepad1.left_stick_y, 3);
        driveSystem.slowDrive(gamepad1.left_trigger > 0.3f);
        driveSystem.drive(rx, lx, -ly);
        if (gamepad1.right_bumper) {
            intakeSystem.suck();
        } else if (gamepad1.left_bumper) {
            intakeSystem.unsuck();
        }
        latchSystem.run(gamepad2.x, gamepad2.y);
        /*
        if  (armSystem.isHoming()) {
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
        } else if (gamepad2.right_bumper) {
            armSystem.moveUp(1);
        } else if (gamepad2.left_bumper) {
            armSystem.moveDown(1);
        } else if (gamepad2.a) {
            armSystem.moveGripper();
        }
        armSystem.updateHeight(0.75);

         */
    }
}