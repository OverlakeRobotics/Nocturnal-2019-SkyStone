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

    private static final Color RED = new Color(255, 0,0);
    private static final Color BLUE = new Color(0,0,255);
    private static final Color YELLOW = new Color(255,255,0);

    private final Color RED_LINE = new Color(255, 0, 0);//TODO: ADJUST VALUES
    private final Color BLUE_LINE = new Color(0, 0, 255);

    private static final double SCALE_FACTOR = 3.9;

    ColorSensor colorSensor;
    private static DriveSystem driveSystem;

    public ColorSystem(OpMode opMode) {
        colorSensor = opMode.hardwareMap.get(LynxI2cColorRangeSensor.class, "color_sensor");
    }

    public int getRed() {
        return (int) (colorSensor.red() / SCALE_FACTOR);
    }

    public int getBlue() {
        return (int) (colorSensor.blue() / SCALE_FACTOR);
    }

    public int getGreen() {
        return (int) (colorSensor.green() / SCALE_FACTOR);
    }

    public int getAlpha() {
        return (int) (colorSensor.alpha() / SCALE_FACTOR);
    }

    public boolean isRed() {
        return getColor().equals(RED);
    }

    public boolean isBlue() {
        return getColor().equals(BLUE);
    }

    public boolean isYellow() {
        return getColor().equals(YELLOW);
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
        if (toCheck == OverLineSettings.OVER_ANY || toCheck == OverLineSettings.OVER_BLUE ||
                toCheck == OverLineSettings.OVER_RED) {
            return getColor().equals(BLUE_LINE) || getColor().equals(RED_LINE);
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
