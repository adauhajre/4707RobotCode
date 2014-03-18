/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.RobotDrive;

/**
 *
 * @author Quelvin & Giovanni
 */

public class AutoNoEnc {
    private static int nCounter = 0;
    
    public static void Reset() 
    {
        nCounter = 0;
    }
    
    public static boolean mRun(RobotDrive motors) 
    {
    	//*** Is used to go forward in Autonomous ***
        nCounter = nCounter+1;
        
        if(nCounter<80)
        {
            motors.tankDrive(-0.75, 0.75);
        }
        else
        {
            //*** Impedes Overflow ***
            nCounter = 85;
            
            return true;
        }
        
        return false;
    }
}
