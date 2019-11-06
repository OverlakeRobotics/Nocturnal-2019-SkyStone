package org.firstinspires.ftc.teamcode.opmodes.autonomous;

import android.util.Log;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.teamcode.components.DriveSystem;
import org.firstinspires.ftc.teamcode.components.Vuforia.CameraChoice;
import org.firstinspires.ftc.teamcode.opmodes.base.BaseOpMode;

public abstract class BaseStateMachine extends BaseOpMode {
    public enum State {
        STATE_INITIAL,
        STATE_FIND_SKYSTONE,
        STATE_GRAB_STONE,
        STATE_PARK_AT_LINE,
        GRAB_STONE_DEAD_RECKONING,
        EJECT_STONE,
        STATE_ALIGN_STONE,
        STATE_HORIZONTAL_ALIGN_STONE,
        STATE_INTAKE_STONE,
        STATE_ALIGN_BRIDGE,
        STATE_MOVE_PAST_LINE,
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
    VuforiaTrackable skystone;

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
        lightSystem.off();
        this.msStuckDetectLoop = 30000;
        newState(State.STATE_INITIAL);
        skystone = vuforia.targetsSkyStone.get(0);
    }

    private VectorF translation;
    private int distanceToWall;
    @Override
    public void loop() {
        switch (mCurrentState) {
            case LOGGING:
                telemetry.addData("DistanceFront", distanceCenter.getDistance(DistanceUnit.INCH));
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
                if (driveSystem.driveToPosition(600, centerDirection, 1.0) && !isStopRequested()) {
                    newState(State.STATE_FIND_SKYSTONE);
                }
                break;

            case STATE_FIND_SKYSTONE:
                // If it has seen the stone grab the stone
                if (vuforia.isTargetVisible(skystone)) {
                    translation = vuforia.getRobotPosition();
                    newState(State.STATE_GRAB_STONE);
                    break;
                }
                // If it moves 700 millimeters and it hasn't found the stone just use dead reckoning
                if (driveSystem.driveToPosition(700, DriveSystem.Direction.BACKWARD, 0.2)) {
                    newState(State.GRAB_STONE_DEAD_RECKONING);
                    break;
                }
                break;

            case STATE_ALIGN_STONE:
                // Align to prepare intake
                if (driveSystem.driveToPosition((int) translation.get(0), DriveSystem.Direction.BACKWARD, 1.0)) {
                    newState(State.STATE_HORIZONTAL_ALIGN_STONE);
                }
                break;

            case STATE_HORIZONTAL_ALIGN_STONE:
                if (driveSystem.driveToPosition(700, centerDirection, 0.7)) {
                    newState(State.STATE_INTAKE_STONE);
                }
                break;

            case STATE_INTAKE_STONE:
                if (driveSystem.driveToPosition(500, DriveSystem.Direction.FORWARD, 0.2)) {
                    spinnySystem.spin(false, false);
                    distanceToWall = (int) distanceOutside.getDistance(DistanceUnit.MM);
                    newState(State.STATE_ALIGN_BRIDGE);
                } else {
                    spinnySystem.spin(true, false);
                }
                break;

            case STATE_ALIGN_BRIDGE:
                if (driveSystem.driveToPosition(distanceToWall, outsideDirection, 1.0)) {
                    newState(State.STATE_PARK_AT_LINE);
                }
                break;

            case STATE_MOVE_PAST_LINE:
                if (driveSystem.driveToPosition(2000, DriveSystem.Direction.FORWARD, 1.0)) {
                    mStateTime.reset();
                    newState(State.EJECT_STONE);
                }
                break;

            case EJECT_STONE:
                if (mStateTime.milliseconds() >= 1000) {
                    intakeSystem.stop();
                    newState(State.STATE_PARK_AT_LINE);
                } else {
                    intakeSystem.stop();
                }
                break;

            case STATE_PARK_AT_LINE:
                // Find the line
                if (currentTeam == Team.BLUE) {
                    if (colorSensor.blue() > colorSensor.red() * 1.5) {
                        driveSystem.setMotorPower(0.0);
                        newState(State.LOGGING);
                        break;
                    }
                } else if (currentTeam == Team.RED) {
                    if (colorSensor.red() > colorSensor.blue() * 1.5) {
                        driveSystem.setMotorPower(0.0);
                        newState(State.LOGGING);
                        break;
                    }
                }
                driveSystem.drive(0.0f, 0.0f, -0.2f);
                break;
        }
    }

    public void newState(State newState) {
        // Restarts the state clock as well as the state
        mStateTime.reset();
        mCurrentState = newState;
    }

}

