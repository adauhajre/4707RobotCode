/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;

/**
 *
 * @author Quelvin
 */
public class Brazo_AD {
    
    static RobotDrive BMotors = new RobotDrive(9, 10);
    
    static double previous_error = 0;
    static double integral = 0 ;
    static double error;
    static double setpoint = 90;//50;
    static double actual_position;
    static double dt = 0.005;
    static double derivative;
    static double output;
    static double Kp = 0.1;
    static double Ki = 0;
    static double Kd = 0;
    static double kFactorEncoder = 2.8;
    
    static boolean StatusBtn2 = false; //-- Resta 
    static boolean StatusBtn4 = false; //-- Suma
    
    public static void Engine(Joystick ctrl, Encoder enc) 
    {     
        actual_position = enc.getDistance() / kFactorEncoder;
        
        error = setpoint - actual_position;
        integral = integral + (error*dt);
        derivative = (error - previous_error)/dt;
        output = (((Kp*error) + (Ki*integral) + (Kd*derivative)) / -2.5) * 0.75;
        previous_error = error;

        //System.out.println("output : " + output);
        //System.out.println("error : " + error);
        System.out.println("actual_position : " + actual_position);
        System.out.println("getDistance : " + enc.getDistance());
        
        BMotors.tankDrive(output, output);
        
        if(ctrl.getRawButton(1))
        {
            setpoint = 65;
        }
        
        if(ctrl.getRawButton(3))
        {
            setpoint = 50;
        }
        
        //================================
        if(ctrl.getRawButton(2))
        {   
            if(StatusBtn2 == false)
            {
                setpoint -= 5;
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
        if(ctrl.getRawButton(4))
        {
            if(StatusBtn4 == false)
            {
                setpoint += 5;
            }
            StatusBtn4 = true;
            
            if(setpoint > 90){
                setpoint = 90;
            }
        }
        else
        {
            StatusBtn4 = false;
        }
        //================================
    }    
}
