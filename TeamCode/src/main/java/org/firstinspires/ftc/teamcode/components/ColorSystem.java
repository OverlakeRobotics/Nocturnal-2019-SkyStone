package org.firstinspires.ftc.teamcode.components;

import android.util.Log;

import com.qualcomm.robotcore.hardware.ColorSensor;

    public class ColorSystem {

        private static final Color red = new Color(255, 0,0);
        private static final Color blue = new Color(0,0,255);
        private static final Color yellow = new Color(255,255,0);

        private ColorSensor colorSensor;

        public ColorSystem(ColorSensor colorSensor) {
            this.colorSensor = colorSensor;
            colorSensor.enableLed(true);
        }

        public void debug() {
            Log.d("colorSensor", "Alpha: " + colorSensor.alpha());
            Log.d("colorSensor", "Red: " + colorSensor.red());
            Log.d("colorSensor", "Green: " + colorSensor.green());
            Log.d("colorSensor", "Blue: " + colorSensor.blue());
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

        public int getAlpha() {
            return colorSensor.alpha();
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

    }
