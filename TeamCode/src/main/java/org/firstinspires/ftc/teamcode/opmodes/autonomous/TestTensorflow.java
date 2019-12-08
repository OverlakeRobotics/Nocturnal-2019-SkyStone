package org.firstinspires.ftc.teamcode.opmodes.autonomous;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.teamcode.components.DriveSystem;
import org.firstinspires.ftc.teamcode.components.Tensorflow;
import org.firstinspires.ftc.teamcode.components.Vuforia;
import org.firstinspires.ftc.teamcode.opmodes.base.BaseOpMode;
import org.firstinspires.ftc.teamcode.opmodes.base.TestOpMode;

import java.util.List;

@Autonomous(name = "TestTensorflow", group="Autonomous")
public class TestTensorflow extends BaseAutonomous {
    public enum State {
        STATE_SCAN_STONE,
    }

    protected State mCurrentState;    // Current State Machine State.

    @Override
    public void init() {
        super.init(Team.BLUE);
        newState(State.STATE_SCAN_STONE);
    }

    private int skystoneOffset;
    private static final int DEAD_RECKON_SKYSTONE = -110;
    @Override
    public void loop() {
        switch (mCurrentState) {
            case STATE_SCAN_STONE:
                List<Recognition> recognitions = tensorflow.getInference();
                if (recognitions != null) {
                    for (Recognition recognition : recognitions) {
                        if (recognition.getLabel().equals("Skystone")) {
                            double degrees = recognition.estimateAngleToObject(AngleUnit.DEGREES);
                            int sign = (int) Math.signum(degrees);
                            int currOffset = sign * (int) (300 * (Math.sin(Math.abs(degrees * Math.PI / 180))));
                            currOffset -= 350;
                            // The skystone detected is one of the first three which means that
                            // the second skystone must be farthest from the audience
                            if (currOffset > -370) {
                                skystoneOffset = currOffset;
                            }
                        }
                    }
                    if (skystoneOffset == 0) {
                        skystoneOffset = DEAD_RECKON_SKYSTONE;
                    }
                    if (currentTeam == Team.BLUE) {
                        skystoneOffset *= 1.15;
                    }
                    Log.d("BaseStateMachine", "Skystone offset: " + skystoneOffset);
                }
                telemetry.update();
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

