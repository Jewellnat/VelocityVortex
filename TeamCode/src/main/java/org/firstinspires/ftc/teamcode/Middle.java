package org.firstinspires.ftc.teamcode;

/**
 * Created by mg15 on 11/1/2016.
 */
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.Servo;

@Autonomous(name="Midddle", group="")  // @Autonomous(...) is the other common choice

public class Middle extends OpMode
{
    /* Declare OpMode members. */
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor leftShootMotor = null;
    private DcMotor rightShootMotor = null;
    private DcMotor leftDriveMotor = null;
    private DcMotor rightDriveMotor = null;
    private Servo shootTrigger = null;


    int Stage;



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
    public void start() {runtime.reset();
        Stage = Settings.stagemiddle1shoot;
    }


    /*
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    @Override
    public void loop() {
        telemetry.addData("Status", "Running: " + runtime.toString());
        telemetry.addData("Status", "Stage: " + Stage);
        if (Stage == Settings.stagemiddle1shoot) {
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
                Stage = Settings.stageDriveForwardmiddle2;
                leftDriveMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                rightDriveMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                leftDriveMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                rightDriveMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            }

        }

        if (Stage == Settings.stageDriveForwardmiddle2) {
//
            leftDriveMotor.setPower(Settings.normalDriveSpeed);
            rightDriveMotor.setPower(Settings.normalDriveSpeed);

            double leftcm = Settings.Tics2CM(leftDriveMotor.getCurrentPosition());
            double rightcm = Settings.Tics2CM(rightDriveMotor.getCurrentPosition());
            double averagecm = (leftcm + rightcm) / 2;
            if (averagecm > Settings.middleDriveDistance) {
                Stage = Settings.stageStoppingmiddle3;

            }
        }
        if (Stage == Settings.stageStoppingmiddle3) {
            leftDriveMotor.setPower(0);
            rightDriveMotor.setPower(0);
        }


    }



    @Override
    public void stop() {
    }

}

