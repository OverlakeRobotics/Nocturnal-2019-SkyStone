package org.firstinspires.ftc.teamcode.opmodes.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.components.color.ColorSystem;
import org.firstinspires.ftc.teamcode.components.LidarNavigationSystem;

@Autonomous(name = "Sensor")
public class Sensor extends LinearOpMode {
    public ColorSystem colorSystem;

    public LidarNavigationSystem distance1;

    @Override
    public void runOpMode() {

        colorSystem = new ColorSystem(this);
        distance1 = new LidarNavigationSystem(this);

        waitForStart();
        while (opModeIsActive()) {
            colorSystem.bLed(false);

            telemetry.addData("Alpha:", colorSystem.getAlpha() / 39);
            telemetry.addData("Red:", colorSystem.getRed() / 39);
            telemetry.addData("Green:", colorSystem.getGreen() / 39);
            telemetry.addData("Blue:", colorSystem.getBlue() / 39);

            telemetry.addData("isRed:", colorSystem.isRed());
            telemetry.addData("isBlue:", colorSystem.isBlue());
            telemetry.addData("isYellow:", colorSystem.isYellow());

            telemetry.addData("dist1:", distance1.getDistance1());
            telemetry.addData("dist2:", distance1.getDistance2());

            telemetry.update();
        }
    }

}
