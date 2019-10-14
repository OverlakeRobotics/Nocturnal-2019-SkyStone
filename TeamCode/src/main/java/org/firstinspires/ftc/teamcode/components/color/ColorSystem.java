package org.firstinspires.ftc.teamcode.components.color;

import android.util.Log;

import com.qualcomm.hardware.lynx.LynxI2cColorRangeSensor;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.HardwareMap;

import java.util.Iterator;

public class ColorSystem {

    private static final Color red = new Color(255, 0,0);
    private static final Color blue = new Color(0,0,255);
    private static final Color yellow = new Color(255,255,0);

    ColorSensor colorSensor;

    public ColorSystem(OpMode opMode) {
        HardwareMap hardwareMap = opMode.hardwareMap;
        for (Iterator<HardwareDevice> it = hardwareMap.iterator(); it.hasNext(); ) {
            HardwareDevice hardwareDevice = it.next();
            Log.d("HardwareMapDevice", hardwareDevice.getDeviceName());
        }
        colorSensor = hardwareMap.get(LynxI2cColorRangeSensor.class, "color_sensor");
    }

    public void debug() {
        Log.d("colorSensor", "Alpha: " + colorSensor.alpha());
        Log.d("colorSensor", "Red: " + colorSensor.red());
        Log.d("colorSensor", "Green: " + colorSensor.green());
        Log.d("colorSensor", "Blue: " + colorSensor.blue());
    }

    public int getRed() {
        return colorSensor.red() / 39;
    }

    public int getBlue() {
        return colorSensor.blue() / 39;
    }

    public int getGreen() {
        return colorSensor.green() / 39;
    }

    public int getAlpha() {
        return colorSensor.alpha() / 39;
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

    public void bLed(boolean lightOn) {
        colorSensor.enableLed(lightOn);
    }

}
