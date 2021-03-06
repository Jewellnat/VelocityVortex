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
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
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

 //@Autonomous(name = "Red Corner", group = "")  // @Autonomous(...) is the other common choice
@Disabled
public class Auto_Red_Corner extends OpMode {
    /* Declare OpMode members. */

    private ElapsedTime runtime = new ElapsedTime();
    private int stage;
    private DcMotor leftMotor = null;
    private DcMotor rightMotor = null;
    private com.qualcomm.robotcore.hardware.ColorSensor ColorSensor = null;
    private GyroSensor gyro = null;
    private MRI_RangeFinder far = null;

    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init() {
        telemetry.addData("Status", "Initialized");
        stage = Settings.stageRedCorner1Forward;
        /* eg: Initialize the hardware variables. Note that the strings used here as parameters
         * to 'get' must correspond to the names assigned during the robot configuration
         * step (using the FTC Robot Controller app on the phone).
         */
        leftMotor = hardwareMap.dcMotor.get("leftMotor");
        rightMotor = hardwareMap.dcMotor.get("rightMotor");
        rightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        ColorSensor = hardwareMap.colorSensor.get("colorSensor");
        gyro = hardwareMap.gyroSensor.get("gyroSensor");
        far = new MRI_RangeFinder(hardwareMap.i2cDevice.get("far"));
        gyro.calibrate();

        // eg: Set the drive motor directions:
        // Reverse the motor that runs backwards when connected directly to the battery
        // leftMotor.setDirection(DcMotor.Direction.FORWARD); // Set to REVERSE if using AndyMark motors
        //  rightMotor.setDirection(DcMotor.Direction.REVERSE);// Set to FORWARD if using AndyMark motors
        // telemetry.addData("Status", "Initialized");
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
        double leftCM = Settings.Tics2CM(leftMotor.getCurrentPosition());
        double rightCM = Settings.Tics2CM(rightMotor.getCurrentPosition());
        double averageCM = (leftCM + rightCM) / 2;
        telemetry.addData("Status", "Running: " + runtime.toString());
        telemetry.addData("Status", "distance Average; " + averageCM);

        double stageThreeStartPos = 0;
        int heading = gyro.getHeading();
        int lightAlpha = ColorSensor.alpha();
        int distance = far.getDistanceCM();

        if (stage == Settings.stageRedCorner1Forward) {
            leftMotor.setPower(Settings.normalDriveSpeed);
            rightMotor.setPower(Settings.normalDriveSpeed);
            if (lightAlpha > Settings.redTapeLightVal) {
                leftMotor.setPower(0);
                rightMotor.setPower(0);
                stage = Settings.stageRedCorner2TurnLeft;
            }
        }

        if (stage == Settings.stageRedCorner2TurnLeft) {
            leftMotor.setPower(-1);
            rightMotor.setPower(1);
             if (heading > Settings.RedTapeAngle){
                 rightMotor.setPower(0);
                 leftMotor.setPower(0);
                 stageThreeStartPos = averageCM;
                 stage =Settings.stageRedCorner3LineFollow;
             }
        }
        if (stage == Settings.stageRedCorner3LineFollow){
           if (lightAlpha > Settings.redTapeLightVal) {
               leftMotor.setPower(.05);
                rightMotor.setPower(1);
            } else {
                leftMotor.setPower(1);
                rightMotor.setPower(.05);
            }
            if ((averageCM - stageThreeStartPos)< Settings.redTapeDistance){
               leftMotor.setPower(0);
               rightMotor.setPower(0);
                stage = Settings.stageRedCorner4AlignBucket;
            }

//            if (distance<Settings.redTapeDistance){
//                leftMotor.setPower(0);
//                rightMotor.setPower(0);
//            }


        }
        if (stage == Settings.stageRedCorner4AlignBucket){
            leftMotor.setPower(-.5);
            rightMotor.setPower(.5);
            if (heading > Settings.bucketAngle){
                rightMotor.setPower(0);
                leftMotor.setPower(0);
                stage = 5;
            }
        }

    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
    }

}