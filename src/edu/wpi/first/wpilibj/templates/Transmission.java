/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.DriverStationLCD;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Solenoid;

/**
 *
 * @author Quelvin & Giovanni
 */

public class Transmission {
    private static Solenoid s1 = new Solenoid(7);
    private static Solenoid s2 = new Solenoid(8);
    
    //*** Toggle Button ***
    private static boolean btnStatus = false;
    private static boolean preButton = false;
    private static boolean curButton = false;
    //********************
    
    //*** 13 may 2014 ***
    public static void gearChangeLow(DriverStationLCD LCD) 
    {
    	//*** Used to change Gear to low ***
        if(s1.get())
        {
            s1.set(false);
            s2.set(true);
        }
    }
    //*** 13 may 2014 ***
    
    public static void gearChange(Joystick ctrl) 
    {            
    	//*** Used to change Gears ***
        preButton = curButton;
        curButton = ctrl.getRawButton(5); //6

        if(curButton && !preButton)
        {
            btnStatus = !btnStatus;
        }

        //*** Toggle Button ***
        if(btnStatus)
        {
            s1.set(true);
            s2.set(!s1.get());
        }
        else
        {
            s1.set(false);
            s2.set(!s1.get());
        }
    }
}
