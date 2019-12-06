package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.components.ArmSystem;
import org.firstinspires.ftc.teamcode.opmodes.base.BaseOpMode;

@TeleOp(name = "Real Teleop", group="TeleOp")
public class DriveTeleop extends BaseOpMode {

    private final double SLIDER_SPEED = 1;
    private boolean xRecentlyHit, m_gripper, m_down, m_up;

    public void loop(){

        telemetry.addData("Limit switch", armSystem.switchIsPressed());
        telemetry.addData("Current encoder position", armSystem.getSliderPos());
        telemetry.update();


        float rx = (float) Math.pow(gamepad1.right_stick_x, 5);
        float lx = (float) Math.pow(gamepad1.left_stick_x, 3);
        float ly = (float) Math.pow(gamepad1.left_stick_y, 3);
        driveSystem.slowDrive(gamepad1.left_trigger > 0.3f);
        driveSystem.drive(rx, lx, ly);


        if (gamepad1.right_bumper) {
            intakeSystem.suck();
        } else if (gamepad1.left_bumper) {
            intakeSystem.unsuck();
        } else {
            intakeSystem.stop();
        }


        if(gamepad1.x && !xRecentlyHit){
            xRecentlyHit = true;
            latchSystem.toggle();
        } else if (!gamepad1.x){
            xRecentlyHit = false;
        }
        if (armSystem.isHoming()) {
            armSystem.goHome();
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

        armSystem.raise(SLIDER_SPEED);

    }
}