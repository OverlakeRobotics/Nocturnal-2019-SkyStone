package org.firstinspires.ftc.teamcode.components.color;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;

public class ColorSystem {

    private static final Color RED = new Color(255, 0,0);
    private static final Color BLUE = new Color(0,0,255);
    private static final Color YELLOW = new Color(255,255,0);

    private final Color RED_LINE = new Color(255, 0, 0);//TODO: ADJUST VALUES
    private final Color BLUE_LINE = new Color(0, 0, 255);

    ColorSensor colorSensor;

    public ColorSystem(OpMode opMode) {
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
        OVER_ANY (0, 0, 0, 0, 0, 0),//NEVER USE COLOR VALUES
        OVER_RED (3, 6, 0, 2, 0, 2),
        OVER_BLUE (0, 2, 0, 2, 3,6);

        public int r_min;
        public int r_max;
        public int g_min;
        public int g_max;
        public int b_min;
        public int b_max;

        OverLineSettings(int r_min, int r_max, int g_min, int g_max, int b_min, int b_max) {
            this.r_min = r_min;
            this.r_max = r_max;
            this.g_min = g_min;
            this.g_max = g_max;
            this.b_min = b_min;
            this.b_max = b_max;
        }

    }

    public boolean checkIfOverLine(OverLineSettings toCheck) {
        if(toCheck == OverLineSettings.OVER_ANY)
        {
            return (checkIfOverLine(OverLineSettings.OVER_RED) ||
                    checkIfOverLine(OverLineSettings.OVER_BLUE));
        }
        if(toCheck.r_min <= getRed() && getRed() >= toCheck.r_max &&
                toCheck.g_min <= getGreen() && getGreen() >= toCheck.g_max &&
                toCheck.b_min <= getBlue() && getBlue() >= toCheck.b_max)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

}
