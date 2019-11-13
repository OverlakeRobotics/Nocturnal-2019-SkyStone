package org.firstinspires.ftc.teamcode.opmodes.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.teamcode.components.DriveSystem;
import org.firstinspires.ftc.teamcode.components.Tensorflow;
import org.firstinspires.ftc.teamcode.components.Vuforia;
import org.firstinspires.ftc.teamcode.opmodes.base.BaseOpMode;

import java.util.List;

@Autonomous(name = "TestDrive", group="Autonomous")
public class TestDrive extends BaseOpMode {
    public enum State {
        STATE_INITIAL,
        STATE_ROTATE_RIGHT,
        STATE_ROTATE_LEFT,
        STATE_RIGHT,
        STATE_LEFT,
        STATE_FORWARD,
        STATE_BACKWARD,
        STATE_FINISHED,
        STATE_TURN_90,
        STATE_TURN_BACK,
        STATE_TENSORFLOW_SCAN,
    }

    public static final String TAG = "Test Drive";
    private static final float mmPerInch = 25.4f;
    protected State mCurrentState;    // Current State Machine State.
    private Tensorflow tensorflow;

    @Override
    public void init() {
        super.init();
        tensorflow = new Tensorflow(hardwareMap, Vuforia.CameraChoice.WEBCAM1);
        newState(State.STATE_TENSORFLOW_SCAN);
//        super.setCamera(Vuforia.CameraChoice.WEBCAM1);
//        skystone = vuforia.targetsSkyStone.get(0);

    }

    @Override
    public void loop() {
        switch (mCurrentState) {
            case STATE_TENSORFLOW_SCAN:
                List<Recognition> recognitions = tensorflow.getInference();
                if (recognitions != null) {
                    telemetry.addData("Detections", recognitions.toString());
                }
                break;

            case STATE_INITIAL:
                // Initialize
                newState(State.STATE_TURN_90);
                break;

            case STATE_TURN_90:
                // Initialize
                if (driveSystem.turn(90, 0.5)) {
                    newState(State.STATE_TURN_BACK);
                }
                break;
            case STATE_TURN_BACK:
                // Initialize
                if (driveSystem.turnAbsolute(0, 0.5)) {
                    newState(State.STATE_FINISHED);
                }
                break;
            case STATE_FORWARD:
                if (driveSystem.driveToPosition(100, DriveSystem.Direction.FORWARD, .5)) {
                    newState(State.STATE_BACKWARD);
                }
                break;
            case STATE_BACKWARD:
                if (driveSystem.driveToPosition(100, DriveSystem.Direction.BACKWARD, .5)) {
                    newState(State.STATE_RIGHT);
                }
                break;
            case STATE_RIGHT:
                if (driveSystem.driveToPosition(100, DriveSystem.Direction.RIGHT, .5)) {
                    newState(State.STATE_LEFT);
                }
                break;
            case STATE_LEFT:
                if (driveSystem.driveToPosition(100, DriveSystem.Direction.LEFT, .5)) {
                    newState(State.STATE_FINISHED);
                }
                break;
            case STATE_FINISHED:

        }
    }

    // Stop
    @Override
    public void stop() {
    }

    public void newState(State newState) {
        mCurrentState = newState;
    }

}

