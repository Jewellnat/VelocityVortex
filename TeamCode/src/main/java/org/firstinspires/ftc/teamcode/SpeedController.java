package org.firstinspires.ftc.teamcode;

/**
 * Created by mg15 on 10/29/2016.
 */

import com.qualcomm.robotcore.util.ElapsedTime;

public class SpeedController {
    double targetSpeedRPM;
    double  currentRPM = 0;

    public SpeedController(double targetSpeedRPM) {

        this.targetSpeedRPM = targetSpeedRPM;
    }

    public void setTargetSpeedRPM(double targetSpeedRPM) {
        this.targetSpeedRPM = targetSpeedRPM;
    }


    public double getMeasuredRPM() {
        return currentRPM;
    }

    public double getMotorPower(double deltaMilliSec, int deltaTicks) {
        double retValue = 0;
        currentRPM = (deltaTicks/ deltaMilliSec * 60000 / Settings.shooterTicksPerRev);
        if (currentRPM <= targetSpeedRPM) {
            retValue = 1;
        }
        return retValue;
    }
}
