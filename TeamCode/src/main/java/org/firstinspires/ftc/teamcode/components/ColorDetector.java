package org.firstinspires.ftc.teamcode.components;

import android.util.Log;

import com.qualcomm.robotcore.hardware.ColorSensor;

    public class ColorDetector {

        private static final Color red = new Color(255, 0,0);
        private static final Color blue = new Color(0,0,255);
        private static final Color yellow = new Color(255,255,0);
x
        private ColorSensor colorSensor;

        public ColorDetector(ColorSensor colorSensor) {
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
            return colorSensor.red() >= ;
        }

        public boolean isBlue() {
            return colorSensor.blue() >= BLUE_THRESHOLD;
        }

        public boolean isYellow() {
            return colorSensor.
        }
    }
