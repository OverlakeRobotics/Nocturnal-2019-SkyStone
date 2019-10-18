package org.firstinspires.ftc.teamcode.components.color;

import android.util.Log;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.lynx.LynxI2cColorRangeSensor;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;


import org.firstinspires.ftc.teamcode.components.DriveSystem;

import java.util.EnumMap;
import java.util.Iterator;

public class ColorSystem {

    private static final Color red = new Color(255, 0,0);
    private static final Color blue = new Color(0,0,255);
    private static final Color yellow = new Color(255,255,0);

    private static final Color redLine = new Color(255, 0, 0);//TODO: ADJUST VALUES
    private static final double redLineTolerance = 10d;
    private static final Color blueLine = new Color(0, 0, 255);
    private static final double blueLineTolerence = 10d;

    private static final int CORRECTION = 39;

    ColorSensor colorSensor;
    private static DriveSystem driveSystem;

    public ColorSystem(OpMode opMode) {
        HardwareMap hardwareMap = opMode.hardwareMap;
        for (Iterator<HardwareDevice> it = hardwareMap.iterator(); it.hasNext(); ) {
            HardwareDevice hardwareDevice = it.next();
            Log.d("HardwareMapDevice", hardwareDevice.getDeviceName());
        }
        colorSensor = hardwareMap.get(LynxI2cColorRangeSensor.class, "color_sensor");
        EnumMap<DriveSystem.MotorNames, DcMotor> driveMap = new EnumMap<>(DriveSystem.MotorNames.class);
        for(DriveSystem.MotorNames name : DriveSystem.MotorNames.values()){
            driveMap.put(name,hardwareMap.get(DcMotor.class, name.toString()));
        }
        driveSystem = new DriveSystem(driveMap, hardwareMap.get(BNO055IMU.class, "imu"));
    }

    public void debug() {
        Log.d("colorSensor", "Alpha: " + colorSensor.alpha());
        Log.d("colorSensor", "Red: " + colorSensor.red());
        Log.d("colorSensor", "Green: " + colorSensor.green());
        Log.d("colorSensor", "Blue: " + colorSensor.blue());
    }

    public int getRed() {
        return colorSensor.red() / CORRECTION;
    }

    public int getBlue() {
        return colorSensor.blue() / CORRECTION;
    }

    public int getGreen() {
        return colorSensor.green() / CORRECTION;
    }

    public int getAlpha() {
        return colorSensor.alpha() / CORRECTION;
    }

    public boolean isRed() {
        Color input = new Color(colorSensor.red(), colorSensor.blue(), colorSensor.green());
        return input.equals(red);
    }

    public boolean isBlue() {
        Color input = new Color(colorSensor.red(), colorSensor.blue(), colorSensor.green());
        return input.equals(blue);
    }

    public boolean isYellow() {
        Color input = new Color(colorSensor.red(), colorSensor.blue(), colorSensor.green());
        return input.equals(yellow);
    }

    public Color getColor() {
        return new Color(getRed(), getGreen(), getBlue());
    }

    public void bLed(boolean lightOn) {
        colorSensor.enableLed(lightOn);
    }

    public enum OverLineSettings {
        OVER_RED,
        OVER_BLUE,
        OVER_ANY
    }

    public boolean checkIfOverLine(OverLineSettings toCheck) {
        if(toCheck == OverLineSettings.OVER_ANY || toCheck == OverLineSettings.OVER_BLUE)
        {
            if(getColor().equals(blueLine, blueLineTolerence))
            {
                return true;
            }
        }
        if(toCheck == OverLineSettings.OVER_ANY || toCheck == OverLineSettings.OVER_RED)
        {
            if(getColor().equals(redLine, redLineTolerance))
            {
                return true;
            }
        }
        return false;
    }

    public enum LineFoundEnum {
        FOUND,
        FAILED,
        NOT_FOUND
    }
    ElapsedTime et = new ElapsedTime();
    boolean resetET = true;

    public LineFoundEnum driveToLine(OverLineSettings lineToFind, double maximumTime, float xDir, float yDir) {
        if(resetET) {
            et.reset();
            resetET = false;
        }

        if(maximumTime < et.seconds()) {
            resetET = true;
            return LineFoundEnum.FAILED;
        } else if(checkIfOverLine(lineToFind)) {
            resetET = true;
            return LineFoundEnum.FOUND;
        } else {
            driveSystem.drive(xDir, yDir, 0, 0);
            return LineFoundEnum.NOT_FOUND;
        }
    }
}
