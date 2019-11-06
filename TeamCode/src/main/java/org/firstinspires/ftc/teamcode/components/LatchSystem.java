package org.firstinspires.ftc.teamcode.components;

import com.qualcomm.robotcore.hardware.Servo;


public class LatchSystem {
    private Servo servo;
    private final double DOWN_POSITION = 0.9;
    private final double UP_POSITION = 0.05;
    public boolean latched;

    public LatchSystem(Servo servo) {
        this.servo = servo;
        initServo();
    }

    private void initServo() {
        servo.setPosition(UP_POSITION);
        servo.close();
    }

    public void run(boolean up, boolean down) {
        if (up) {
            unlatch();
        } else if (down) {
            latch();
        }
    }

    public void toggle() {
        if (latched) {
            unlatch();
            latched = false;
        }
        else if (!latched) {
            latch();
            latched = true;
        }
    }

    public void latch() {
        servo.setPosition(DOWN_POSITION);
        servo.close();
        latched = true;
    }

    public void unlatch() {
        servo.setPosition(UP_POSITION);
        servo.close();
        latched = false;
    }
}
