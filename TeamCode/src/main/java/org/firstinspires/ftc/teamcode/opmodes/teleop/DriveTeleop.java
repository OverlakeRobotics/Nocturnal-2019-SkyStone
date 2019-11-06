package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.components.ArmSystem;
import org.firstinspires.ftc.teamcode.opmodes.base.BaseOpMode;

import java.util.EnumMap;
import java.util.*;

@TeleOp(name = "Real Teleop", group="TeleOp")
public class DriveTeleop extends BaseOpMode {
    
    public void loop(){
        /*listoftogglebooleans.set(loopcount, gamepad2.a);
        if(!(listoftogglebooleans.get(loopcount)) && listoftogglebooleans.get(loopcount-1)){
            if(latched){
                latchSystem.unlatch();
                latched = false;
            }
            if(!latched){
                latchSystem.latch();
                latched = true;
            }
        }*/
        if(gamepad2.a && !aRecentlyHit){
            aRecentlyHit = true;
            latchSystem.toggle();
        }
        else if (!gamepad2.a)
        {
            aRecentlyHit = false;
        }

        float rx = (float) Math.pow(gamepad1.right_stick_x, 3);
        float lx = (float) Math.pow(gamepad1.left_stick_x, 3);
        float ly = (float) Math.pow(gamepad1.left_stick_y, 3);

        driveSystem.drive(rx, lx, -ly, gamepad1.x);
        spinnySystem.spin(gamepad1.left_bumper, gamepad1.right_bumper);

        // Arm code (THIS NEEDS TO BE CLEANED UP LATER)
        // Put every joystick value to the 3rd power for greater control over the robot
        // 1^3 = 1, so we don't even need to trim the values or anything
            if (gamepad2.dpad_left) {
                armSystem.movePresetPosition(ArmSystem.Position.POSITION_WEST);
            } else if (gamepad2.dpad_up) {
                armSystem.movePresetPosition(ArmSystem.Position.POSITION_HOME);
            } else if (gamepad2.dpad_down) {
                armSystem.movePresetPosition(ArmSystem.Position.POSITION_SOUTH);
            } else if (gamepad2.dpad_right) {
                armSystem.movePresetPosition(ArmSystem.Position.POSITION_EAST);
            }

            if (gamepad2.right_bumper && !m_right) {
                armSystem.queuedHeight++;
                armSystem.go();
                m_right = true;
            } else if (!gamepad2.right_bumper) {
                m_right = false;
            }

            if (gamepad2.left_bumper && !m_left) {
                armSystem.queuedHeight--;
                armSystem.go();
                m_left = true;
            } else if (!gamepad2.left_bumper) {
                m_left = false;
            }

            if (gamepad2.a && !m_gripper) { // This doesn't fit the spec on the google doc and should be changed later
                armSystem.toggleGripper();
                m_gripper = true;
            } else if (!gamepad2.a) {
                m_gripper = false;
            }
            armSystem.updateHeight();
        //loopcount++;
    }
}