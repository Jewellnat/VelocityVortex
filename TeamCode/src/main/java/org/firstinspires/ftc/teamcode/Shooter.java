package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Hardware;


public class Shooter {

    //Tri state the trigger states...
    public static int triggerPositionReset = 0;
    public static int triggerPositionMoving = 1;
    public static int triggerPositionShoot = 2;

    private int triggerPosition = triggerPositionMoving;

    private DcMotor leftShootMotor = null;
    private DcMotor rightShootMotor = null;
    private Servo trigger = null;
    private int shooterSpeedRPM = 1;
    private ElapsedTime shotTimer = null;   //time for trigger reset
    private ElapsedTime speedControlerInitTimer = null;
    public double timeTriggerUp = 1.0;
    public double timeTriggerDown = 2.0;

    private double leftshooterSpeedRPM = 0;
    private double  rightshooterSpeedRPM = 0;

    private SpeedController shotspeedRightControler =  null;
    private SpeedController shotspeedLeftControler =  null;


    Shooter (){

      leftShootMotor = hardwareMap.dcMotor.get("leftShootMotor");
      rightShootMotor = hardwareMap.dcMotor.get("rightShootMotor");
      shootTrigger = hardwareMap.servo.get("shootTrigger");
      shotTimer = new ElapsedTime();

      speedControlerInitTimer = new ElapsedTime();
      //rightShootMotor.setDirection(XXX);
      //leftShootMotor.setDirection(XXX);

        leftShootMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        rightShootMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

      setMotorSpeed(0);
      resetTrigger();
      shotspeedRightControler = new SpeedController(0);
      shotspeedLeftControler = new SpeedController(0);

    }

    public void setMotorSpeed(int speedRPM){
        //set the shooter motor RPM

        if (shooterSpeedRPM != speedRPM) {
          shooterSpeedRPM = speedRPM;
          if (shooterSpeedRPM > Settings.shooterMotorMaxRPM) {
            shooterSpeedRPM = Settings.shooterMotorMaxRPM;
          }
          if (shooterSpeedRPM < 0) {
            shooterSpeedRPM = 0;
          }
          shotspeedRightControler.setTargetSpeedRPM(shooterSpeedRPM);
          shotspeedLeftControler.setTargetSpeedRPM(shooterSpeedRPM);
        }
    }

    private void pullTrigger(){
        //moves the trigger to the shoot position
          shootTrigger.setPosition(Settings.launch);
          triggerPosition = triggerPositionMoving;
    }

    private void resetTrigger(){
        //moves the trigger to the reset position
        shootTrigger.setPosition(Settings.reset);
        triggerPosition = triggerPositionMoving;
    }

    public boolean isTriggerReset() {
      return triggerPosition == triggerPositionReset;
    }

    public void shoot(){
      if (triggerPosition == triggerPositionReset){
        pullTrigger();
      }
    }

    public void loop(){
      // put this in the loop to make the shooter cycle

      if (isTriggerinPosition(Settings.reset, Settings.posTriggerTol)){
        triggerPosition = triggerPositionReset;
      } else if (isTriggerinPosition(Settings.launch, Settings.posTriggerTol)){
        triggerPosition = triggerPositionShoot;
        resetTrigger();
      } else{
        triggerPosition = triggerPositionMoving;
      }

      double currTime = speedControlerInitTimer.time ();
      if (currTime > 1.0) {
        currTime = 0;
        speedControlerInitTimer.reset();
        shotspeedRightControler.Init(currTime, rightShootMotor.getCurrentPosition());
        shotspeedLeftControler.Init(currTime, leftShootMotor.getCurrentPosition());
      }

      adjustSpeed(currTime);
    }

    private boolean isTriggerinPosition(double setPosition, double tol){
      //Is the trigger in the requested position ?  with some tollerance window

    boolean retValue = false;
        double currPos = shootTrigger.getCurrentPosition();
      if (currPos > (setPosition - tol) &&
           currPos < (setPosition + tol )) {
        retValue = true;
      }
      else{
        retValue = false;
      }
      return retValue;

    }

    private void adjustSpeed(currTime){
      //Read encoders and adjust speed as needed to bring it back to
      //set speed.

      //calculate  right motor adjustment
      rightMotorAdjustment = shotspeedRightControler.getMotorSpeed(currTime, rightShootMotor.getCurrentPosition());
      //calculate left motor adjustment
      leftMotorAdjustment = shotspeedRightControler.getMotorSpeed(currTime, leftShootMotor.getCurrentPosition());
      //act on right motor adjustment
      rightShootMotor.setPower(rightMotorAdjustment);
      //act on left motor adjustment
      leftShootMotor.setPower(leftMotorAdjustment);

    }
}
