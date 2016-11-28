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
import com.qualcomm.robotcore.hardware.Servo;
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

@Autonomous(name = "RedCorner2Ramp", group = "")  // @Autonomous(...) is the other common choice

public class RedCorner2Ramp extends OpMode {
    /* Declare OpMode members. */

    public static int stage_0PreStart =0;
    public static int stage_1DriveForward = 10;
    public static int stage_2DobuleShot =20;
    public static int stage_3TurnLeft = 30;
    public static int stage_4DriveStraight = 40;
    public static int stage_5TurnLeftAgain = 50;
    public static int stage_6ClimbRamp = 60;
    public static int stage_7Done = 70;

    private boolean isDone = false;

    Shoot doubleShooter = new Shoot();
    Chassis robotChassis = new Chassis();
    private ElapsedTime runtime = new ElapsedTime();
    private Servo beaconServo = null;

    int stage = stage_0PreStart;


    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init() {

        //colorSensor = hardwareMap.colorSensor.get("colorSensor");
        beaconServo = hardwareMap.servo.get("bacon");

        doubleShooter.hardwareMap = hardwareMap;
        doubleShooter.telemetry = telemetry;
        doubleShooter.init();

        robotChassis.hardwareMap = hardwareMap;
        robotChassis.telemetry = telemetry;
        robotChassis.init();

        telemetry.addData("Status", "Initialized");

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
        stage = stage_0PreStart;;
        robotChassis.start();
        }


    /*
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    @Override

    public void loop() {

        telemetry.addData("Status", "Running: " + runtime.toString());

        robotChassis.loop();

        if (stage == stage_0PreStart) {
            //Start Stage 1
            stage = stage_1DriveForward;
            robotChassis.cmdDriveStraightByGyro(.7, 0, 45);
        }

        if (stage == stage_1DriveForward){
            if (robotChassis.isMoveComplete()){
                //start Stage 2
                stage = stage_2DobuleShot;
                doubleShooter.start();
            }
        }

        if (stage == stage_2DobuleShot){
            doubleShooter.loop();
            if (doubleShooter.isDone()){
                //start Stage 3
                stage = stage_3TurnLeft;
                robotChassis.cmdTurnByGyro(.25,-45);
            }
        }


        if (stage == stage_3TurnLeft){
            //loop for a little bit longer on the dobule shot
            doubleShooter.loop();
            if (robotChassis.isMoveComplete()){
                //start stage 4
                stage = stage_4DriveStraight;
                robotChassis.cmdDriveStraightByGyro (.9, -45, 280);
            }
        }

        if (stage == stage_4DriveStraight){
            if (robotChassis.isMoveComplete()){
                //start Stage 5
                stage = stage_5TurnLeftAgain;
                robotChassis.cmdTurnByGyro(.4, -90);
            }
        }

        if (stage == stage_5TurnLeftAgain){
            if (robotChassis.isMoveComplete()){
                stage = stage_6ClimbRamp;
                robotChassis.cmdDriveStraightByGyro(.5, -90, 45);
            }
        }

        if (stage == stage_6ClimbRamp){
            if (robotChassis.isMoveComplete()){
                isDone = true;
                stage = stage_7Done;
            }
        }

    }
}
