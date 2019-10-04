package org.firstinspires.ftc.teamcode.opmodes.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import com.qualcomm.robotcore.hardware.ColorSensor;

import org.firstinspires.ftc.teamcode.components.Color;
import org.firstinspires.ftc.teamcode.components.ColorSystem;


public class SensorOp extends LinearOpMode {
    private ColorSensor sensor;
    private ColorSystem system = new ColorSystem(sensor);

    public void initialize() {
        sensor = hardwareMap.colorSensor.get("color_sensor");
    }

    public void runOpMode() {
        int alpha = sensor.alpha();
        int red = sensor.red();
        int green = sensor.green();
        int blue = sensor.blue();

        //telemetry.addData();


    }

}
