/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Relay;

/**
 *
 * @author Quelvin & Giovanni
 */

public class Loader {
    //private static  Relay ArmMotor = new Relay(5);
    
    public static void LoadAuto(Relay ArmMotor, boolean Status) 
    {          
        if(Status)
        {
            //*** Picks up Ball in Autonomous ***
            ArmMotor.set(Relay.Value.kReverse);
        }
        else
        {
            ArmMotor.set(Relay.Value.kOff);
        }
    }
    
    public static void Load(Joystick ctrl, Relay ArmMotor) 
    {          
        if(ctrl.getRawAxis(6) < -0.25)
        {
            //*** Button to pick up ball ***
            ArmMotor.set(Relay.Value.kForward);
            
            return;
        }
        
        if(ctrl.getRawAxis(6) > 0.25)
        {
            //*** Button to spit out ball ***
            ArmMotor.set(Relay.Value.kReverse);
            
            return;
        }
        
        ArmMotor.set(Relay.Value.kOff);
    }
}
