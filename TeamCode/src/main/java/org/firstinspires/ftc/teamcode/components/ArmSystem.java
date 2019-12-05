package org.firstinspires.ftc.teamcode.components;
import android.util.Log;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.Gamepad;
import  com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.EnumMap;

/*
    This class controls everything related to the arm, including driver assist features.

    IMPORTANT: When working on this class (and arm stuff in general),
    keep the servo names consistent: (from closest to the block to farthest)
        - Gripper
        - Wrist
        - Elbow
        - Pivot
 */
public class ArmSystem {
    private Servo gripper;
    private Servo wrist;
    private Servo elbow;
    private Servo pivot;
    private DcMotor slider;
    private final double GRIPPER_OPEN = 0.7;
    private final double GRIPPER_CLOSE = 0.3;

    // This is in block positions, not ticks
    public int targetHeight;

    private enum Direction {
        UP, DOWN;
        private static Direction reverse(Direction direction){
            return direction == UP ? DOWN : UP;
        }

        private static DcMotorSimple.Direction motorDirection(Direction direction){
            return direction == UP ? DcMotorSimple.Direction.REVERSE : DcMotorSimple.Direction.FORWARD;
        }
    };

    private Direction direction;
    private boolean isHoming;
    // Don't change this unless in calibrate() or init(), is read in the calculateHeight method
    private int calibrationDistance;

    // This can actually be more, like 5000, but we're not going to stack that high
    // for the first comp and the servo wires aren't long enough yet
    private final int MAX_HEIGHT = calculateHeight(9);
    private final int INCREMENT_HEIGHT = 564; // how much the ticks increase when a block is added
    private final int START_HEIGHT = 366; // Height of the foundation

    public enum Position {
        // Double values ordered Pivot, elbow, wrist.
        POSITION_HOME(new double[] {0.98, 0.17, 0.79}),
        POSITION_WEST(new double[] {0.16, 0.22, 0.72}),
        POSITION_SOUTH(new double[] {0.16, 0.22, 0.37}),
        POSITION_EAST(new double[] {0.16, 0.22, 0.37}),
        POSITION_NORTH(new double[] {0.16, 0.58, 0.05}),
        POSITION_CAPSTONE(new double[] {0.78, 0.31, 0.75});

        private double[] posArr;

        Position(double[] positions) {
            posArr = positions;
        }

        private double[] getPos() {
            return this.posArr;
        }
    }

    public enum ServoNames {
        GRIPPER, WRIST, ELBOW, PIVOT
    }

    public static final String TAG = "ArmSystem"; // for debugging
    private boolean gripped;
    private boolean goUp;
    private boolean goDown;

    /*
     If the robot is at the bottom of the screen, and X is the block:

     XO
     XO  <--- Position west

     OO
     XX  <--- Position south 

     OX
     OX  <--- Position east

     XX
     OO  <--- Position north
     */
    public ArmSystem(EnumMap<ServoNames, Servo> servos, DcMotor slider) {
        this.gripper = servos.get(ServoNames.GRIPPER);
        this.wrist = servos.get(ServoNames.WRIST);
        this.elbow = servos.get(ServoNames.ELBOW);
        this.pivot = servos.get(ServoNames.PIVOT);
        this.slider = slider;
        this.calibrationDistance = slider.getCurrentPosition();
        this.direction = Direction.UP;
        this.slider.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.isHoming = false;
    }

    // Go to "west" position
    public void moveWest() {
        movePresetPosition(Position.POSITION_WEST);
    }

    // Go to "east" position
    public void moveEast() {
        movePresetPosition(Position.POSITION_EAST);
    }

    // Go to "south" position
    public void moveSouth() {
        movePresetPosition(Position.POSITION_SOUTH);
    }

    // Go to "north" position
    public void moveNorth() {
        movePresetPosition(Position.POSITION_NORTH);
    }

    // Go to capstone position
    public void moveCapstone() {
        movePresetPosition(Position.POSITION_CAPSTONE);
    }

    // Go to the home position
    public void moveHome() {
        goHome();
    }

    // These are public for debugging purposes
    public double getGripper() {
        return gripper.getPosition();
    }

    public double getWrist() {
        return wrist.getPosition();
    }

    public double getElbow() {
        return elbow.getPosition();
    }

    public double getPivot() {
        return 0;
    }

    // Moves the slider up to one block high, moves the gripper to the home position, and then moves
    // back down so we can fit under the bridge.
    private void goHome() {
        isHoming = true;
        if (direction == Direction.UP) {
            int diff = getSliderPos() - calculateHeight(0);
            setSliderHeight(1);
            if (Math.abs(getSliderPos() - calculateHeight(1)) < 50) {
                movePresetPosition(Position.POSITION_HOME);
                openGripper();
                direction = Direction.DOWN;
                isHoming = false;
            }
        }
        updateHeight(1);

    }

    private void openGripper() {
        gripper.setPosition(GRIPPER_OPEN);
    }

    private void closeGripper() {
        gripper.setPosition(GRIPPER_CLOSE);
    }

    public void toggleGripper() {
        if (Math.abs(gripper.getPosition() - GRIPPER_CLOSE)
                < Math.abs(gripper.getPosition() - GRIPPER_OPEN)) {
            // If we're in here, the gripper is closer to it's closed position
            openGripper();
        } else {
            closeGripper();
        }
    }

    private void placeStone() {
        openGripper();
        setSliderHeight(getSliderPos() + 1);
        movePresetPosition(Position.POSITION_HOME);
        moveHome();
    }

    private void movePresetPosition(Position pos) {
        double[] posArray = pos.getPos();
        pivot.setPosition(posArray[0]);
        elbow.setPosition(posArray[1]);
        wrist.setPosition(posArray[2]);
    }


    private void stop() {
        slider.setPower(0);
    }

    // Pos should be the # of blocks high it should be
    public void setSliderHeight(int pos) {
        /*
        int calculatedHeight = calculateHeight(pos);
        if (calculatedHeight < calibrationDistance || calculatedHeight > MAX_HEIGHT) {
            return;
        }

         */
        targetHeight = pos;
        slider.setTargetPosition(calculateHeight(targetHeight));
        slider.setDirection(Direction.motorDirection(Direction.UP));
        slider.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Log.d(TAG, "Set target height to" + calculateHeight(targetHeight));
    }

    // Little helper method for setSliderHeight
    private int calculateHeight(int pos) {
        return START_HEIGHT + calibrationDistance + (pos * INCREMENT_HEIGHT);
    }

    // Must be called every loop
    public void updateHeight(double speed) {
        slider.setPower(speed);
        slider.setTargetPosition(calculateHeight(targetHeight));
    }

    public int getSliderPos() { return slider.getCurrentPosition(); }

    public boolean isHoming() {
        return isHoming;
    }
}
