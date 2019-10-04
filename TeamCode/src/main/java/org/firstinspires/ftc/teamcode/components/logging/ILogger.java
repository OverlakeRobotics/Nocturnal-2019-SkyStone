package org.firstinspires.ftc.teamcode.components.logging;

public interface ILogger
{
    void log(String name, Object data, Object... args);
    void write();
}
