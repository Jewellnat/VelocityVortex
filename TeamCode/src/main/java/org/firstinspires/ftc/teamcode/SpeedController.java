package org.firstinspires.ftc.teamcode;

/**
 * Created by mg15 on 10/29/2016.
 */

import com.qualcomm.robotcore.util.ElapsedTime;

public class SpeedController {
    private double targetSpeedRPM;
    private double  currentRPM = 0;
    private double tol = 250;
    private double lowerLimit = 0;
    private double upperLimit = 0;
    public SpeedController(double targetSpeedRPM) {

        this.targetSpeedRPM = targetSpeedRPM;
        lowerLimit = targetSpeedRPM - tol;
        upperLimit = targetSpeedRPM + tol;
    }

    public void setTargetSpeedRPM(double targetSpeedRPM) {
        this.targetSpeedRPM = targetSpeedRPM;
    }


    public double getMeasuredRPM() {
        return currentRPM;
    }

    public double getMotorPower(double deltaMilliSec, int deltaTicks) {
        //special case of 0  stop the motors
        if (targetSpeedRPM  < 1) {return (0);}

        double retValue = 0;
        currentRPM = (deltaTicks/ deltaMilliSec * 60000 / Settings.shooterTicksPerRev);

        //If we are close try to stay there.
        if ((currentRPM > lowerLimit)  && (currentRPM <= targetSpeedRPM)){
             retValue = targetSpeedRPM/Settings.shooterMotorMaxRPM;
        }
        else if ((currentRPM < upperLimit)  && (currentRPM > targetSpeedRPM)){
             retValue = .3 * targetSpeedRPM/Settings.shooterMotorMaxRPM;
        }
        else if (currentRPM <= lowerLimit) {
            retValue = 1.0;
        }
        return retValue;
    }
}
