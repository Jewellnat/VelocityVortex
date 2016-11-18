package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;


public class Shooter extends OpMode {

    //Trigger position stages ...
    public static int triggerPositionReset = 0;
    public static int triggerPositionMoving2Shoot = 1;
    public static int triggerPositionShoot = 2;
    public static int triggerPositionMoving2Reset = 3;
    public double timeTriggerUp = 1.0;
    public double timeTriggerDown = 2.0;
    private int triggerPosition = triggerPositionMoving2Reset;
    private DcMotor leftShootMotor = null;
    private DcMotor rightShootMotor = null;
    private Servo shootTrigger = null;
    private double shooterPower = 1;
    private double shooterRPMsetPoint = 0;
    private ElapsedTime shotTimer = null;   //time for trigger reset
    private ElapsedTime speedControlerInitTimer = null;
    private double leftshooterSpeedRPM = 0;
    private double rightshooterSpeedRPM = 0;

    private int rightShooterCurrPos = 0;
    private int leftShooterCurrPos = 0;
    private int rightShooterDeltaPos = 0;
    private int leftShooterDeltaPos = 0;
    private int leftShooterPrevPos = 0;
    private int rightShooterPrevPos = 0;

    private SpeedController shotspeedRightControler = null;
    private SpeedController shotspeedLeftControler = null;

    public void init() {

        leftShootMotor = hardwareMap.dcMotor.get("leftShootMotor");
        rightShootMotor = hardwareMap.dcMotor.get("rightShootMotor");
        leftShootMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightShootMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftShootMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightShootMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        shootTrigger = hardwareMap.servo.get("trigger");
        speedControlerInitTimer = new ElapsedTime();
        shotTimer = new ElapsedTime();
        leftShootMotor.setDirection(DcMotor.Direction.REVERSE);
        leftShootMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        rightShootMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        shotspeedRightControler = new SpeedController(0);
        shotspeedLeftControler = new SpeedController(0);
        //telemetry.addData("Status", "Initialized");
    }

    public void start() {
        shotTimer.reset();
        speedControlerInitTimer.reset();
        resetTrigger();
        setMotorRPM(0);
    }

    public int getMotorRPM() {
        //return the measured motor speed
        return (int) (shotspeedLeftControler.getMeasuredRPM() +
                shotspeedRightControler.getMeasuredRPM()) / 2;
    }

    /*public void setMotorPower(double ShootMotorPower) {
        //set the shooter motor RPM

        if (shooterPower != ShootMotorPower) {
            shooterPower= ShootMotorPower;
            if ( shooterPower > 1) {
                shooterPower= 1;
            }
            if (shooterPower < 0) {
                shooterPower = 0;
            }
            leftShootMotor.setPower(shooterPower);
            rightShootMotor.setPower(shooterPower);
        }
    }
*/

    public void setMotorRPM(double shootMotorRPM) {

        if (shooterRPMsetPoint != shootMotorRPM) {
            shooterRPMsetPoint = shootMotorRPM;
            if (shooterRPMsetPoint > Settings.shooterMotorMaxRPM) {
                shooterRPMsetPoint = Settings.shooterMotorMaxRPM;
            }
            if (shooterRPMsetPoint < 0) {
                shooterRPMsetPoint = 0;
            }
            shotspeedRightControler.setTargetSpeedRPM(shooterRPMsetPoint);
            shotspeedLeftControler.setTargetSpeedRPM(shooterRPMsetPoint);
        }
    }


    private void pullTrigger() {
        //moves the trigger to the shoot position
        shootTrigger.setPosition(Settings.launch);
        triggerPosition = triggerPositionMoving2Shoot;
        shotTimer.reset();
    }

    private void resetTrigger() {
        //moves the trigger to the reset position
        shootTrigger.setPosition(Settings.reset);
        triggerPosition = triggerPositionMoving2Reset;
    }

    public boolean isTriggerReset() {
        return triggerPosition == triggerPositionReset;
    }

    public void shoot() {
        if (triggerPosition == triggerPositionReset) {
            pullTrigger();
        }
    }

    public void loop() {
        // put this in the parent.loop() to make the shooter cycle

        if ((shotTimer.milliseconds() > 1500) && (triggerPosition == triggerPositionMoving2Shoot)) {
            shotTimer.reset();
            resetTrigger();
        }

        if ((shotTimer.milliseconds() > 1500) && (triggerPosition == triggerPositionMoving2Reset)) {
            shotTimer.reset();
            triggerPosition = triggerPositionReset;
        }

        telemetry.addData("RightShooterCurrPos = ", rightShooterCurrPos);
        telemetry.addData("RightShooterDeltaPos = ", rightShooterDeltaPos);
        telemetry.addData("LeftShooterCurrPos =", leftShooterCurrPos);
        telemetry.addData("LeftShooterDeltaPos =", leftShooterDeltaPos);

        if (speedControlerInitTimer.milliseconds() > 20.0) {
            adjustSpeed(speedControlerInitTimer.milliseconds());
            speedControlerInitTimer.reset();
        }
    }


    private void adjustSpeed(double deltaTime) {
        //Read encoders and adjust speed as needed to bring it back to set speed.
        //calculate  right motor adjustment
        rightShooterCurrPos = Math.abs(rightShootMotor.getCurrentPosition());
        leftShooterCurrPos = Math.abs(leftShootMotor.getCurrentPosition());

        rightShooterDeltaPos = Math.abs(rightShooterCurrPos - rightShooterPrevPos);
        leftShooterDeltaPos = Math.abs(leftShooterCurrPos - leftShooterPrevPos);

        rightShooterPrevPos = rightShooterCurrPos;
        leftShooterPrevPos = leftShooterCurrPos;

        double rightMotorAdjustment = shotspeedRightControler.getMotorPower(deltaTime, rightShooterDeltaPos);
        //calculate left motor adjustment
        double leftMotorAdjustment = shotspeedLeftControler.getMotorPower(deltaTime, leftShooterDeltaPos);

        //act on right motor adjustment
        rightShootMotor.setPower(rightMotorAdjustment);
        //act on left motor adjustment
        leftShootMotor.setPower(leftMotorAdjustment);
    }
}
