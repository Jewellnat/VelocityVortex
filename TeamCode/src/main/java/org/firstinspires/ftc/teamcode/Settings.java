package org.firstinspires.ftc.teamcode;

/**
 * Created by mg15 on 10/14/2016.
 */

public class Settings {
    //Chassis settings
    public static double chassis_KPLineFollow = .3;
    public static double chassis_KPGyroStraight = .003;
    public static int  chassis_GyroHeadingTol = 7;
    public static int chassis_TurnMilliSeconds = 5000;
    public static double chassis_TurnMotorPower = .25;
    public static double chassis_Width = 42;
    public static double chassis_KMotorBalance = .82;

    public static int stageRedCorner1Forward = 1;
    public static int stageRedCorner2TurnLeft = 2;
    public static int stageRedCorner3LineFollow = 3;
    public static int stageRedCorner4AlignBucket =4;
    public static int bucketAngle=300;
    public static int stagecorner1shoot = 5;
    public static int stageDriveForwardcorner2 = 6;
    public static int stageStoppingcorner3 = 7;
    public static int stagemiddle1shoot = 8;
    public static int stageDriveForwardmiddle2 = 9;
    public static int stageStoppingmiddle3 = 10;
    public static int stageShooterSpinUp = 11;
    public static double normalDriveSpeed = 0.75;
    public static int cornerDriveDistance = 155;
    public static int middleDriveDistance = 100;
    public static int redTapeLightVal = 99;
    public static double GearRatio = 1 / 1; //motor revoulutions /wheel revoulutions
    public final static int ticsPerRevoulution = 1440;
    public final static double wheelCircumfence = 8*Math.PI; //wheel diameter * PI
    public static double TicsPerCM = (GearRatio *ticsPerRevoulution)/wheelCircumfence;
    public static int RedTapeAngle = 315;
    public static double redTapeDistance= 60;
    public static int firstLaunch = 4;
    public static int firstReset = 5;
    public static int secondLaunch = 7;
    public static int secondReset = 8;
    public static int turnOffShooter = 9;
    public static double launch = .4;
    public static double reset = 0;
    public static double shooterSpeedTeleOP = .4;
    public static double spinnerShooterAuto = .3;
    public static double bestShooterSpeed = 120 * Math.PI;
    public static int beaconRight = 1;
    public static int beaconLeft = 0;
    public static int stage1FIRE = 12;
    public static int stage2Charge = 13;
    public static int stage3Stop = 14;
    public static double driveSpeedL = -1;
    public static double driveSpeedR = driveSpeedL * chassis_KMotorBalance;
    public static double spinnerShooterMiddle = .25;
    public static int shooterTicksPerRev = 28;
    public static int shooterMotorMaxRPM = 6000;
    public static double shooterMotorRPM = 4200;
    public static double shooter_Kp = .00009;
    public static double posTriggerTol = .0;
    public static int stage3turn180 = 15;
    public static int stage4backup = 16;
    public static int middleBackupDriveDistance = -20;
    public static int stage5stop = 17;
    public static int stageCornerShoot1Drive = 18;
    public static int stageConerShoot2Shoot = 19;
    public static int stageConerShoot3Stop = 20;
    public static int cornerShootDriveDistence = 45;
    public static int driveMoterMaxTicksPerSec = 150 * ticsPerRevoulution / 60; //motor rpm * ticsPerRevoulution / 60 sec



    public static double Tics2CM (int tics){

        return tics / TicsPerCM;

    }
}
