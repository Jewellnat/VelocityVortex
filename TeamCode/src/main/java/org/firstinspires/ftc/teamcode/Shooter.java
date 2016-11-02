package org.firstinspires.ftc.teamcode;

/*
 * Created by mg15 on 10/26/2016.
 */

import com.qualcomm.robotcore.hardware.DcMotor;

public class Shooter {
      double lastLoopTime;
      int lastLeftEncoder;
      int lastRightEncoder;
    public double CaculateLeftSpeed(double currentTime, DcMotor Motor) {
        double speed =0 ;
        speed = (Settings.Tics2CM(Motor.getCurrentPosition()-lastLeftEncoder))/(currentTime-lastLoopTime);
        return speed;
    }
    public double CaculateRightSpeed(double currentTime, DcMotor Motor) {
        double speed = 0;
        speed = (Settings.Tics2CM(Motor.getCurrentPosition() - lastRightEncoder)) / (currentTime - lastLoopTime);
        return speed;
    }
    public double setSpeedLeft(double currentTime, DcMotor Motor){
        double leftSpeed = CaculateLeftSpeed(currentTime, Motor);
        if (leftSpeed > Settings.bestShooterSpeed) {

        }
        return 0;
    }
}
