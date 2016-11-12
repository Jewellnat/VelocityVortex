package org.firstinspires.ftc.teamcode;

/**
 * Created by mg15 on 10/29/2016.
 */

public class SpeedController {
    double startingMiliseconds;
    int targetSpeedRPM;
    int startingTicks;
    int currentRPM = 0;

    public SpeedController(int targetSpeedRPM) {
        this.targetSpeedRPM = targetSpeedRPM;
    }

    public void setTargetSpeedRPM(int targetSpeedRPM) {
        this.targetSpeedRPM = targetSpeedRPM;
    }

    public void Init(double startingMiliseconds, int startingTicks) {
        this.startingMiliseconds = startingMiliseconds;
        this.startingTicks = startingTicks;
    }

    public int getMeasuredRPM() {
        return currentRPM;
    }

    public double getMotorPower(double currentMiliseconds, int currentTIcks) {


        double deltaTime = (currentMiliseconds - startingMiliseconds);
        double deltaRevs = (currentTIcks - startingTicks) / Settings.shooterTicksPerRev;
        double retValue = 0;
        // times 1000 because time is milliseconds * 60 seconds per minute
        currentRPM = (int) ((deltaRevs / deltaTime) * 60000);

        if (currentRPM > targetSpeedRPM) {
            retValue = 0;
        }
        if (currentRPM <= targetSpeedRPM) {
            retValue = 1;
        }
        return retValue;
    }
}
