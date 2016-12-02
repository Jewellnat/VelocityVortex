/*
Copyright (c) 2016 Robert Atkinson

All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted (subject to the limitations in the disclaimer below) provided that
the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list
of conditions and the following disclaimer.

Redistributions in binary form must reproduce the above copyright notice, this
list of conditions and the following disclaimer in the documentation and/or
other materials provided with the distribution.

Neither the name of Robert Atkinson nor the names of his contributors may be used to
endorse or promote products derived from this software without specific prior
written permission.

NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESSFOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.LightSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * This file contains an example of an iterative (Non-Linear) "OpMode".
 * An OpMode is a 'program' that runs in either the autonomous or the teleop period of an FTC match.
 * The names of OpModes appear on the menu of the FTC Driver Station.
 * When an selection is made from the menu, the corresponding OpMode
 * class is instantiated on the Robot Controller and executed.
 * <p>
 * This particular OpMode just executes a basic Tank Drive Teleop for a PushBot
 * It includes all the skeletal structure that all iterative OpModes contain.
 * <p>
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

public class Chassis extends OpMode {
    public static int driveModeStopped = 0;
    public static int driveModeStraightByGyro = 1;
    public static int driveModeTurnByGyro = 2;
    public static int driveModeLineFollow = 3;

    private int driveModeCurrent = driveModeStopped;   //one of the above modes  use in loop

    private boolean moveIsComplete = false;   //while driving this is false... while in position it is true

    private double motorPowerTarget = 0;     //while looping this is our target motor Power
    private int headingTarget = 0;           // while driving by gyro this is our target heading
    private double distance2TargetCM = 0;    // while driviing this is are destination distance
    private double lightSensorTarget = 0;    //while driving by line follow this is the target light sensor value
    private double leftPower = 0;
    private double rightPower = 0;
    private double storedCMTraveled = 0;     //for debugging store it for telemetry;
    private int storedGyroHeading = 0;    //for debugging store it for telemetry;
    private int gyroOffset = 0;               //gyro offset for driving backwards  will be 0 and 180

    /* Declare OpMode members. */
    private DcMotor leftDriveMotor = null;
    private DcMotor rightDriveMotor = null;
    private DcMotor sweeperMotor = null;
    private GyroSensor gyroSensor = null;
    private ElapsedTime runtime = new ElapsedTime();
    private LightSensor lightSensorLineFollow = null;
    int stage;


    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init() {

        telemetry.addData("Status", "Initialized");
        sweeperMotor = hardwareMap.dcMotor.get("sweeperMotor");

        leftDriveMotor = hardwareMap.dcMotor.get("leftDriveMotor");
        rightDriveMotor = hardwareMap.dcMotor.get("rightDriveMotor");
        leftDriveMotor.setDirection(DcMotor.Direction.REVERSE);

        leftDriveMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightDriveMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftDriveMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightDriveMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        gyroSensor = hardwareMap.gyroSensor.get("gyroSensor");
        gyroSensor.calibrate();

        //lightSensorLineFollow = hardwareMap.lightSensor.get("lightSensorLineFollow");

    }

    /*
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
     */
    @Override
    public void init_loop() {


    }

    /*
     * Code to run ONCE when the driver hits PLAY
     */
    @Override
    public void start() {

        runtime.reset();
    }


    /*
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    @Override

    public void loop() {

        telemetry.addData("Status", "Running: " + runtime.toString());
        telemetry.addData("Status", "GyroHeading " + storedGyroHeading);
        telemetry.addData("Status", "Distance " + storedCMTraveled);
        telemetry.addData("Status", "motorPowerT " + motorPowerTarget);
        telemetry.addData("Status", "motorPowerL " + leftPower);
        telemetry.addData("Status", "motorPowerR " + rightPower);

        if (driveModeCurrent == driveModeLineFollow) {
            executeLineFollow();
        }

        if (driveModeCurrent == driveModeTurnByGyro) {
            // turn using the Gyro
            executeTurnByGyro();
        }

        if (driveModeCurrent == driveModeStraightByGyro) {
            //Drive straight by the using the Gyro
            executeDriveStraightByGyro();
        }

    }

    private void resetCounters() {
        //reset the move counters and encoders
        leftDriveMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightDriveMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftDriveMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightDriveMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        moveIsComplete = false;
        driveModeCurrent = driveModeStopped;
        leftDriveMotor.setPower(0);
        rightDriveMotor.setPower(0);
    }

    public double getCMTraveled() {
        // reads encoders, converts to centimeters, and averages them.
        double leftCM = Settings.Tics2CM(leftDriveMotor.getCurrentPosition());
        double rightCM = Settings.Tics2CM(rightDriveMotor.getCurrentPosition());
        storedCMTraveled = (Math.abs(leftCM) + Math.abs(rightCM)) / 2;
        return storedCMTraveled;
    }

    public void cmdDriveStraightByGyro(double motorPower, int heading, double distanceCM) {

        //drive by straight by using the gyro
        driveModeCurrent = driveModeStraightByGyro;
        motorPowerTarget = motorPower;
        headingTarget = heading;
        distance2TargetCM = distanceCM;
        resetCounters();
    }


    public void cmdTurnByGyro(double motorPower, int heading) {
        //drive by straight by using the gyro
        driveModeCurrent = driveModeTurnByGyro;
        motorPowerTarget = motorPower;
        headingTarget = heading;
        distance2TargetCM = 0;
        resetCounters();
        runtime.reset();
    }

    public void cmdDriveByLineFollow(double motorPower, int lightSensorValue, double distanceCM) {
        driveModeCurrent = driveModeLineFollow;
        motorPowerTarget = motorPower;
        headingTarget = 0;
        lightSensorTarget = lightSensorValue;
        distance2TargetCM = distanceCM;
        resetCounters();
    }

    public void cmdSweeper(double motorPower) {
        sweeperMotor.setPower(motorPower);
    }


    public boolean isMoveComplete() {
        return moveIsComplete;
    }

    public int getCurrentDriveMode() {
        return driveModeCurrent;
    }


    private void executeLineFollow() {
        // executes the logic and motor control for line follow

        double deltaLight = lightSensorTarget - lightSensorLineFollow.getLightDetected();

        leftPower = motorPowerTarget + (deltaLight * Settings.chassis_KPLineFollow);
        rightPower = motorPowerTarget - (deltaLight * Settings.chassis_KPLineFollow);

        if (leftPower < 0) {
            leftPower = 0;
        }
        if (rightPower < 0) {
            rightPower = 0;
        }

        if (leftPower > 1) {
            leftPower = 1;
        }
        if (rightPower > 1) {
            rightPower = 1;
        }

        leftDriveMotor.setPower(leftPower);
        rightDriveMotor.setPower(rightPower);

        if (getCMTraveled() >= distance2TargetCM) {
            moveIsComplete = true;
            driveModeCurrent = driveModeStopped;
            leftDriveMotor.setPower(0);
            rightDriveMotor.setPower(0);
        }
    }

    private void executeDriveStraightByGyro() {

        double deltaHeading = headingTarget - getGyroHeading();
        leftPower = motorPowerTarget + (deltaHeading * Settings.chassis_KPGyroStraight);
        rightPower = motorPowerTarget - (deltaHeading * Settings.chassis_KPGyroStraight);

        if (leftPower < 0) {
            leftPower = 0;
        }
        if (rightPower < 0) {
            rightPower = 0;
        }

        if (leftPower > 1) {
            leftPower = 1;
        }
        if (rightPower > 1) {
            rightPower = 1;
        }
        leftDriveMotor.setPower(leftPower);
        rightDriveMotor.setPower(rightPower);

        if (getCMTraveled() >= distance2TargetCM) {
            moveIsComplete = true;
            driveModeCurrent = driveModeStopped;
            leftDriveMotor.setPower(0);
            rightDriveMotor.setPower(0);
        }
    }


    private void executeTurnByGyro() {

        int loHeading = headingTarget - Settings.chassis_GyroHeadingTol;
        int hiHeading = headingTarget + Settings.chassis_GyroHeadingTol;
        int currHeading = getGyroHeading();

        if ((currHeading > loHeading) && (currHeading < hiHeading) ||
                runtime.milliseconds() > Settings.chassis_TurnMilliSeconds) {
            //We are there stop
            moveIsComplete = true;
            driveModeCurrent = driveModeStopped;
            leftDriveMotor.setPower(0);
            rightDriveMotor.setPower(0);
        }
    }

    public void setSweeperFront() {

        //Sets the drive mode to Sweeper first
        leftDriveMotor.setDirection(DcMotor.Direction.FORWARD);
        rightDriveMotor.setDirection(DcMotor.Direction.REVERSE);
        setGyroOffset(0);

    }

    public void setShooterFront() {

        //sets the drive mode to shooter first
        leftDriveMotor.setDirection(DcMotor.Direction.REVERSE);
        rightDriveMotor.setDirection(DcMotor.Direction.FORWARD);
        setGyroOffset(180);

    }

    public void setGyroOffset(int offSet) {
        //set offset to 180 for driving straight in reverse with heading = 0.
        gyroOffset = offSet;
    }


    public int getGyroHeading() {

        double retValue = gyroSensor.getHeading();
        //First take out full rotations
        retValue = (retValue % 360) * 360;

        if ((retValue > 180) && (retValue < 360)) {
            retValue = retValue - 360;
        }

        storedGyroHeading = (int) retValue + gyroOffset;
        return storedGyroHeading;
    }
}
