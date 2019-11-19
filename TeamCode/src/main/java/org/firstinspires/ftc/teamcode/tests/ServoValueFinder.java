package org.firstinspires.ftc.teamcode.tests;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.opmodes.base.BaseOpMode;

@TeleOp(name = "ServoValueFinder", group = "Test")
public class ServoValueFinder extends BaseOpMode {

    private Servo wrist;
    private Servo elbow;
    private Servo pivot;

    public void init() {
        this.wrist = hardwareMap.servo.get("wrist");
        this.elbow = hardwareMap.servo.get("elbow");
        this.pivot = hardwareMap.servo.get("pivot");
    }
    public void loop() {

        // x + b - move wrist
        // y + a - move elbow
        // dpad left + right - move pivot

        //if button pressed
        if (gamepad1.b) {
            wrist.setPosition(wrist.getPosition() + 0.01);
        }

        if (gamepad1.x) {
            wrist.setPosition(wrist.getPosition() - 0.01);
        }

        if (gamepad1.a) {
            elbow.setPosition(elbow.getPosition() + 0.01);
        }

        if (gamepad1.y) {
            elbow.setPosition(elbow.getPosition() - 0.01);
        }

        if (gamepad1.dpad_left) {
            pivot.setPosition(pivot.getPosition() + 0.01);
        }

        if (gamepad2.dpad_right) {
            pivot.setPosition(pivot.getPosition() - 0.01);
        }

        telemetry.addLine("Jared is dumb stinky poophead.");
        telemetry.addData("wrist:", wrist.getPosition());
        telemetry.addData("elbow:", elbow.getPosition());
        telemetry.addData("pivot:", pivot.getPosition());
    }
}