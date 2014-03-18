/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStationLCD;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;

/**
 *
 * @author Quelvin & Giovanni
 */

public class Arm {

	//*** Variables ***
    static double previous_error = 0;
    static double integral = 0 ;
    static double error;
    static double setpoint = 0;
    static double actual_position;
    static double encoder_position;
    static double dt = 0.005;
    static double derivative;
    static double output;
    static double Kp = 0.07;//0.1;
    static double Ki = 0;
    static double Kd = 0;
    static double kFactorEncoder = 2.98;
    
    static int nCounter40 = 0;
    static boolean isGoTo40Auto = false;
        
    static boolean StatusBtn2 = false; //-- Subtracts
    static boolean StatusBtn4 = false; //-- Adds
    
    static DigitalInput IsUp = new DigitalInput(5);

    static int nCounterZero = 0;
    static int nCounter = 0;
    static boolean isGoToZero = false;
    static boolean isGoTo100 = false;
    
    public static void PositionArm(int Position) 
    {
        setpoint = Position;
    }
    
    public static void SendToZero() 
    {
        isGoTo100 = false;
        isGoToZero = true;
    }
    
    public static void SendTo100() 
    {
        isGoToZero = false;
        isGoTo100 = true;
    }
    
    public static void SendTo40Auto() 
    {
        isGoTo40Auto = true;
    }
    
    public static void ResetAuto() 
    {
    	//*** Autonomous variables RESET ***
        setpoint = 0;
        nCounter40 = 0;
        isGoTo40Auto = false;
        isGoTo100 = false;
        isGoToZero = false;
    }
    
    public static void EngineAuto(Encoder enc, DriverStationLCD LCD, RobotDrive BMotors) 
    {   
    	//*** Autonomous Shooting Position ***
        if(isGoTo40Auto)
        {
            switch(nCounter40)
            {
                case 0:
                    setpoint = 15;
                    break;
                case 30:
                    setpoint = 30;
                    break;
                case 70:
                    setpoint = 43;//42;
                    isGoTo40Auto = false;
                    break;
            }
            
            nCounter40 = nCounter40+1;
        }
        else
        {
            nCounter40 = 0;
        }
        
        
        //================================================================
        //*** The Autonomus PID engine ***
        actual_position = enc.getDistance() / kFactorEncoder;
        
        error = setpoint - actual_position;
        integral = integral + (error*dt);
        derivative = (error - previous_error)/dt;
        output = (((Kp*error) + (Ki*integral) + (Kd*derivative)) / -2.5);
        previous_error = error;
        
        if(Math.abs(output)<0.35)
        {
            Kp = 0.8;
        }
        else
        {
            Kp = 0.1;
        }
        
        output = (((Kp*error) + (Ki*integral) + (Kd*derivative)) / -2.5);
        previous_error = error;

        BMotors.tankDrive(output, output);
        //================================================================
    }    
    
    public static void Engine(Joystick ctrl, Encoder enc, DriverStationLCD LCD, RobotDrive BMotors) 
    {
        if(isGoTo100)
        {
            switch(nCounter)
            {
                case 0:
                    setpoint = 70;
                    break;
                case 50:
                    setpoint = 85;
                    break;
                case 80:
                    setpoint = 100;
                    isGoTo100 = false;
                    break;
            }
            
            nCounter = nCounter+1;
        }
        else
        {
            nCounter = 0;
        }
        
        if(isGoToZero)
        {
            switch(nCounterZero)
            {
                case 0:
                    setpoint = 30;
                    break;
                case 50:
                    setpoint = 15;
                    break;
                case 80:
                    setpoint = 0;
                    isGoToZero = false;
                    break;
            }
            
            nCounterZero = nCounterZero+1;
        }
        else
        {
            nCounterZero = 0;
        }
        //==============================================================================
        //*** The Teleop PID engine ***
        encoder_position = enc.getDistance();
        actual_position = enc.getDistance() / kFactorEncoder;
        
        error = setpoint - actual_position;
        integral = integral + (error*dt);
        derivative = (error - previous_error)/dt;
        output = (((Kp*error) + (Ki*integral) + (Kd*derivative)) / -2.5);
        previous_error = error;
        
        //*** Displays the following in the Driver Station Message Box ***
        LCD.println(DriverStationLCD.Line.kUser1, 1, "EncPos : " + encoder_position);
        LCD.updateLCD();
        LCD.println(DriverStationLCD.Line.kUser2, 1, "SetPoint  : " + setpoint);
        LCD.updateLCD();
        LCD.println(DriverStationLCD.Line.kUser3, 1, "ActualPos : " + actual_position);
        LCD.updateLCD();
        
        //*** This is used because any value lower than 0.29 doesnt move the arm ***
        if(Math.abs(output)<0.35)
        {
            Kp = 0.8;
        }
        else
        {
            Kp = 0.1;
        }
        
        output = (((Kp*error) + (Ki*integral) + (Kd*derivative)) / -2.5);
        previous_error = error;

        LCD.updateLCD();
        
        BMotors.tankDrive(output, output);
        //==============================================================================    
        
        //*** Buttons ***
        if(ctrl.getRawButton(1))
        {
            PositionArm(39);//46);
        }
        
        if(ctrl.getRawButton(3))
        {
            PositionArm(38);//42); 
        }
            
        if(ctrl.getRawButton(9))
        {
            isGoToZero = false;
            isGoTo100 = true;
        }
            
        if(ctrl.getRawButton(5))
        {
            isGoTo100 = false;
            isGoToZero = true;
        }

        if(ctrl.getRawButton(10))
        {
            //Used in the Shooting class && is used to catch balls
        }
        
        //================================
        if(ctrl.getRawButton(2))
        {   
        	//*** Makes the Arm go up ***
            if(StatusBtn4 == false)
            {
                setpoint += 1;
            }
            StatusBtn4 = true;
            
            if(setpoint > 100){
                setpoint = 100;
            }
        }
        else
        {
            StatusBtn4 = false;
        }
        //================================
        if(ctrl.getRawButton(4))
        {
        	//*** Makes the arm go up ***
            if(StatusBtn2 == false)
            {
                setpoint -= 1;
            }
            StatusBtn2 = true;
            
            if(setpoint < 0){
                setpoint = 0;
            }
        }
        else
        {
            StatusBtn2 = false;
        }
        //================================
    }    
}
