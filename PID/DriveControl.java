/*
	Written by Tom Choi 07/23/2016
	for Carleton College Summer Computer Science Institute
	for high school students.
	
	DriveControl class provides
	leJOS nxt brick basic control interface
	such as setting its motor powers and moving it forward.
	
	The integer instance variables are collected
	by observing the amount of light reflected to the light sensor
	when it shone
		- a blue tape (TOTALLYONLINE)
		- a white board (TOTALLYOFFLINE)
		- the border between the tape and the board (TARGET)
	so that the brick can recognize and travel along the border.
*/

import lejos.nxt.*;
import lejos.util.*;

public abstract class DriveControl{
	
	protected final int ONLINE = 52;
	protected final int OFFLINE = 58;
	protected final int TOTALLYONLINE = 48;
	protected final int TOTALLYOFFLINE = 62;
	protected final int TARGET_LOW = 53;
	protected final int TARGET = 55;
	protected final int TARGET_HIGH = 57;
	
	protected int brightness;
	protected NXTMotor motorA;
	protected NXTMotor motorC;
	protected LightSensor light;
	
	public DriveControl(){
		this.motorA = new NXTMotor(MotorPort.A);
		this.motorC = new NXTMotor(MotorPort.B);
		this.light = new LightSensor(SensorPort.S2);
	}
	
	protected void setPower(int both){
		motorA.setPower(both);
		motorA.setPower(both);
	}
	
	protected void setPower(int A, int C){
		motorA.setPower(A);
		motorC.setPower(C);
	}
	
	protected void roll(){
		motorA.forward();
		motorC.forward();
	}
	
	public abstract void drive();
}