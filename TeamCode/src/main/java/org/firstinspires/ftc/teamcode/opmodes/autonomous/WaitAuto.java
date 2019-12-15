package org.firstinspires.ftc.teamcode.opmodes.autonomous;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

public abstract class WaitAuto extends BaseAutonomous {
    private boolean waited;
    private boolean arrived;

    // In seconds
    public static final int WAIT_TIME = 15;

    public void init(Team team) {
        super.init(team);
        waited = false;
        arrived = false;

    }

    @Override
    public void loop() {
        if (!waited) {
            try {
                Thread.sleep(WAIT_TIME * 1000);
                waited = true;
            } catch (InterruptedException ie) {

            }
        }
        if (!arrived) {
            if (currentTeam == Team.RED && colorSensor.red() > colorSensor.blue() * 1.25) {
                driveSystem.drive(0, 0, 0.0f);
                arrived = true;
                return;
            } else if (colorSensor.blue() > colorSensor.red() * 1.25) {
                driveSystem.drive(0, 0, 0.0f);
                arrived = true;
                return;
            }
            driveSystem.drive(0, 0, -0.75f);
        }
    }

}
