package org.firstinspires.ftc.teamcode.tests;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.opmodes.base.BaseOpMode;

@TeleOp(name = "MotorEncoderTest", group = "Test")
public class ServoValueFinder extends BaseOpMode {

    public Servo wrist;
    public Servo elbow;
    public Servo pivot;

    public void init() {
        this.wrist = hardwareMap.servo.get("wrist");
        this.elbow = hardwareMap.servo.get("elbow");
        this.pivot = hardwareMap.servo.get("pivot");
    }
    public void loop() {

        // x + b - move wrist
        // y + a - move elbow
        // bumpers - move pivot

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

        if (gamepad1.left_bumper) {
            pivot.setPosition(pivot.getPosition() + 0.01);
        }

        if (gamepad2.right_bumper) {
            pivot.setPosition(pivot.getPosition() - 0.01);
        }

        telemetry.addData("wrist:", wrist.getPosition());
        telemetry.addData("elbow:", elbow.getPosition());
        telemetry.addData("pivot:", pivot.getPosition());
    }
}