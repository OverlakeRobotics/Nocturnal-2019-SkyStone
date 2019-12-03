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
public class TestTensorflow extends BaseOpMode {
    public enum State {
        STATE_SCAN_STONE,
    }

    protected State mCurrentState;    // Current State Machine State.

    @Override
    public void init() {
        super.init();
        newState(State.STATE_SCAN_STONE);
        super.setCamera(Vuforia.CameraChoice.WEBCAM1);
    }

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
                            double skystoneOffset = sign * (int) (300 * (Math.sin(Math.abs(degrees * Math.PI / 180))));
                            skystoneOffset -= 250;
                            telemetry.addData("Skystone offset", skystoneOffset);
                        }
                    }
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

