package org.firstinspires.ftc.teamcode.opmodes.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.opmodes.base.BaseOpMode;

@Autonomous(name = "TestBlockSensor", group = "Autonomous")
public class TestBlockSensor extends BaseOpMode {

    public boolean color;
    public void init() {
        super.init();
    }
    public void loop() {
        if (colorSensor.alpha() > 150) {
            color = true;
        }
        else {
            color = false;
        }
        telemetry.addData("color", color);
        telemetry.addData("r", colorSensor.red());
        telemetry.addData("g", colorSensor.green());
        telemetry.addData("b", colorSensor.blue());
        telemetry.addData("a", colorSensor.alpha());
        telemetry.update();
    }
}
