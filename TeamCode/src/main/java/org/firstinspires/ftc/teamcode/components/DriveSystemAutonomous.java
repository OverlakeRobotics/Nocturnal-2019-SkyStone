package org.firstinspires.ftc.teamcode.components;

import android.util.Log;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.Range;

import java.util.EnumMap;

public class DriveSystemAutonomous extends DriveSystem {

    public enum Direction {
        FORWARD, BACKWARD, LEFT, RIGHT;

        private static boolean isStrafe(Direction direction) {
            return direction == LEFT || direction == RIGHT;
        }
    }

    public static final String TAG = "DriveSystem";
    public static final double P_TURN_COEFF = 0.018;     // Larger is more responsive, but also less stable
    public static final double HEADING_THRESHOLD = 1 ;      // As tight as we can make it with an integer gyro
    public IMUSystem imuSystem;
    private int mTargetTicks;
    private double mTargetHeading;
    private double mInitHeading;
    private int mTurnCounter;

    // 12.566370614359173 inches circumference of a wheel
    // 319.185813604722993 mm circumference of a wheel
    // 1120 ticks in a revolution
    // 1120 / 319.185813604722993 = 3.508927879
    private final double TICKS_IN_MM = 3.508927879;

    /**
     * Handles the data for the abstract creation of a drive system with four wheels
     */
    public DriveSystemAutonomous(EnumMap<MotorNames, DcMotor> motors, BNO055IMU imu) {
        super(motors);
        mTargetTicks = 0;
        mTurnCounter = 0;
        imuSystem = new IMUSystem(imu);
    }

    public boolean driveToPositionTicks(int ticks, Direction direction, double maxPower, double heading) {
        if(mTargetTicks == 0){
            mTargetTicks = direction == Direction.BACKWARD ? -ticks : ticks;
            mTargetHeading = heading;
            motors.forEach((name, motor) -> {
                motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                if(Direction.isStrafe(direction)) {
                    int sign = direction == Direction.LEFT ? -1 : 1;
                    switch(name){
                        case FRONTLEFT:
                        case BACKRIGHT:
                            motor.setTargetPosition(sign * mTargetTicks);
                            break;
                        case FRONTRIGHT:
                        case BACKLEFT:
                            motor.setTargetPosition(sign * -mTargetTicks);
                            break;
                    }
                } else {
                    motor.setTargetPosition(mTargetTicks);
                }
                motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            });
        }

        for (MotorNames motorName : motors.keySet()) {
            DcMotor motor = motors.get(motorName);
            int offset = Math.abs(motor.getCurrentPosition() - mTargetTicks);
            if(offset <= 15){
                // Shut down motors
                setMotorPower(0);
                // Reset motors to default run mode
                setRunMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                // Reset target
                mTargetTicks = 0;
                mTargetHeading = 0;
                // Motor has reached target
                Log.d(TAG, "Finished");
                return true;
            } else {
                int sign;
                double difference = motor.getTargetPosition() - motor.getCurrentPosition();
                // Run the motor in the positive direction
                if (difference > 0) {
                    sign = 1;
                }
                // Run the motor in the positive direction
                else {
                    sign = -1;
                }
                double powerAdjustment = getError(imuSystem.getHeading()) / 70.0;
                Log.d(TAG, "Power adjustment: " + powerAdjustment);
                // https://www.desmos.com/calculator/cnixjfjec
                // I just made up a ramping function that looks good, x is the number of encoder
                // ticks left. Aka the difference
                double power = sign * Range.clip((Math.pow(Math.abs(difference), 0.6) / 15.0), 0.1, maxPower);
                if (motorName == MotorNames.FRONTRIGHT) {
                    power -= powerAdjustment;
                } else if (motorName == MotorNames.BACKRIGHT) {
                    power += powerAdjustment;
                } else if (motorName == MotorNames.FRONTLEFT) {
                    power -= powerAdjustment;
                } else if (motorName == MotorNames.BACKLEFT) {
                    power += powerAdjustment;
                }

                Log.d(TAG, motorName + ": " + power);
                motor.setPower(power);
            }
        }
        Log.d(TAG, "\n");
        // Motor has not reached target
        return false;
    }

    public void stopAndReset() {
        setMotorPower(0.0);
        mTargetTicks = 0;
        mTargetHeading = 0;
    }

    public void setRunMode(DcMotor.RunMode runMode) {
        for (DcMotor motor : motors.values()) {
            motor.setMode(runMode);
        }
    }

    /**
     * Gets the minimum distance from the target
     * @return
     */
    public int  getMinDistanceFromTarget() {
        int distance = Integer.MAX_VALUE;
        for (DcMotor motor : motors.values()) {
            distance = Math.min(distance, motor.getTargetPosition() - motor.getCurrentPosition());
        }
        return distance;
    }

    public boolean driveToPosition(int millimeters, Direction direction, double maxPower, double heading) {
        // Since no heading is passed in just give it the current heading such it doesn't have
        // any impact
        return driveToPositionTicks(millimetersToTicks(millimeters), direction, maxPower, heading);
    }
    public boolean driveToPosition(int millimeters, Direction direction, double maxPower) {
        // Since no heading is passed in just give it the current heading such it doesn't have
        // any impact
        return driveToPositionTicks(millimetersToTicks(millimeters), direction, maxPower, imuSystem.getHeading());
    }

    /**
     * Converts millimeters to ticks
     * @param millimeters Millimeters to convert to ticks
     * @return
     */
    public int millimetersToTicks(int millimeters) {
        return (int) Math.round(millimeters * TICKS_IN_MM);
    }

    /**
     * Turns relative the heading upon construction
     * @param degrees The degrees to turn the robot by
     * @param maxPower The maximum power of the motors
     */
    public boolean turnAbsolute(double degrees, double maxPower) {
        // Since it is vertical, use pitch instead of heading
        return turn(diffFromAbs(degrees), maxPower);
    }

    /**
     * Turns the robot by a given number of degrees
     * @param degrees The degrees to turn the robot by
     * @param maxPower The maximum power of the motors
     */
    public boolean turn(double degrees, double maxPower) {
        // Since controller hub is vertical, use pitch instead of heading
        double heading = imuSystem.getHeading();
        // if controller hub is flat: double heading = imuSystem.getHeading();
        Log.d(TAG,"Current Heading: " + heading);
        if(mTargetHeading == 0) {
            mTargetHeading = (heading + degrees) % 360;
            Log.d(TAG, "Setting Heading -- Target: " + mTargetHeading);

            Log.d(TAG, "Degrees: " + degrees);
        }
        double difference = mTargetHeading - heading;
        if (mTurnCounter == 0) {
            mInitHeading = difference;
        }
        Log.d(TAG,"Difference: " + difference);
        mTurnCounter++;
        return onHeading(maxPower, heading);

    }

    /**
     * Perform one cycle of closed loop heading control.
     * @param speed     Desired speed of turn
     */
    public boolean onHeading(double speed, double heading) {
        double leftSpeed;

        // determine turn power based on +/- error
        double error = getError(heading);

        // If it gets there: stop
        if (Math.abs(error) <= HEADING_THRESHOLD) {
            mTargetHeading = 0;
            setMotorPower(0);
            mTurnCounter = 0;
            return true;
        }

        // Go full speed until 60% there
        leftSpeed = error > Math.abs(0.85 * (mInitHeading)) ? speed : (speed * getSteer(error));
        // leftSpeed = speed * getSteer(error);


        if (leftSpeed < 0) {
            leftSpeed = Range.clip(leftSpeed, -1.0, -0.22);
        } else {
            leftSpeed = Range.clip(leftSpeed, 0.22, 1.0);
        }
        Log.d(TAG,"Left Speed: " + leftSpeed);
        // Send desired speeds to motors.
        tankDrive(leftSpeed, -leftSpeed);

        return false;
    }

    /**
     * getError determines the error between the target angle and the robot's current heading
     * @param   heading  Desired angle (relative to global reference established at last Gyro Reset).
     * @return  error angle: Degrees in the range +/- 180. Centered on the robot's frame of reference
     *          +ve error means the robot should turn LEFT (CCW) to reduce error.
     */
    public double getError(double heading) {
        // calculate error in -179 to +180 range  (
        double robotError = mTargetHeading - heading;
        Log.d(TAG,"Robot Error: " + robotError);
        while (robotError > 180) {
            robotError -= 360;
        }
        while (robotError <= -180) {
            robotError += 360;
        }
        return robotError;
    }

    public double diffFromAbs(double heading) {
        // calculate error in -179 to +180 range
        // When vertical use pitch instead of heading
        double robotDiff = heading - imuSystem.getHeading();
        Log.d(TAG,"Difference from initial: " + robotDiff);
        while (robotDiff > 180) {
            robotDiff -= 360;
        }
        while (robotDiff <= -180) {
            robotDiff += 360;
        }
        return robotDiff;
    }

    /**
     * returns desired steering force.  +/- 1 range.  +ve = steer left
     * @param error   Error angle in robot relative degrees
     * @return
     */
    // TODO
    public double getSteer(double error) {
        return Range.clip(error *  P_TURN_COEFF, -1, 1);
    }

    /**
     * Causes the system to tank drive
     * @param leftPower sets the left side power of the robot
     * @param rightPower sets the right side power of the robot
     */
    public void tankDrive(double leftPower, double rightPower) {
        motors.forEach((name, motor) -> {
            switch(name) {
                case FRONTLEFT:
                case BACKLEFT:
                    motor.setPower(leftPower);
                    break;
                case FRONTRIGHT:
                case BACKRIGHT:
                    motor.setPower(rightPower);
                    break;
            }
        });
    }
}