package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.components.color.ColorSystem;

@TeleOp(name = "SensorColorTest", group="TeleOp")
public class ColorSensorTest extends LinearOpMode {
    public void runOpMode()
    {
        ColorSystem cs = new ColorSystem(this);
        while (!isStopRequested())
        {
            telemetry.addData("R: ", cs.getRed());
            telemetry.addData("G: ", cs.getGreen());
            telemetry.addData("B: ", cs.getBlue());
            telemetry.update();
        }
    }
}
