/*
	Written by Tom Choi 07/24/2016
	for Carleton College Summer Computer Science Institute
	for high school students.
	
	Implementation of Derivative(D) control
		- Assumes that the blue tape is on its left side
		- Has the following features:
			A.	Adjusts the motor power in proportion to
				the amount of change in brightness aka error
			B.	Adjusts the motor power in proportion to
				the amount of change in the error aka dError
		- It tries to stabilize at the point where
			A. dError = 0
			B. error = TARGET(53)
			
*/

import lejos.nxt.*;
import lejos.util.*;

public class Derivative extends DriveControl{
	
	private Thread dErrorThread;
	
	private final int BASEPOWER = 46;
	
	/*
		dError > 0 : veering closer to the right
		dError < 0 : veering closer to the tape
	*/
	private int dError = 0;
	private int error = 0;
	private int leftP = 10;
	private int rightP = 2;
	private double Kd = 0.85;
	private double Kp = 1.6;
	
	public Derivative(){
		super();
		initDErrorThread();
	}
	
	public void drive(){
		dErrorThread.start();
		setPower(BASEPOWER);
		
		while(true){
			brightness = light.getLightValue();
			error = brightness - TARGET;
			
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
				adjustRight();
			}
			
			/* Verring closer to the tape
				-> more power to the left tire
			*/
			else {
				adjustLeft();
			}
			
			roll();
			displayStatus();
		}
	}
	
	private void adjustRight(){
		int pAdjust_Right = rightP * (int)(Kp * error);
		int dAdjust_Right = dAdjust_Right = (int)(Kd * dError);
		
		/*	error > 0, dError > 0
			veering along the board, getting away from the target
		*/	
		if (dError > 0){
			setPower(BASEPOWER - pAdjust_Right + dAdjust_Right,
					 BASEPOWER + pAdjust_Right - dAdjust_Right);
		}
		
		/*	error > 0, dError < 0
			veering along the board, and closing in to the target
		*/
		else{
			setPower(BASEPOWER - pAdjust_Right,
					 BASEPOWER + pAdjust_Right);
		}
	}
	
	private void adjustLeft(){
		int pAdjust_Left = leftP * (int)(Kp * error);
		int dAdjust_Left = (int)(Kd * dError);
		
		
		/*	error < 0, dError > 0
			veering along the tape, getting closer to the target
		*/
		if(dError > 0){
			setPower(BASEPOWER - pAdjust_Left,
					 BASEPOWER + pAdjust_Left);
		}
		/*	error < 0, dError <= 0
			veering along the tape, getting away from target
		*/
		else{
			setPower(BASEPOWER - pAdjust_Left + dAdjust_Left,
					 BASEPOWER + pAdjust_Left - dAdjust_Left);
		}
	}
	
	private void displayStatus(){
		LCD.drawString("Light: ", 0, 0);
		LCD.drawInt(brightness, 3, 7, 0);
		LCD.drawString("Error: ", 0, 1);
		LCD.drawInt(error, 3, 7, 1);
		LCD.drawString("dError: ", 0, 2);
		LCD.drawInt(dError, 3, 7, 2);
	}
	
	/*
		Initializes a thread that calculates the change in
		brightness of error for every 100ms.
	*/
	private void initDErrorThread(){
		dErrorThread = new Thread(){
			boolean isFirstRun = true;
			int previousError = 0;
			int currentBrightness = 0;
			int currentError = 0;
			
			public void run(){
				while(true){
					currentBrightness = light.getLightValue();
					currentError = currentBrightness - TARGET;
					
					if(isFirstRun){
						previousError = currentError;
						isFirstRun = false;
					}else{
						dError = currentError - previousError;
						previousError = currentError;
					}
					Delay.msDelay(90);
				}
			}
		};
	}
	
	public static void main(String[] args){
		Derivative d = new Derivative();
		d.drive();
	}
}