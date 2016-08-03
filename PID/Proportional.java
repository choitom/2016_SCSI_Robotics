/*
	Written by Tom Choi 07/23/2016
	for Carleton College Summer Computer Science Institute
	for high school students.
	
	Implementation of Proportional(P) control
		- Assumes that the blue tape is on its left side
		- Measures the error (change in brightness) and
		  adjusts the motor power proportional to the amount of error
		- The adjustment constant such as
			kp, leftAdjustConstant, rightAdjustConstant
		  are from my observations, trial & error.
		  
	
	*** CAUTION ***
		- If the constants or the base power are too high, 
		the robot rotates fast so that it cannot register the change
		in brightness fast enough and might end up malfunctioning.
		
		- If the constants are too low, the robot does not
		turn fast enough so that it might end up veering into
		the direction that was not intended.
*/

import lejos.nxt.*;
import lejos.util.*;

public class Proportional extends DriveControl{
	
	private final int BASEPOWER = 46;
	private int current = 0;
	private int error = 0;
	private int adjustment;
	private int leftAdjustConstant = 10;
	private int rightAdjustConstant = 4;
	private double Kp = 0.976;
	
	public Proportional(){
		super();
	}
	
	public void drive(){
		setPower(BASEPOWER);
		Delay.msDelay(1000);
		
		while(true){
			brightness = light.getLightValue();
			current = brightness;
			
			error = current - TARGET;
			
			
			/* Moving straight
				-> keep moving straight
			*/
			if (error == 0){
				setPower(BASEPOWER);
			}
			
			/* Veering closer to the board
				-> more power to the right tire
			*/
			else if (error > 0){
				int rightAdjustment = powerAdjustmentRight(error);
				adjustment = rightAdjustment;
				setPower(BASEPOWER - rightAdjustment, BASEPOWER + rightAdjustment);
			}
			
			/* Veering closer to the tape
				-> more power to the left tire
			*/
			else {
				int leftAdjustment = powerAdjustmentLeft(error);
				adjustment = leftAdjustment;
				setPower(BASEPOWER - (int)(1.2 * leftAdjustment), BASEPOWER + leftAdjustment);
			}
			
			roll();
			
			displayStatus();
		}
	}
	
	private int powerAdjustmentRight(int error){
		int adjustRight = (int) (Kp * error);
		return rightAdjustConstant * adjustRight;
	}
	
	private int powerAdjustmentLeft(int error){
		int adjustLeft = (int) (Kp * error);
		return leftAdjustConstant * adjustLeft;
	}
	
	private void displayStatus(){
		LCD.drawString("Light: ", 0, 0);
		LCD.drawInt(brightness, 3, 7, 0);
		LCD.drawString("Error: ", 0, 1);
		LCD.drawInt(error, 3, 7, 1);
		LCD.drawString("Adjust: ", 0, 2);
		LCD.drawInt(adjustment, 3, 7, 2);
	}
	
	public static void main(String[] args){
		Proportional p = new Proportional();
		p.drive();
	}
}