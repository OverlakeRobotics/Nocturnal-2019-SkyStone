package org.firstinspires.ftc.teamcode.components.color;

public class Color {
    public int red;
    public int green;
    public int blue;
    public final static double TOLERANCE = 10;

    public Color(int red, int green, int blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }
    public boolean equals(Color other) {
        return isWithin(this.red, other.red) && isWithin(this.blue, other.blue) && isWithin(this.green, other.green);
    }

    private boolean isWithin(int a, int b) {
        return Math.abs(a - b) <= TOLERANCE;
    }
}
