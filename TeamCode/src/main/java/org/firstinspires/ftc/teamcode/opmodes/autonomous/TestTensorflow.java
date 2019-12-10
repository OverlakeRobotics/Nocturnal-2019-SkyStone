package org.firstinspires.ftc.teamcode.opmodes.autonomous;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.teamcode.components.DriveSystem;
import org.firstinspires.ftc.teamcode.components.DriveSystemAutonomous;
import org.firstinspires.ftc.teamcode.components.Tensorflow;
import org.firstinspires.ftc.teamcode.components.Vuforia;
import org.firstinspires.ftc.teamcode.opmodes.base.BaseOpMode;
import org.firstinspires.ftc.teamcode.opmodes.base.TestOpMode;

import java.util.List;

@Autonomous(name = "TestTensorflow", group="Autonomous")
public class TestTensorflow extends BaseAutonomous {
    public enum State {
        STATE_STRAFE_RIGHT,
        STATE_COMPLETE
    }

    protected State mCurrentState;    // Current State Machine State.

    @Override
    public void init() {
        super.init(Team.BLUE);
        newState(State.STATE_STRAFE_RIGHT);
    }

    @Override
    public void loop() {
        switch (mCurrentState) {
            case STATE_STRAFE_RIGHT:
                if (driveSystem.driveToPosition(1500, DriveSystemAutonomous.Direction.RIGHT, 0.2, 0)) {
                    newState(State.STATE_COMPLETE);
                }
                break;
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

