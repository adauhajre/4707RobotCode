/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Solenoid;

/**
 *
 * @author Quelvin & Giovanni
 */

public class Shooting {
    private static final Solenoid FetcherSolDown = new Solenoid(2); 
    private static final Solenoid FetcherSolUp = new Solenoid(1); 
    private static final Solenoid ReleaseSolDown = new Solenoid(3); 
    private static final Solenoid ReleaseSolUp = new Solenoid(4); 
    private static final Solenoid ArmSolUp = new Solenoid(6); 
    private static final Solenoid ArmSolDown = new Solenoid(5); 
    
    private static boolean isUp = false;
    private static int nCounter = 0;
    private static int nCounterAuto = 0;

    static double kFactorEncoder = 2.8; //Same as Arm class
    static double LimitMayor = 60;
    static double LimitMinor = 10;
    static double NormalEncValue = 0;
    static boolean isCatching = false;
    
    public static boolean Catcher(Joystick ctrl, Encoder enc, Relay ArmMotor) 
    {
        if(!isUp)
        {
            if(ctrl.getRawButton(10))
            {
            	//*** Used to Catch Balls ***
                ArmMotor.set(Relay.Value.kReverse);
                
                ArmSolUp.set(false);
                ArmSolDown.set(!ArmSolUp.get());
                
                isCatching = true;
            }
            else
            {
                ArmMotor.set(Relay.Value.kOff);
                
                ArmSolUp.set(true);
                ArmSolDown.set(!ArmSolUp.get());
                isCatching = false;
            }
        }
        
        return isCatching;
    }            
    
    public static boolean Shoot(Joystick ctrl, Encoder enc, Relay ArmMotor) 
    {            
        //*** Whole shooting process ***
        if(ctrl.getRawButton(8))
        {
            NormalEncValue = enc.getDistance() / kFactorEncoder;
            if(NormalEncValue >= LimitMinor
               &&
               NormalEncValue <= LimitMayor)
            {
                isUp = true;
            }
        }
        
        if(isUp)
        {
            nCounter++;

            switch(nCounter)
            {
            	//Shooting Process
                case 1:
                	//*** Ball Recovery (turns motor on) ***
                    ArmMotor.set(Relay.Value.kReverse);
                    break;
                case 20://30:
                    //*** Ball Recovery (turns motor off) && Opens Loader ***
                    ArmMotor.set(Relay.Value.kOff);
                    ArmSolUp.set(false);
                    ArmSolDown.set(!ArmSolUp.get());
                    break;
                case 65://90:
                    //*** Shoots ***
                    ReleaseSolUp.set(true);
                    ReleaseSolDown.set(false);
                    break;
                case 90://110:
                	//*** Shoots (Release Piston Retracts) ***
                    ReleaseSolUp.set(false);
                    ReleaseSolDown.set(true);
                    break;
                case 120://150:
                    //*** Retriver picks up the hitter ***
                    FetcherSolUp.set(true);
                    FetcherSolDown.set(false);
                    break;
                case 160://200:
                	//*** Retriver goes back ***
                    FetcherSolUp.set(false);
                    FetcherSolDown.set(true);
                    break;
                case 200://250:
                	//Closes Loader
                    ArmSolUp.set(true);
                    ArmSolDown.set(!ArmSolUp.get());
                    break;
                case 215://270:
                    //RESET
                    //Brazo.SendTo100();
                    nCounter = 0;
                    isUp = false;
                    break;
            }
        }
        
        return isUp;
    }
    
    public static void ShootAutoReset() 
    {
        nCounterAuto = 0;
    }            
    
    public static boolean ShootAuto(Relay ArmMotor) 
    {          
    //Autonous shooting  
            nCounterAuto++;
            
            if(nCounterAuto>371)//331)
            {
            	//Impedes Overflow
                nCounterAuto = 371;//331; 
                return false;
            }

            switch(nCounterAuto)
            {
                case 1:
                	//*** Turns on the recovery motor ***
                    ArmMotor.set(Relay.Value.kReverse);
                    break;
                case 140: //100:
                    //*** Turns off the recovery motor ***
                    ArmMotor.set(Relay.Value.kOff);
                    //*** Opens Loader ***
                    ArmSolUp.set(false);
                    ArmSolDown.set(!ArmSolUp.get());
                    break;
                case 220://180:
                    //*** Shoots ***
                    ReleaseSolUp.set(true);
                    ReleaseSolDown.set(false);
                    break;
                case 240://200:
                	//*** Shoots (Reteracts) ***
                    ReleaseSolUp.set(false);
                    ReleaseSolDown.set(true);
                    break;
                case 270://230:
                    //*** Fecther retreives ball ***
                    FetcherSolUp.set(true);
                    FetcherSolDown.set(false);
                    break;
                case 320://280:
                	//*** Fecther retreives ball (Retracts) ***
                    FetcherSolUp.set(false);
                    FetcherSolDown.set(true);
                    break;
                case 370://330:
                	//Closes Arm
                    ArmSolUp.set(true);
                    ArmSolDown.set(!ArmSolUp.get());
                    break;
                    /*
                case 500: //470:
                    //*** Sends arm Up ***
                    Brazo.SendToZero();
                    //*** Sends arms down ***
                    //Brazo.SendTo100();
                    break;
                    */
            }
            
            return true;
    }
    
}
