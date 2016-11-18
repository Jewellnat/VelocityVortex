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
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * This file contains an example of an iterative (Non-Linear) "OpMode".
 * An OpMode is a 'program' that runs in either the autonomous or the teleop period of an FTC match.
 * The names of OpModes appear on the menu of the FTC Driver Station.
 * When an selection is made from the menu, the corresponding OpMode
 * class is instantiated on the Robot Controller and executed.
 * <p/>
 * This particular OpMode just executes a basic Tank Drive Teleop for a PushBot
 * It includes all the skeletal structure that all iterative OpModes contain.
 * <p/>
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@Autonomous(name = "Corner", group = "")  // @Autonomous(...) is the other common choice

public class Corner extends OpMode {
    int stage;
    /* Declare OpMode members. */
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor leftShootMotor = null;
    private DcMotor rightShootMotor = null;
    private DcMotor leftDriveMotor = null;
    private DcMotor rightDriveMotor = null;
    private Servo shootTrigger = null;
    // private GyroSensor gyroSensor;
    private DcMotor sweeperMotor = null;
    // private ColorSensor colorSensor;
    private Servo beaconServo = null;

    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init() {
        telemetry.addData("Status", "Initialized");

        //leftShootMotor = hardwareMap.dcMotor.get("leftShootMotor");
        //
        // rightShootMotor = hardwareMap.dcMotor.get("rightShootMotor");
        leftDriveMotor = hardwareMap.dcMotor.get("leftDriveMotor");
        rightDriveMotor = hardwareMap.dcMotor.get("rightDriveMotor");
        leftDriveMotor.setDirection(DcMotor.Direction.REVERSE);
        leftShootMotor = hardwareMap.dcMotor.get("leftShootMotor");
        rightShootMotor = hardwareMap.dcMotor.get("rightShootMotor");
        leftShootMotor.setDirection(DcMotor.Direction.REVERSE);
        leftShootMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        rightShootMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        shootTrigger = hardwareMap.servo.get("trigger");
        // gyroSensor = hardwareMap.gyroSensor.get("gyroSensor");
        sweeperMotor = hardwareMap.dcMotor.get("sweeperMotor");
        // colorSensor = hardwareMap.colorSensor.get("colorSensor");
        beaconServo = hardwareMap.servo.get("bacon");
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
        shootTrigger.setPosition(Settings.reset);
        runtime.reset();
        stage = Settings.stagecorner1shoot;
    }


    /*
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    @Override

    public void loop() {
        telemetry.addData("Status", "Running: " + runtime.toString());

        if (stage == Settings.stagecorner1shoot) {
            leftShootMotor.setPower(Settings.spinnerShooterAuto);
            rightShootMotor.setPower(Settings.spinnerShooterAuto);
            if (runtime.seconds() > Settings.firstLaunch && runtime.seconds() < Settings.firstReset) {
                shootTrigger.setPosition(Settings.launch);
            }
            if (runtime.seconds() > Settings.firstReset && runtime.seconds() < Settings.secondLaunch) {
                shootTrigger.setPosition(Settings.reset);
            }
            if (runtime.seconds() > Settings.secondLaunch && runtime.seconds() < Settings.secondReset) {
                shootTrigger.setPosition(Settings.launch);
            }
            if (runtime.seconds() > Settings.secondReset && runtime.seconds() < Settings.turnOffShooter) {
                shootTrigger.setPosition(Settings.reset);
            }
            if (runtime.seconds() > Settings.turnOffShooter) {
                leftShootMotor.setPower(0);
                rightShootMotor.setPower(0);
                leftDriveMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                rightDriveMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                leftDriveMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                rightDriveMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                stage = Settings.stage2Charge;

            }
        }
        if (stage == Settings.stage2Charge) {

            leftDriveMotor.setPower(Settings.driveSpeed);
            rightDriveMotor.setPower(Settings.driveSpeed);

            double leftcm = Settings.Tics2CM(leftDriveMotor.getCurrentPosition());
            double rightcm = Settings.Tics2CM(rightDriveMotor.getCurrentPosition());
            double averagecm = (leftcm + rightcm) / 2;
            if (averagecm > Settings.cornerDriveDistance) {
                stage = Settings.stage3turn180;

            }
        }
        if (stage == Settings.stage3turn180) {
            leftDriveMotor.setPower(0);
            rightDriveMotor.setPower(0);
        }


    }


}
