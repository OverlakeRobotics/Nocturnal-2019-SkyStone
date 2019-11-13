package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.teamcode.components.ArmSystem;
import org.firstinspires.ftc.teamcode.components.IMUSystem;
import org.firstinspires.ftc.teamcode.opmodes.base.BaseOpMode;

import java.io.IOException;
import java.util.EnumMap;
import java.util.concurrent.ExecutionException;

@TeleOp(name = "strafingTests", group = "TeleOp")
public class strafingTests extends BaseOpMode {

    private static final float PRECISION_FACTOR = 2;//In degrees
    private boolean aRecentlyHit = false;
    private boolean rtRecentlyHit = false;
    private enum Modes {
        DEAD_RECKONING ("DEAD_RECKONING"),
        IMU ("IMU"),
        DISTANCE_SENSOR("DISTANCE_SENSOR");

        String name;

        Modes(String name)
        {
            this.name = name;
        }
    }
    private Modes mode = Modes.DEAD_RECKONING;
    private SensorPlacement sensorDirection = SensorPlacement.BACK;
    private IMUSystem imuSystem = new IMUSystem(hardwareMap.get(BNO055IMU.class, "imu"));
    @Override
    public void loop() {
        String pushedButton = "No Buttons Pressed";
        if(gamepad1.a && !aRecentlyHit)
        {
            aRecentlyHit = true;
            //Cycle Mode
            if(mode == Modes.DEAD_RECKONING)mode = Modes.IMU;
            if(mode == Modes.IMU)mode = Modes.DISTANCE_SENSOR;
        }
        else
        {
            aRecentlyHit = false;
        }
        if(mode == Modes.DEAD_RECKONING)
        {
            if(gamepad1.b)//right
            {
                pushedButton = "B Pushed";
                driveSystem.drive(0, 1, 0, false);
            }
            if(gamepad1.x)//left
            {f
                pushedButton = "X Pushed";
                driveSystem.drive(0, -1, 0, true);
            }
        }
        else if (mode == Modes.IMU)
        { double direction = imuSystem.getPitch();
            if(Math.abs(direction) > PRECISION_FACTOR)//In real code would need to take target direction into account.
            {
                driveSystem.turnAbsolute(0, 0.75);//In real "
            }
            if(gamepad1.b)//right
            {
                pushedButton = "B Pushed";
                driveSystem.drive(0, 1, 0, false);
            }
            if(gamepad1.x)//left
            {
                pushedButton = "X Pushed";
                driveSystem.drive(0, -1, 0, true);
            }
        }
        else
        {
            telemetry.addLine("EXCEPTION: NO MODE SELECTED.");
        }
        telemetry.addLine("Mode: " + mode.name);
        telemetry.addLine("Buttons Pushed: " + pushedButton);
        telemetry.update();
    }

    private enum SensorPlacement
    {
        FRONT,
        BACK
    }
    /**
     * Calculates the Angle that needs to be turned based on distance sensor readouts.
     * @param left The sensor on the left side of the robot
     * @param right The sensor on the right side of the robot
     * @param distBetweenRangeSensors the distance between the two range sensors (9?)
     * @param sensorPlacement If you want to detect off back or front sensors
     * @return Angle that needs to be turned (0-360)
     */
}
