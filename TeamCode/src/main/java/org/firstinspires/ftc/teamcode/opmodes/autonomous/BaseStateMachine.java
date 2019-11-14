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
        STATE_PARK_AT_LINE,
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
    @Override
    public void loop() {
        telemetry.addData("State", mCurrentState);
        telemetry.update();
        switch (mCurrentState) {
            case LOGGING:
                telemetry.addData("DistanceFront", distanceCenter.getDistance(DistanceUnit.MM));
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
                            newState(State.STATE_ALIGN_STONE);
                            break;
                        }
                    }
                } else {
                    skystoneOffset = 20;
                    newState(State.STATE_ALIGN_STONE);
                }
                break;

            case STATE_ALIGN_STONE:
                // Align to prepare intake
                if (driveSystem.driveToPosition(skystoneOffset, DriveSystem.Direction.FORWARD, 0.75)) {
                    newState(State.STATE_HORIZONTAL_ALIGN_STONE);
                }
                break;

            case STATE_HORIZONTAL_ALIGN_STONE:
                if (driveSystem.driveToPosition(800, centerDirection, 0.7)) {
                    newState(State.STATE_INTAKE_STONE);
                }
                break;

            case STATE_INTAKE_STONE:
                if (driveSystem.driveToPosition(150, DriveSystem.Direction.FORWARD, 0.2)) {
//                    spinnySystem.spin(false, false);
                    distanceToWall = (int) distanceOutside.getDistance(DistanceUnit.MM);
                    Log.d(TAG, "Distance to wall: " + distanceToWall);
                    newState(State.STATE_ALIGN_BRIDGE);
                }
//                else {
//                    spinnySystem.spin(true, false);
//                }
                break;

            case STATE_ALIGN_BRIDGE:
                if (driveSystem.driveToPosition(distanceToWall + 100, outsideDirection, 1.0)) {
                    newState(State.STATE_MOVE_PAST_LINE);
                }
                break;

            case STATE_MOVE_PAST_LINE:
                if (driveSystem.driveToPosition(900 - skystoneOffset, DriveSystem.Direction.FORWARD, 1.0)) {
                    newState(State.EJECT_STONE);
                }
                break;

            case EJECT_STONE:
                if (mStateTime.milliseconds() >= 1000) {
//                    spinnySystem.spin(false, false);
                    newState(State.STATE_PARK_AT_LINE);
                } else {
//                    spinnySystem.spin(false, true);
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

