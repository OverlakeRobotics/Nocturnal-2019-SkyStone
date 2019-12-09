package org.firstinspires.ftc.teamcode.components;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.Range;
import java.util.EnumMap;

public class DriveSystem {

    public enum MotorNames {
        FRONTLEFT, FRONTRIGHT, BACKRIGHT, BACKLEFT
    }
    public static final double SLOW_DRIVE_COEFF = 0.4;
    public boolean slowDrive;
    public EnumMap<MotorNames, DcMotor> motors;

    /**
     * Handles the data for the abstract creation of a drive system with four wheels
     */
    public DriveSystem(EnumMap<MotorNames, DcMotor> motors) {
        this.motors = motors;
        initMotors();
    }

    /**
     * Set the power of the drive system
     * @param power power of the system
     */
    public void setMotorPower(double power) {
        for (DcMotor motor : motors.values()) {
            motor.setPower(power);
        }
    }

    public void initMotors() {
        motors.forEach((name, motor) -> {
            motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            switch(name) {
                case FRONTLEFT:
                    motor.setDirection(DcMotorSimple.Direction.REVERSE);
                    break;
                case FRONTRIGHT:
                case BACKRIGHT:
                case BACKLEFT:
                    motor.setDirection(DcMotorSimple.Direction.FORWARD);
                    break;
            }
        });
        setMotorPower(0);
    }

    public void slowDrive(boolean on) {
        slowDrive = on;
    }

    private void setDriveSpeed(DcMotor motor, double motorPower) {
        motor.setPower(Range.clip(slowDrive ?
                SLOW_DRIVE_COEFF * motorPower : motorPower, -1, 1));
    }

    /**
     * Clips joystick values and drives the motors.
     * @param rightX Right X joystick value
     * @param leftX Left X joystick value
     * @param leftY Left Y joystick value in case you couldn't tell from the others
     */
    public void drive(float rightX, float leftX, float leftY) {
        // Prevent small values from causing the robot to drift
        if (Math.abs(rightX) < 0.01) {
            rightX = 0.0f;
        }
        if (Math.abs(leftX) < 0.01) {
            leftX = 0.0f;
        }
        if (Math.abs(leftY) < 0.01) {
            leftY = 0.0f;
        }

        double frontLeftPower = -leftY + rightX + leftX;
        double frontRightPower = -leftY - rightX - leftX;
        double backLeftPower = -leftY + rightX - leftX;
        double backRightPower = -leftY - rightX + leftX;

        motors.forEach((name, motor) -> {
            switch(name) {
                case FRONTRIGHT:
                    setDriveSpeed(motor, frontRightPower);
                    break;
                case BACKLEFT:
                    setDriveSpeed(motor, backLeftPower);
                    break;
                case FRONTLEFT:
                    setDriveSpeed(motor, frontLeftPower);
                    break;
                case BACKRIGHT:
                    setDriveSpeed(motor, backRightPower);
                    break;
            }
        });
        slowDrive = false;
    }
}