/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;

/**
 *
 * @author Quelvin & Giovanni
 */

public class Ramp {
    //private static RobotDrive motores = new RobotDrive(1, 2);

    private static double InitR = 0;
    private static double InitL = 0;
    private static double kRampR = 0.01;
    private static double kRampL = 0.01;
    private static double leftY = 0;
    private static double rightY = 0;
    
    public static void SoftStarter(Joystick GamePad, RobotDrive motores) 
    {
        leftY = GamePad.getRawAxis(2);
        rightY = GamePad.getRawAxis(4);

        //******************************
        //*** Right ***
        if(Math.abs(rightY)<0.009)
        {
            rightY = 0;
        }

        if(rightY>0)
        {    
            if(rightY > InitR)
            {
                //*** Right Ramp Forward ***
                {
                    rightY = GamePad.getRawAxis(4);
                    if(InitR<0.45)
                    {
                        kRampR = 0.05;
                    }
                    else
                    {
                        kRampR = 0.01;
                    }

                    InitR += kRampR;
                }
            }
            if(rightY < InitR)
            {
                InitR = rightY;
            }
        }
        else
        {
            if(rightY < InitR)
            {
                //*** Right Ramp Reverse ***
                {
                    rightY = GamePad.getRawAxis(4);
                    if(Math.abs(InitR)<0.45)
                    {
                        kRampR = 0.05;
                    }
                    else
                    {
                        kRampR = 0.01;
                    }

                    InitR -= kRampR;
                }
            }
            if(rightY > InitR)
            {
                InitR = rightY;
            }
        }

        //*************************
        //*** Left
        if(Math.abs(leftY)<0.009)
        {
            leftY = 0;
        }

        if(leftY>0)
        {    
            if(leftY > InitL)
            {
                //*** Left Ramp Forward ***
                {
                    leftY = GamePad.getRawAxis(2);
                    if(InitL<0.45)
                    {
                        kRampL = 0.05;
                    }
                    else
                    {
                        kRampL = 0.01;
                    }

                    InitL += kRampL;
                }
            }
            if(leftY < InitL)
            {
                InitL = leftY;
            }
        }
        else
        {
            if(leftY < InitL)
            {
                //*** Left Ramp Reverse ***
                {
                    leftY = GamePad.getRawAxis(2);
                    if(Math.abs(InitL)<0.45)
                    {
                        kRampL = 0.05;
                    }
                    else
                    {
                        kRampL = 0.01;
                    }

                    InitL -= kRampL;
                }
            }
            if(leftY > InitL)
            {
                InitL = leftY;
            }
        }
        //*************************

        motores.tankDrive(InitL, -InitR);
    }
}
