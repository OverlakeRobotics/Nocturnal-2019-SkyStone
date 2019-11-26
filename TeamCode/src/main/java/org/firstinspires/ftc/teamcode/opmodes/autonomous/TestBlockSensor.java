package org.firstinspires.ftc.teamcode.opmodes.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.ColorSensor;

import org.firstinspires.ftc.teamcode.opmodes.base.BaseOpMode;

@Autonomous(name = "TestBlockSensor", group = "Autonomous")
public class TestBlockSensor extends BaseOpMode {

    public boolean color;

    @Override
    public void init() {
        colorSensor = hardwareMap.get(ColorSensor.class, "intake_sensor");
    }
    public void loop() {
        if (colorSensor.alpha() > 1500) {
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
