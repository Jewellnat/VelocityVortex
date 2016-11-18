package org.firstinspires.ftc.teamcode;

/**
 * Created by mg15 on 10/14/2016.
 */

//test small change

public class Settings {
    public final static int ticsPerRevoulution = 1440;
    public final static double wheelCircumfence = 8 * Math.PI; //wheel diameter * PI-
    public static double normalDriveSpeed = 0.5;
    public static int stageBlueCorner1Forward = 1;
    public static int stageBlueCorner2Right = 2;
    public static int stageBlueCorner3Line = 3;
    public static int getStageBlueCorner4Turn = 4;
    public static int stageBlueCorner5Fire = 5;
    public static int blueLine = 15;
    public static double gearratio = 1 / 1;//motor revolutions /wheel revolution

    public static int blueTapeAngle = 45;
    public static int fireAngle = 15;
    public static double lineFollowHigh = .8;
    public static double lineFollowLow = .25;
    public static int stage3Distance = 50;
    public static int stage3turn180 = 3;
    public static int stage4backup = 4;
    public static int stage5stop = 5;
    public static int cornerDriveDistance = 155;
    public static int middleDriveDistance = 140;
    public static int redTapeLightVal = 99;
    public static double GearRatio = 1 / 1; //motor revoulutions /wheel revoulutions
    public static double TicsPerCM = (GearRatio * ticsPerRevoulution) / wheelCircumfence;
    public static int RedTapeAngle = 315;
    public static double redTapeDistance = 60;
    public static int firstLaunch = 4;
    public static int firstReset = 5;
    public static int secondLaunch = 7;
    public static int secondReset = 8;
    public static int turnOffShooter = 9;
    public static double launch = .6;
    public static double reset = 0;
    public static double shooterSpeedTeleOP = .25;
    public static double spinnerShooterAuto = .3;
    public static double bestShooterSpeed = 120 * Math.PI;
    public static int beaconRight = 1;
    public static int beaconLeft = 0;
    public static int stage1FIRE = 1;
    public static int stage2Charge = 2;
    public static int stage3Stop = 3;
    public static double driveSpeed = 1;
    public static double spinnerShooterMiddle = .25;
    public static int shooterTicksPerRev = 28;
    public static double shooterMotorMaxRPM = 6000;
    public static double shooterRPM = 4150;

    public static double Tics2CM(int tics) {
        return tics / TicsPerCM;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }
}