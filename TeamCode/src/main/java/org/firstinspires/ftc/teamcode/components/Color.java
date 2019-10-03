package org.firstinspires.ftc.teamcode.components;

public class Color {
    public int red;
    public int green;
    public int blue;
    public final static double tolerance = 10;
    public Color(int red, int green, int blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }
    public boolean equals(Color other) {
        int sum = other.red + other.green + other.blue;
        return isWithin(this.red / sum, other.red / sum) && isWithin(this.green / sum, other.green / sum) && isWithin(this.blue / sum, other.blue / sum);
    }

    private boolean isWithin(int a, int b) {
        return Math.abs(a - b) <= tolerance;
    }
}
