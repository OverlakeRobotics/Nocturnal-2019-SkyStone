package org.firstinspires.ftc.teamcode.opmodes.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DistanceSensor;

import org.firstinspires.ftc.teamcode.components.ColorSystem;
import org.firstinspires.ftc.teamcode.components.LidarSystem;

@Autonomous(name = "Sensor")
public class Sensor extends LinearOpMode {
    private ColorSensor sensor;
    private ColorSystem system;

    private DistanceSensor lidar;
    private DistanceSensor lidar2;
    private LidarSystem distance1;
    private LidarSystem distance2;

    public Sensor(){
        initialize();
        system = new ColorSystem(sensor);
    }

    public void initialize() {
        sensor = hardwareMap.colorSensor.get("color_sensor");
        distance1 = new LidarSystem(this, lidar);
        distance2 = new LidarSystem(this, lidar2);
    }

    public void runOpMode() {

        waitForStart();

        int alpha = sensor.alpha();
        int red = sensor.red();
        int green = sensor.green();
        int blue = sensor.blue();

        telemetry.addData("Alpha:", alpha);
        telemetry.addData("Red:", red);
        telemetry.addData("Green:", green);
        telemetry.addData("Blue:", blue);

        telemetry.addData("isRed:", system.isRed());
        telemetry.addData("isBlue:", system.isBlue());
        telemetry.addData("isYellow:", system.isYellow());
    }

}
