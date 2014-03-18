/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.DriverStation;

/**
 *
 * @author Quelvin & Giovanni
 */

public class BatteryVoltage {
    public static double GetVoltage() 
    {
    	//*** Gives Battery Voltage ***
        return DriverStation.getInstance().getBatteryVoltage();
    }
}
