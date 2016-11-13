package org.firstinspires.ftc.teamcode;

/**
 * Created by mg15 on 10/29/2016.
 */

import com.qualcomm.robotcore.util.ElapsedTime;

public class SpeedController {
    double startingMiliseconds;
    int targetSpeedRPM;
    int startingTicks;
    double  currentRPM = 0;
    private ElapsedTime speedTimer = new ElapsedTime();;

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

    public double getMeasuredRPM() {
        return currentRPM;
    }

    public double getMotorPower(double deltaTime, int deltaTicks) {
        double deltaRevs = deltaTicks / Settings.shooterTicksPerRev;
        double retValue = 0;
        // times 1000 because time is milliseconds * 60 seconds per minute
        currentRPM = (deltaRevs / deltaTime) * 60000;

        if (currentRPM > targetSpeedRPM) {
            retValue = 0;
        }
        if (currentRPM <= targetSpeedRPM) {
            retValue = 1;
        }
        return retValue;
    }
}
