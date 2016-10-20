package org.firstinspires.ftc.teamcode;

/**
 * Created by mg15 on 10/14/2016.
 */

public class Settings {
    public static int stageRedCorner1Forward = 1;
    public static int stageRedCorner2TurnLeft = 2;
    public static double normalDriveSpeed = 0.75;
    public static int redTapeLightVal = 99;
    public static double GearRatio = 3 / 1; //motor revoulutions /wheel revoulutions
    public final static int ticsPerRevoulution = 1440;
    public final static double wheelCircumfence = 10*Math.PI; //wheel diameter * PI
    public static double TicsPerCM = (GearRatio *ticsPerRevoulution)/wheelCircumfence;

    public static double Tics2CM (int tics){
        return tics / TicsPerCM;

    }
}