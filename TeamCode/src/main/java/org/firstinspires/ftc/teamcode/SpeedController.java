package org.firstinspires.ftc.teamcode;

/**
 * Created by mg15 on 10/29/2016.
 */

public class SpeedController {

    private double startingMiliseconds;
    private int targetSpeedRPM=0;
    private int startingTicks=0;
    private double currentRPM = 0;

    public SpeedController(int targetSpeedRPM){
        this.targetSpeedRPM = targetSpeedRPM;
    }

    public void setTargetSpeedRPM(int targetSpeedRPM){
      this.targetSpeedRPM = targetSpeedRPM;
    }

    public void Init(double startingMiliseconds, int startingTicks){
        this.startingMiliseconds = startingMiliseconds;
              this.startingTicks = startingTicks;
    }

    public double getMeasuredRPM(){
      //return the current Measured RPM
      return currentRPM;
    }

    public double getMotorSpeed(double currentMiliseconds, int currentTIcks){

        //delta time in minutes therefore need to divide by 60000
        double deltaTime = (currentMiliseconds - startingMiliseconds) / 60000;
        double deltaRevs = (currentTIcks - startingTicks) /Settings.shooterTicksPerRev;
        double retValue = 0;
        currentRPM = deltaRevs/deltaTime;

        if (currentRPM > targetSpeedRPM) {
          retValue = 0;
        }
        if (currentRPM <= targetSpeedRPM){
            retValue = 1;
        }
      return retValue;
     }
}
