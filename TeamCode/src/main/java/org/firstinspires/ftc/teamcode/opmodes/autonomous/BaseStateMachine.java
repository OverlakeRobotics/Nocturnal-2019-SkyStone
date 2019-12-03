package org.firstinspires.ftc.teamcode.opmodes.autonomous;

import android.util.Log;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.teamcode.components.DriveSystem;
import org.firstinspires.ftc.teamcode.components.Vuforia.CameraChoice;
import org.firstinspires.ftc.teamcode.opmodes.base.BaseOpMode;

import java.util.List;

public abstract class BaseStateMachine extends BaseOpMode {
    public enum State {
        STATE_INITIAL,
        STATE_FIND_SKYSTONE,
        STATE_ALIGN_SKYSTONE,
        STATE_HORIZONTAL_ALIGN_SKYSTONE,
        STATE_INTAKE_SKYSTONE,
        STATE_ALIGN_STONE,
        STATE_HORIZONTAL_ALIGN_STONE,
        STATE_INTAKE_STONE,
        STATE_ALIGN_BRIDGE,
        STATE_MOVE_PAST_LINE,
        STATE_TURN_FOR_FOUNDATION,
        STATE_BACKUP_INTO_FOUNDATION,
        STATE_INITIAL_ALIGN_STONE,
        STATE_STRAFE_AWAY_FROM_FOUNDATION,
        STATE_MOVE_INTO_WALL,
        STATE_ALIGN_FOR_BRIDGE,
        STATE_FIND_STONE,
        STATE_APPROACH_STONE,
        STATE_COMPLETE,
        STATE_DEPOSIT_STONE,
        STATE_BACKUP_TO_LINE,
        STATE_TURN_FOR_BACKUP,
        STATE_BACKUP_FOR_SECOND_STONE,
        STATE_MOVE_PAST_COLOR_LINE,
        LOGGING
    }

    public enum Team {
        RED, BLUE
    }

    private final static String TAG = "BaseStateMachine";
    private ColorSensor colorSensor;
    protected State mCurrentState;    // Current State Machine State.
    protected ElapsedTime mStateTime = new ElapsedTime();  // Time into current state
    private DistanceSensor distanceCenter;
    private DistanceSensor distanceOutside;
    private DriveSystem.Direction centerDirection;
    private DriveSystem.Direction outsideDirection;
    private Team currentTeam;

    private DriveSystem.Direction direction;

    public void init(Team team) {
        super.init();
        currentTeam = team;
        this.msStuckDetectInit = 15000;
        this.msStuckDetectInitLoop = 15000;
        if (team == Team.RED) {
            distanceCenter = hardwareMap.get(DistanceSensor.class, "FRONTLEFTLIDAR");
            distanceOutside = hardwareMap.get(DistanceSensor.class, "FRONTRIGHTLIDAR");
            super.setCamera(CameraChoice.WEBCAM1);
            centerDirection = DriveSystem.Direction.LEFT;
            outsideDirection = DriveSystem.Direction.RIGHT;
        } else {
            distanceCenter = hardwareMap.get(DistanceSensor.class, "FRONTRIGHTLIDAR");
            distanceOutside = hardwareMap.get(DistanceSensor.class, "FRONTLEFTLIDAR");
            super.setCamera(CameraChoice.WEBCAM2);
            centerDirection = DriveSystem.Direction.RIGHT;
            outsideDirection = DriveSystem.Direction.LEFT;
        }
        colorSensor = hardwareMap.get(ColorSensor.class, "colorSensor");
        newState(State.STATE_INITIAL);
    }

    private int skystoneOffset;
    private int distanceToWall;
    private double alignStone;
    @Override
    public void loop() {
        telemetry.addData("State", mCurrentState);
        telemetry.update();
        switch (mCurrentState) {
            case LOGGING:
                // telemetry.addData("DistanceFront", distanceCenter.getDistance(DistanceUnit.MM));
                telemetry.addData("Color Blue", colorSensor.blue());
                telemetry.addData("Color Red", colorSensor.red());
                telemetry.addData("Color Green", colorSensor.green());
                telemetry.addData("Color Alpha", colorSensor.alpha());
                telemetry.addData("Color Hue", colorSensor.argb());
                telemetry.update();
                break;
            case STATE_INITIAL:
                // Initialize
                // Drive 0.5m (1 tile) to the left
                newState(State.STATE_FIND_SKYSTONE);
                break;

            case STATE_FIND_SKYSTONE:
                List<Recognition> recognitions = tensorflow.getInference();
                if (recognitions != null) {
                    for (Recognition recognition : recognitions) {
                        if (recognition.getLabel().equals("Skystone")) {
                            double degrees = recognition.estimateAngleToObject(AngleUnit.DEGREES);
                            int sign = (int) Math.signum(degrees);
                            skystoneOffset = sign * (int) (300 * (Math.sin(Math.abs(degrees * Math.PI / 180))));
                            skystoneOffset -= 250;
                            Log.d(TAG, "Skystone offset: " + skystoneOffset);
                            newState(State.STATE_ALIGN_SKYSTONE);
                            break;
                        }
                    }
                } else {
                    skystoneOffset = 20;
                    newState(State.STATE_ALIGN_SKYSTONE);
                }
                break;

            case STATE_ALIGN_SKYSTONE:
                // Align to prepare intake
                if (driveSystem.driveToPosition(skystoneOffset, DriveSystem.Direction.FORWARD, 0.75)) {
                    newState(State.STATE_HORIZONTAL_ALIGN_SKYSTONE);
                }
                break;

            case STATE_HORIZONTAL_ALIGN_SKYSTONE:
                if (driveSystem.driveToPosition(850, centerDirection, 0.7)) {
                    newState(State.STATE_INTAKE_SKYSTONE);
                }
                break;

            case STATE_INTAKE_SKYSTONE:
                if (driveSystem.driveToPosition(150, DriveSystem.Direction.FORWARD, 0.2)) {
//                    intakeSystem.stop();
                    distanceToWall = (int) distanceOutside.getDistance(DistanceUnit.MM);
                    Log.d(TAG, "Distance to wall: " + distanceToWall);
                    newState(State.STATE_ALIGN_BRIDGE);
                }
                else {
//                    intakeSystem.suck();
                }
                break;

            case STATE_ALIGN_BRIDGE:
                if (driveSystem.driveToPosition(distanceToWall + 100, outsideDirection, 1.0)) {
                    newState(State.STATE_MOVE_PAST_LINE);
                }
                break;

            case STATE_MOVE_PAST_LINE:
                if (driveSystem.driveToPosition(1600 - skystoneOffset, DriveSystem.Direction.FORWARD, 1.0)) {
                    newState(State.STATE_TURN_FOR_FOUNDATION);
                }
                break;

            case STATE_TURN_FOR_FOUNDATION:
                int sign = currentTeam == Team.RED ? 1 : -1;
                if (driveSystem.turnAbsolute(90 * sign, 1.0)) {
                    newState(State.STATE_BACKUP_INTO_FOUNDATION);
                }
                break;

            case STATE_BACKUP_INTO_FOUNDATION:
                if (driveSystem.driveToPosition(150, DriveSystem.Direction.BACKWARD, 1.0)) {
                    newState(State.STATE_MOVE_INTO_WALL);
                }
                break;

            case STATE_MOVE_INTO_WALL:
                if (driveSystem.driveToPosition(400, DriveSystem.Direction.FORWARD, 1.0)) {
                    newState(State.STATE_STRAFE_AWAY_FROM_FOUNDATION);
                }
                break;

            case STATE_STRAFE_AWAY_FROM_FOUNDATION:
                if (driveSystem.driveToPosition(500, outsideDirection, 1.0)) {
                    newState(State.STATE_TURN_FOR_BACKUP);
                }
                break;

            case STATE_TURN_FOR_BACKUP:
                if (driveSystem.turnAbsolute(0, 1.0)) {
                    newState(State.STATE_BACKUP_FOR_SECOND_STONE);
                }
                break;

            case STATE_BACKUP_FOR_SECOND_STONE:
                if (driveSystem.driveToPosition(1000 - skystoneOffset, DriveSystem.Direction.BACKWARD, 1.0)) {
                    newState(State.STATE_FIND_STONE);
                }
                break;

            case STATE_FIND_STONE:
                recognitions = tensorflow.getInference();
                if (recognitions != null) {
                    for (Recognition recognition : recognitions) {
                        if (recognition.getLabel().equals("Stone") || recognition.getLabel().equals("Skystone")) {
                            double degrees = recognition.estimateAngleToObject(AngleUnit.DEGREES);
                            sign = (int) Math.signum(degrees);
                            alignStone = sign * (int) (300 * (Math.sin(Math.abs(degrees * Math.PI / 180))));
                            newState(State.STATE_INITIAL_ALIGN_STONE);
                            break;
                        }
                    }
                }
                break;

            case STATE_INITIAL_ALIGN_STONE:
                if (driveSystem.driveToPosition((int) alignStone - 50, DriveSystem.Direction.FORWARD, 0.75)) {
                    newState(State.STATE_APPROACH_STONE);
                }
                break;

            case STATE_APPROACH_STONE:
                if (distanceCenter.getDistance(DistanceUnit.MM) < 350) {
                    driveSystem.stopAndReset();
                    alignStone = distanceCenter.getDistance(DistanceUnit.MM);
                    newState(State.STATE_ALIGN_STONE);
                } else {
                    driveSystem.driveToPosition(2000, centerDirection, 0.7);
                }
                break;

            case STATE_ALIGN_STONE:
                if (driveSystem.driveToPosition(250, DriveSystem.Direction.BACKWARD, 1.0)) {
                    newState(State.STATE_HORIZONTAL_ALIGN_STONE);
                }
                break;
            case STATE_HORIZONTAL_ALIGN_STONE:
                if (driveSystem.driveToPosition((int) alignStone + 125, centerDirection, 1.0)) {
                    newState(State.STATE_INTAKE_STONE);
                }
                break;

            case STATE_INTAKE_STONE:
                if (driveSystem.driveToPosition(200, DriveSystem.Direction.FORWARD, 1.0)) {
                    newState(State.STATE_ALIGN_FOR_BRIDGE);
                }
                break;

            case STATE_ALIGN_FOR_BRIDGE:
                if (driveSystem.driveToPosition((int) alignStone + 250, outsideDirection, 1.0)) {
                    newState(State.STATE_MOVE_PAST_COLOR_LINE);
                }
                break;

            case STATE_MOVE_PAST_COLOR_LINE:
                if (currentTeam == Team.RED) {
                    if (colorSensor.red() > colorSensor.blue() * 1.5) {
                        driveSystem.setMotorPower(0.0);
                        newState(State.STATE_DEPOSIT_STONE);
                    }
                } else {
                    if (colorSensor.blue() > colorSensor.red() * 1.5) {
                        driveSystem.setMotorPower(0.0);
                        newState(State.STATE_DEPOSIT_STONE);
                    }
                }
                driveSystem.drive(0, 0, -1.0f);
                break;


            case STATE_DEPOSIT_STONE:
                if (mStateTime.milliseconds() > 1250) {
//                    intakeSystem.stop();
                    newState(State.STATE_BACKUP_TO_LINE);
                } else {
//                    intakeSystem.unsuck();
                }
                break;

            case STATE_BACKUP_TO_LINE:
                if (driveSystem.driveToPosition(150, DriveSystem.Direction.BACKWARD, 1.0)) {
                    newState(State.STATE_COMPLETE);
                }
                break;

            case STATE_COMPLETE:

                break;
        }
    }

    public void newState(State newState) {
        // Restarts the state clock as well as the state
        mStateTime.reset();
        mCurrentState = newState;
    }

}