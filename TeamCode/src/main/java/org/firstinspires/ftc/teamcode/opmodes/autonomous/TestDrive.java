package org.firstinspires.ftc.teamcode.opmodes.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DistanceSensor;

import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.teamcode.components.DriveSystem;
import org.firstinspires.ftc.teamcode.components.Tensorflow;
import org.firstinspires.ftc.teamcode.components.Vuforia;
import org.firstinspires.ftc.teamcode.opmodes.base.BaseOpMode;
import org.firstinspires.ftc.teamcode.opmodes.base.TestOpMode;

import java.util.List;

@Autonomous(name = "TestStonePickup", group="Autonomous")
public class TestDrive extends TestOpMode {

    public static final String TAG = "Test Drive";
    private static final float mmPerInch = 25.4f;
    protected State mCurrentState;    // Current State Machine State.
    private Tensorflow tensorflow;
    private DistanceSensor distanceCenter;
    private DistanceSensor distanceOutside;
    private DriveSystem.Direction centerDirection;
    private DriveSystem.Direction outsideDirection;

    @Override
    public void init() {
        super.init();
        newState(State.STATE_FIND_STONE);
        distanceCenter = hardwareMap.get(DistanceSensor.class, "FRONTLEFTLIDAR");
        distanceOutside = hardwareMap.get(DistanceSensor.class, "FRONTRIGHTLIDAR");
        centerDirection = DriveSystem.Direction.LEFT;
        outsideDirection = DriveSystem.Direction.RIGHT;
        tensorflow = new Tensorflow(hardwareMap, Vuforia.CameraChoice.WEBCAM1);
    }

    public enum State {
        STATE_INITIAL,
        STATE_FIND_STONE,
        STATE_INITIAL_ALIGN_STONE,
        STATE_APPROACH_STONE,
        STATE_ALIGN_STONE,
        STATE_HORIZONTAL_ALIGN_STONE,
        STATE_INTAKE_STONE,
        STATE_COMPLETE,
    }

    List<Recognition> recognitions;
    private double alignStone;
    @Override
    public void loop() {
        switch (mCurrentState) {
            case STATE_FIND_STONE:
                recognitions = tensorflow.getInference();
                if (recognitions != null) {
                    for (Recognition recognition : recognitions) {
                        if (recognition.getLabel().equals("Stone") || recognition.getLabel().equals("Skystone")) {
                            double degrees = recognition.estimateAngleToObject(AngleUnit.DEGREES);
                            int sign = (int) Math.signum(degrees);
                            alignStone = sign * (int) (300 * (Math.sin(Math.abs(degrees * Math.PI / 180))));
                            newState(TestDrive.State.STATE_INITIAL_ALIGN_STONE);
                            break;
                        }
                    }
                }
                break;

            case STATE_INITIAL_ALIGN_STONE:
                if (driveSystem.driveToPosition((int) alignStone - 20, DriveSystem.Direction.FORWARD, 0.75)) {
                    newState(TestDrive.State.STATE_APPROACH_STONE);
                }
                break;

            case STATE_APPROACH_STONE:
                if (distanceCenter.getDistance(DistanceUnit.MM) < 350) {
                    driveSystem.stopAndReset();
                    alignStone = distanceCenter.getDistance(DistanceUnit.MM);
                    newState(TestDrive.State.STATE_ALIGN_STONE);
                } else {
                    driveSystem.driveToPosition(2000, centerDirection, 0.7);
                }
                break;

            case STATE_ALIGN_STONE:
                if (driveSystem.driveToPosition(250, DriveSystem.Direction.BACKWARD, 1.0)) {
                    newState(TestDrive.State.STATE_HORIZONTAL_ALIGN_STONE);
                }
                break;
            case STATE_HORIZONTAL_ALIGN_STONE:
                if (driveSystem.driveToPosition((int) alignStone + 120, centerDirection, 1.0)) {
                    newState(TestDrive.State.STATE_INTAKE_STONE);
                }
                break;

            case STATE_INTAKE_STONE:
                if (driveSystem.driveToPosition(225, DriveSystem.Direction.FORWARD, 1.0)) {
                    newState(TestDrive.State.STATE_COMPLETE);
                }
                break;

            case STATE_COMPLETE:

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

