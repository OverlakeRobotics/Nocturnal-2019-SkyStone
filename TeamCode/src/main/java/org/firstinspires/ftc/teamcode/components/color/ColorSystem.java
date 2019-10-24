package org.firstinspires.ftc.teamcode.components.color;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class ColorSystem {

    private static final Color RED = new Color(255, 0,0);
    private static final Color BLUE = new Color(0,0,255);
    private static final Color YELLOW = new Color(255,255,0);

    private final Color RED_LINE = new Color(255, 0, 0);//TODO: ADJUST VALUES
    private final Color BLUE_LINE = new Color(0, 0, 255);

    ColorSensor colorSensor;

    public ColorSystem(OpMode opMode) {
        colorSensor = opMode.hardwareMap.colorSensor.get("color_sensor");
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

    public Color getColor() {
        return new Color(getRed(), getGreen(), getBlue());
    }

    public void bLed(boolean lightOn) {
        colorSensor.enableLed(lightOn);
    }

    public enum OverLineSettings {
        OVER_ANY, //NEVER USES TRADITIONAL 0-255 COLOR VALUES
        //TODO CHANGE TO PERCENTAGE RBG AS DECIMAL
        OVER_RED (3, 1, 1),
        OVER_BLUE (1, 2, 4);

        public double r;
        public double g;
        public double b;

        OverLineSettings(int r, int g, int b) {
            int total = r + g + b;
            this.r = r/total;
            this.g = g/total;
            this.b = b/total;
        }

        OverLineSettings() { }
    }

    public boolean checkIfOverLine(OverLineSettings toCheck) {
        if (toCheck == OverLineSettings.OVER_ANY) {
            return (checkIfOverLine(OverLineSettings.OVER_RED) ||
                    checkIfOverLine(OverLineSettings.OVER_BLUE));
        }
        double totalOfColors = toCheck.r + toCheck.g + toCheck.b;
        if (withinMargin(colorSensor.red()/totalOfColors, toCheck.r) &&
                withinMargin(colorSensor.blue()/totalOfColors, toCheck.b) &&
                withinMargin(colorSensor.green(), toCheck.g)) {
            return true;
        } else {
            return false;
         }
    }

    private boolean withinMargin(double a, double b)
    {
        double allowedDifferenceAsDecimalOfPercent = .25;
        if(a >= b*(1-(0.5*allowedDifferenceAsDecimalOfPercent)) && a <= (1+(0.5*allowedDifferenceAsDecimalOfPercent)))
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}
