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
import com.qualcomm.robotcore.util.ElapsedTime;


@Autonomous(name = "shoot only", group = "")  // @Autonomous(...) is the other common choice

public class shoot extends OpMode {
    /* Declare OpMode members. */
    private ElapsedTime runtime = new ElapsedTime();
    private Shooter ballShooter = null;


    public static int spinupDelay = 0;
    public static int firstShot = 1;
    public static int resetDelay = 2;
    public static int secondShot = 3;
    public static int spinDownDelay = 4;
    public static int turnOffShooters = 5;

    private boolean done = false;
    int stage;

    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init() {
        telemetry.addData("Status", "Initialized");
        Shooter ballShooter = new Shooter();
        ballShooter.hardwareMap = hardwareMap;
        ballShooter.init();
    }

    /*
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
     */
    @Override
    public void init_loop() {
        ballShooter.init_loop();
    }

    /*
     * Code to run ONCE when the driver hits PLAY
     */
    @Override
    public void start() {

        ballShooter.start();
        ballShooter.setMotorSpeed(4800);
        runtime.reset();
        stage = spinupDelay;
    }

    /*
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    @Override
    public void loop() {

        telemetry.addData("Status", "Running:" +  ballShooter.getMotorSpeed());
        ballShooter.loop();

        if (stage == spinupDelay) {
            if (runtime.time() > 3.0) {
                stage = firstShot;
            }
        }

        if (stage == firstShot) {
            ballShooter.shoot();
            stage = resetDelay;
            runtime.reset();
        }

        if (stage == resetDelay) {
            if (runtime.time() > 3.0) {
                stage = secondShot;
            }
        }

        if (stage == secondShot) {
            ballShooter.shoot();
            stage = spinDownDelay;
            runtime.reset();
        }

        if (stage == spinDownDelay) {
            if (runtime.time() > 2.0) {
                stage = secondShot;
            }
        }

        if (stage == turnOffShooters) {
            ballShooter.setMotorSpeed(0);
            done = true;
        }
    }

    public boolean isDone() {
        return done;
    }
}