package org.firstinspires.ftc.teamcode.opmodes.autonomous;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.components.DriveSystem;
import org.firstinspires.ftc.teamcode.opmodes.base.BaseOpMode;

@Autonomous(name = "WaitAuto", group="Autonomous")
public class WaitAuto extends BaseOpMode {
    private boolean arrived;
    private ElapsedTime time;

    // In seconds
    public static final int WAIT_TIME = 25;

    public void init() {
        super.init();
        arrived = false;
        time = new ElapsedTime();
        time.reset();
    }

    @Override
    public void loop() {
        if (!(time.milliseconds() > WAIT_TIME * 1000)) {
            return;
        }
        if (!arrived) {
            if (driveSystem.driveToPosition(400, DriveSystem.Direction.FORWARD, 1.0)) {
                arrived = true;
                return;
            }
        }
    }

}
