package org.firstinspires.ftc.teamcode.opmodes.autonomous;

import org.firstinspires.ftc.teamcode.components.*;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.teamcode.components.DriveSystem;
import org.firstinspires.ftc.teamcode.components.IMUSystem;
import org.firstinspires.ftc.teamcode.components.LatchSystem;
import org.firstinspires.ftc.teamcode.components.Vuforia;
import org.firstinspires.ftc.teamcode.components.Vuforia.CameraChoice;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.Range;
import java.util.EnumMap;

@Autonomous(name = "PullFoundation", group="Autonomous")

public class PullFoundationAutonomous extends BaseStateMachine {
    //private static final double LATCH_LINEUP_THRESHOLD = 0.0;

    public final void sleep(long milliseconds) {
        try{
            Thread.sleep(milliseconds);
        }
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public enum State {
        STATE_INITIAL,
        STATE_LINE_UP,
        STATE_PULL_AWAY,
        STATE_FIND_TRAY_X,
        STATE_FIND_TRAY_Y,
    }

    protected State mCurrentState;

    public void init(){
        super.init();
        newState(State.STATE_INITIAL);
    }

    public void newState(State newState){
        mCurrentState = newState;
    }
    @Override
    public void loop(){
        switch(mCurrentState){
            case STATE_INITIAL:
                //has gotten stone
                driveSystem.turn(90, 0.8);
                newState(State.STATE_FIND_TRAY_X);
                break;
            case STATE_FIND_TRAY_X:
                VectorF position = vuforia.getRobotPosition();
                position.get(0); //x-value on axis
                position.get(1); //y-value on axis
                float distancetoDriveBackwardx = (float) 2685.99 - position.get(0);
                sleep(500);
                driveSystem.driveToPosition(Math.abs((int) distancetoDriveBackwardx), DriveSystem.Direction.BACKWARD, 1);
                if(!driveSystem.driveToPosition(Math.abs((int) distancetoDriveBackwardx), DriveSystem.Direction.BACKWARD, 1)){
                    newState(State.STATE_FIND_TRAY_Y);
                    break;
                }
            case STATE_FIND_TRAY_Y:
                position = vuforia.getRobotPosition();
                float distancetoDriveBackwardy = position.get(1) - (float) 2457.4;
                driveSystem.turn(90, 0.5);
                driveSystem.driveToPosition(Math.abs((int) distancetoDriveBackwardy), DriveSystem.Direction.BACKWARD, 1);
                if(!driveSystem.driveToPosition(Math.abs((int) distancetoDriveBackwardy), DriveSystem.Direction.BACKWARD, 1)){
                    newState(State.STATE_PULL_AWAY);
                    break;
                }
            case STATE_PULL_AWAY:
                latchSystem.latch();
                driveSystem.driveToPosition((int)(1200.2 - 101.6), DriveSystem.Direction.FORWARD, 1);
                if(driveSystem.driveToPosition((int)(1200.2 - 101.6), DriveSystem.Direction.FORWARD, 1)){
                    break;
                }
        }
    }
}
