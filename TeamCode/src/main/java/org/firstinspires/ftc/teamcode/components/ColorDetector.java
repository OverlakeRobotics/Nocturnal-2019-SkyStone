package org.firstinspires.ftc.teamcode.components;

import android.util.Log;

import com.qualcomm.robotcore.hardware.ColorSensor;

    public class ColorDetector {

        private static final int RED_THRESHOLD = 200;
        private static final int BLUE_THRESHOLD = 200;

        private ColorSensor colorSensor;

        public ColorDetector(ColorSensor colorSensor) {
            this.colorSensor = colorSensor;
            colorSensor.enableLed(true);
        }

        public void telemetry() {
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
            return colorSensor.red() >= RED_THRESHOLD;
        }

        public boolean isBlue() {
            return colorSensor.blue() >= BLUE_THRESHOLD;
        }
    }
