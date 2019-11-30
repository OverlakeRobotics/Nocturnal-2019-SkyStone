package org.firstinspires.ftc.teamcode.opmodes.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.teamcode.components.DriveSystem;
import org.firstinspires.ftc.teamcode.components.Tensorflow;
import org.firstinspires.ftc.teamcode.components.Vuforia;
import org.firstinspires.ftc.teamcode.opmodes.base.BaseOpMode;
import org.firstinspires.ftc.teamcode.opmodes.base.TestOpMode;

import java.util.List;

@Autonomous(name = "TestDrive", group="Autonomous")
public class TestDrive extends TestOpMode {
    public enum State {
        STATE_INITIAL,
        STATE_FINISHED,
        STATE_TURN_90,
        STATE_TURN_BACK
    }

    public static final String TAG = "Test Drive";
    private static final float mmPerInch = 25.4f;
    protected State mCurrentState;    // Current State Machine State.

    @Override
    public void init() {
        super.init();
        newState(State.STATE_INITIAL);
//        super.setCamera(Vuforia.CameraChoice.WEBCAM1);
//        skystone = vuforia.targetsSkyStone.get(0);

    }

    @Override
    public void loop() {
        switch (mCurrentState) {

            case STATE_INITIAL:
                // Initialize
                newState(State.STATE_TURN_90);
                break;

            case STATE_TURN_90:
                // Initialize
                if (driveSystem.turn(90, 1)) {
                    newState(State.STATE_TURN_BACK);
                }
                break;
            case STATE_TURN_BACK:
                // Initialize
                if (driveSystem.turnAbsolute(0, 1)) {
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

