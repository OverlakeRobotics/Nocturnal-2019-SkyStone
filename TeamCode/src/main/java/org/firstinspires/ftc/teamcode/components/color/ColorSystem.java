package org.firstinspires.ftc.teamcode.components.color;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.components.DriveSystem;

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
        //colorSensor = opMode.hardwareMap.get(ColorSensor.class, "color_sensor");
        colorSensor = opMode.hardwareMap.colorSensor.get("color");
    }

    public int getRed() {
        return colorSensor.red();
    }

    public int getBlue() {
        return colorSensor.blue();
    }

    public int getGreen() {
        return colorSensor.green();
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
        if (toCheck == OverLineSettings.OVER_BLUE){
            return getColor().equals(BLUE_LINE);
        }
        if (toCheck == OverLineSettings.OVER_RED){
            return getColor().equals(RED_LINE);
        }
        return false;
    }

}
