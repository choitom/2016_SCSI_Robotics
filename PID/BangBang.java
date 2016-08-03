/*
	Written by Tom Choi 07/23/2016
	for Carleton College Summer Computer Science Institute
	for high school students.
	
	Implementation of BangBang control
		- Assumes that the blue tape is on its left side
		- Sets different powers to each motor depending on
		  how the robot is off in terms of the brightness
*/

import lejos.nxt.*;
import lejos.util.*;

public class BangBang extends DriveControl{
	
	public BangBang(){
		super();		
	}
	
	public void drive(){
		setPower(30);
		Delay.msDelay(1000);
			
		while(true){
			brightness = light.getLightValue();
			displayBrightnessStatus();
			
			if (brightness >= TARGET_LOW && brightness <= TARGET_HIGH){
				setPower(30);
			}
			else if (brightness <= ONLINE && brightness > TOTALLYONLINE){
				setPower(35, 30);
			}else if (brightness >= OFFLINE && brightness < TOTALLYOFFLINE){
				setPower(30, 35);
			}else if (brightness >= TOTALLYOFFLINE){
				while(!(brightness >= TARGET_LOW && brightness <= TARGET_HIGH)){
					brightness = light.getLightValue();
					setPower(0, 30);
					roll();
				}
				setPower(30);
			}else{
				while(!(brightness >= TARGET_LOW && brightness <= TARGET_HIGH)){
					brightness = light.getLightValue();
					setPower(30, 0);
					roll();
				}
				setPower(30);
			}
			roll();
		}
	}
	
	private void displayBrightnessStatus(){
		LCD.drawString("Light: ", 0, 0);
		LCD.drawInt(brightness, 3, 7, 0);
	}
	
	public static void main(String[] args){
		BangBang bang = new BangBang();
		bang.drive();
	}
}