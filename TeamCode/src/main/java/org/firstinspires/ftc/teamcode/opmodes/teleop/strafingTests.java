package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.rev.Rev2mDistanceSensor;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
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


    private Rev2mDistanceSensor front = hardwareMap.get(Rev2mDistanceSensor.class, "sensorDistFront");
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
            if(mode == Modes.DISTANCE_SENSOR)mode = Modes.DEAD_RECKONING;
            pushedButton = "A Pushed";
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
            {
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
        else if (mode == Modes.DISTANCE_SENSOR)
        {
            if(gamepad1.right_bumper && !rtRecentlyHit)
            {
                rtRecentlyHit = true;
                if(sensorDirection==SensorPlacement.BACK)sensorDirection = SensorPlacement.FRONT;
                if(sensorDirection==SensorPlacement.FRONT)sensorDirection = SensorPlacement.BACK;
                pushedButton = "RT Pushed";
            }
            else
            {
                rtRecentlyHit = false;
            }
            if(sensorDirection == SensorPlacement.BACK)
            {
                driveSystem.turn(calculateAngleWithDist(hardwareMap.get(Rev2mDistanceSensor.class, "rangeRearRight"),
                        hardwareMap.get(Rev2mDistanceSensor.class, "rangeRearLeft"), 9, SensorPlacement.BACK), 0.75);
            }
            if(sensorDirection == SensorPlacement.FRONT)
            {
                driveSystem.turn(calculateAngleWithDist(hardwareMap.get(Rev2mDistanceSensor.class, "frontRearRight"),
                        hardwareMap.get(Rev2mDistanceSensor.class, "frontRearLeft"), 9, SensorPlacement.FRONT), 0.75);
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
            telemetry.addLine("Sensor Direction: " + sensorDirection.toString());
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
    private double calculateAngleWithDist(Rev2mDistanceSensor left, Rev2mDistanceSensor right, double distBetweenRangeSensors, SensorPlacement sensorPlacement)
    {
        double leftInch = left.getDistance(DistanceUnit.INCH);
        double rightInch = right.getDistance(DistanceUnit.INCH);
        try{
            if(leftInch > rightInch && sensorPlacement == sensorPlacement.BACK)//Img 1
            {
                return 360 - Math.atan((leftInch - rightInch)/distBetweenRangeSensors);
            }
            else if (rightInch > leftInch && sensorPlacement == sensorPlacement.BACK)//Img 2
            {
                return Math.atan((rightInch-leftInch)/distBetweenRangeSensors);
            }
            else if (leftInch > rightInch && sensorPlacement == SensorPlacement.FRONT)//Img 3
            {
                return Math.atan((leftInch - rightInch)/distBetweenRangeSensors);
            }
            else if (rightInch > leftInch && sensorPlacement == sensorPlacement.FRONT)//Img 4
            {
                return 360 - Math.atan((rightInch-leftInch)/distBetweenRangeSensors);
            }
            else//If right and left are =
            {
                return 0;
            }
        }
        catch (Exception e)
        {
            telemetry.addLine("ERROR: Wall out of range.");
            return 0;
        }
    }
}
