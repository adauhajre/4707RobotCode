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
 * @author Quelvin
 */
public class Brazo_KGM {
    static double previous_error = 0;
    static double integral = 0 ;
    static double error;
    static double setpoint = 97;
    static double actual_position;
    static double dt = 0.005;
    static double derivative;
    static double output;
    static double Kp = 0.07;//0.1;
    static double Ki = 0;
    static double Kd = 0;
    static double kFactorEncoder = 2.8;
    
    static boolean StatusBtn2 = false; //-- Resta 
    static boolean StatusBtn4 = false; //-- Suma
    
    static DigitalInput lsUp = new DigitalInput(5);

    public static void PosicionBrazo(int Posicion) 
    {
        setpoint = Posicion;
    }
    
    public static void EngineAuto(Encoder enc, DriverStationLCD LCD, RobotDrive BMotors) 
    {   
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
    }    
    
    public static void Engine(Joystick ctrl, Encoder enc, DriverStationLCD LCD, RobotDrive BMotors) 
    {   
        actual_position = enc.getDistance() / kFactorEncoder;
        
        error = setpoint - actual_position;
        integral = integral + (error*dt);
        derivative = (error - previous_error)/dt;
        output = (((Kp*error) + (Ki*integral) + (Kd*derivative)) / -2.5);
        previous_error = error;
        
        LCD.println(DriverStationLCD.Line.kUser1, 1, "Battery   : " + BatteryVoltage.GetVoltage());
        LCD.updateLCD();
        LCD.println(DriverStationLCD.Line.kUser2, 1, "SetPoint  : " + setpoint);
        LCD.updateLCD();
        LCD.println(DriverStationLCD.Line.kUser3, 1, "ActualPos : " + actual_position);
        LCD.updateLCD();
        
        if(lsUp.get())
        {
            LCD.println(DriverStationLCD.Line.kUser4, 1, "Switch : OFF");
        }
        else
        {
            LCD.println(DriverStationLCD.Line.kUser4, 1, "Switch : ON");
        }
        LCD.updateLCD();
        
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

        LCD.println(DriverStationLCD.Line.kUser5, 1, "PWM : " + output);
        LCD.updateLCD();
        
        //if(output<-0.8) {output=-0.8;}
        //if(output> 0.8) {output= 0.8;}
        
        BMotors.tankDrive(output, output);
        
        if(ctrl.getRawButton(1))
        {
            PosicionBrazo(37);
        }
        
        if(ctrl.getRawButton(3))
        {
            PosicionBrazo(34);
        }
        
        if(ctrl.getRawButton(9))
        {
            //PosicionBrazo(10);
        }
        
        if(ctrl.getRawButton(10))
        {
            PosicionBrazo(100);
        }
        
        //================================
        if(ctrl.getRawButton(2))
        {   
            if(StatusBtn4 == false)
            {
                setpoint += 3;
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
            if(StatusBtn2 == false)
            {
                setpoint -= 3;
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
