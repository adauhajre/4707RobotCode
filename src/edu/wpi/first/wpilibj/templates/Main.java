//HEY!!
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DriverStationLCD;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.RobotDrive;

/**
 *
 * @author Quelvin & Giovanni
 */

public class Main extends IterativeRobot {
   //*** Varialbes ***
    Joystick GamePadDriver = new Joystick(1);
    Joystick GamePadOperator = new Joystick(2);
    
    Compressor compressor = new Compressor(1, 1);

    DriverStationLCD ds;

    Encoder ArmEnc = new Encoder(13, 14, true, edu.wpi.first.wpilibj.CounterBase.EncodingType.k4X);
    Relay RelayMotorArm = new Relay(5);
    
    RobotDrive motors = new RobotDrive(1, 2);
    RobotDrive AMotors = new RobotDrive(9, 10);

    boolean isAutoOk = false ;
    boolean processShoot = true;
    boolean isLoading = false;
    
    
    public void robotInit() {
    	//*** The first thing the robot does when it turn on ***

        //===========================
        //*** FIRST Security (Required), Checks every 0.5 secs ***
        getWatchdog().setExpiration(0.5);
        //===========================
        
        ds = DriverStationLCD.getInstance();
        ds.clear();
        
        //*** Starting Parameters for the Encnder ***
        ArmEnc.setDistancePerPulse(1);
        ArmEnc.start();
        ArmEnc.reset();
        
        compressor.start();
}
    
    //============================
    public void  autonomousInit()
    {
    	//*** The first thing the robot does when it goes into Autonomous ***

    	//*** Encoder Reset (is done just to be sure) ***
        ArmEnc.reset();

    	//*** Automous Distance Covered RESET ***
        AutoNoEnc.Reset();
        
        //*** Automous Arm Values RESET ***
        Arm.ResetAuto();
        
        //*** Automous Low Gear Change (So it wont crash against the wall) ***
        Transmission.gearChangeLow(ds);
        
        isAutoOk = false ;
        processShoot = true;
        isLoading = false;
        
        //*** Automous Shoot Values RESET ***
        Shooting.ShootAutoReset();
    }
    //============================
    
    public void autonomousPeriodic() {
        //===========================
        //*** FIRST Security (Required) ***
        getWatchdog().feed();
        //===========================

        //*** Checks if Autonomus Distance is covered ***
        isAutoOk = AutoNoEnc.mRun(motors);

        //*** Changes Transmission to low just make sure ***
        Transmission.gearChangeLow(ds);
        
        if(isAutoOk)
        {
        	//*** While the robot is going forward it will go to shooting position ***
            if(processShoot)
            {
            	//*** Shoots ***
                processShoot = Shooting.ShootAuto(RelayMotorArm);
                Arm.SendTo40Auto();
            }
            else
            {
               // Arm.SendTo100();
            }

            Arm.EngineAuto(ArmEnc, ds, AMotors);
        }
        
    }

    public void  teleopInit()
    {
    	//*** The first thing the robot does when it goes into Teleop (Repeats every time ENABLE is pressed, has to be disabled first) ***

        //Arm.SendTo100();
        Arm.SendToZero();
        Transmission.gearChangeLow(ds);
    }
            
    boolean isCatching = false;
    
    public void teleopPeriodic() {
        //===========================
        //*** FIRST Security (Required) ***
        getWatchdog().feed();
        //===========================
        
        //*** Goes into high speed ***
        Transmission.gearChange(GamePadDriver);

        //*** Uses the Ramp ***
        Ramp.SoftStarter(GamePadDriver, motors);
        
        isLoading = Shooting.Shoot(GamePadOperator, ArmEnc, RelayMotorArm);
        isCatching = Shooting.Catcher(GamePadOperator, ArmEnc, RelayMotorArm);
        
        //*** If the robot is not shooting or catching you can pick up, spit out, catch and adjust the shooting angle ***
        if(!isLoading && !isCatching)
        {
            Loader.Load(GamePadOperator, RelayMotorArm);
        }
        
        Arm.Engine(GamePadOperator, ArmEnc, ds, AMotors);
        
        //*** Always neccesary so the Driver Station Message Box is up to date ***
        ds.updateLCD();
    }
}
