/*
	Written by Tom Choi 07/24/2016
	for Carleton College Summer Computer Science Institute
	for high school students.
	
	Implementation of Integral(D) control
		- Assumes that the blue tape is on its left side
		- Has the following features:
			A.	Adjusts the motor power in proportion to
				the amount of change in brightness aka error
			B.	Adjusts the motor power in proportion to
				the amount of change in the error aka dError
			C.	Adjusts the motor power in proportion to
				the sum of the total error aka errorSum
		- Variables for the integral control is obtained
		  from personal observations, trial & error
		- It tries to stabilize at the point where
			A. dError = 0
			B. error = TARGET(53)
			C. errorSum = 0
*/

import lejos.nxt.*;
import lejos.util.*;

public class Integral extends DriveControl{
	
	private Thread sumThread;
	private Thread dErrorThread;
	
	private final int BASEPOWER_LEFT = 35;
	private final int BASEPOWER_RIGHT = 50;
	
	/*
		dError > 0 : veering closer to the right
		dError < 0 : veering closer to the tape
	*/
	private int dError = 0;
	private int error = 0;
	private int errorSum = 0;
	private int leftP = 8;
	private int rightP = 2;
	private double Kd = 0.888;
	private double Kp = 1.5;
	private double Ki = 0.45;
	
	public Integral(){
		super();
		initSumThread();
		initDErrorThread();
	}
	
	
	public void drive(){
		
		setPower(BASEPOWER_LEFT, BASEPOWER_RIGHT);
		dErrorThread.start();
		sumThread.start();
		
		while(true){
			brightness = light.getLightValue();
			error = brightness - TARGET;
			
			/* Moving straight
				-> keep moving straight
			*/
			if (error == 0){
				setPower(BASEPOWER_LEFT, BASEPOWER_RIGHT);
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
		int dAdjust_Right = (int)(Kd * dError);
		int iAdjust_Right = (int)(Ki * errorSum);
		
		/*	error > 0, dError > 0
			veering along the board, getting away from the target
		*/	
		if (dError > 0){
			setPower(BASEPOWER_LEFT - pAdjust_Right + dAdjust_Right - 3 * iAdjust_Right,
					 BASEPOWER_RIGHT + pAdjust_Right - dAdjust_Right + 3 * iAdjust_Right);
		}
		
		/*	error > 0, dError < 0
			veering along the board, and closing in to the target
		*/
		else{
			setPower(BASEPOWER_LEFT - pAdjust_Right,
					 BASEPOWER_RIGHT + pAdjust_Right);
		}
	}
	
	private void adjustLeft(){
		int pAdjust_Left = leftP * (int)(Kp * error);
		int dAdjust_Left = (int)(Kd * dError);
		int iAdjust_Left = (int)(Ki * errorSum);
		
		
		/*	error < 0, dError > 0
			veering along the tape, getting closer to the target
		*/
		if(dError > 0){
			setPower(BASEPOWER_LEFT - pAdjust_Left,
					 BASEPOWER_RIGHT + pAdjust_Left);
		}
		/*	error < 0, dError <= 0
			veering along the tape, getting away from target
		*/
		else{
			setPower(BASEPOWER_LEFT - pAdjust_Left + dAdjust_Left - 4 * iAdjust_Left,
					 BASEPOWER_RIGHT + pAdjust_Left - dAdjust_Left + 4 * iAdjust_Left);
		}
	}
	
	private void displayStatus(){
		LCD.drawString("Light: ", 0, 0);
		LCD.drawInt(brightness, 3, 7, 0);
		LCD.drawString("Error: ", 0, 1);
		LCD.drawInt(error, 3, 7, 1);
		LCD.drawString("dError: ", 0, 2);
		LCD.drawInt(dError, 3, 7, 2);
		LCD.drawString("Sum : ", 0, 3);
		LCD.drawInt(errorSum, 3, 7 ,3);
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
					Delay.msDelay(100);
				}
			}
		};
	}
	
	private void initSumThread(){
		sumThread = new Thread(){
			int current;
			int error;
			
			public void run(){
				while(true){
					current = light.getLightValue();
					error = current - TARGET;
					
					if (Math.abs(errorSum) >= 100){
						errorSum = 100;
					}else{
						errorSum += error;
					}
					
					Delay.msDelay(1025);
				}
			}
		};
	}
	
	public static void main(String[] args){
		Integral i = new Integral();
		i.drive();
	}
}