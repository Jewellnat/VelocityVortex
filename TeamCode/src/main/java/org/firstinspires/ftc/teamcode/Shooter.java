package org.firstinspires.ftc.teamcode;

/*
 * Created by mg15 on 10/26/2016.
 */

import com.qualcomm.robotcore.hardware.DcMotor;

public class Shooter {
      double lastLoopTime;
      int lastLeftEncoder = 0;
      int lastRightEncoder = 0;
    public double CaculateLeftSpeed(double currentTime, DcMotor Motor) {
        int leftEncoder = Motor.getCurrentPosition();
        double speed =0 ;
        speed = (Settings.Tics2CM(leftEncoder-lastLeftEncoder))/(currentTime-lastLoopTime);
        lastLeftEncoder = leftEncoder;
        return speed;
    }
    public double CaculateRightSpeed(double currentTime, DcMotor Motor) {
        int rightEncoder = Motor.getCurrentPosition();
        double speed = 0;
        speed = (Settings.Tics2CM(rightEncoder - lastRightEncoder)) / (currentTime - lastLoopTime);
        lastRightEncoder = rightEncoder;
        return speed;
    }
    public double setSpeedLeft(double currentTime, DcMotor Motor){
        double motorSpeed = 0;
        double leftSpeed = CaculateLeftSpeed(currentTime, Motor);
        if (leftSpeed < Settings.bestShooterSpeed) {
          motorSpeed = 1;
        }
        return motorSpeed;

    }
    public double setSpeedRight(double currentTime, DcMotor Motor){
        double motorSpeed = 1;
        double rightSpeed = CaculateLeftSpeed(currentTime, Motor);
        if (rightSpeed < Settings.bestShooterSpeed) {
            motorSpeed = 1;
        }
        return motorSpeed;

    }
}
