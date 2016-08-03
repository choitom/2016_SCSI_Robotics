import lejos.nxt.*;
import lejos.util.*;

public class LightSum{
	private final int TARGET = 53;
	private LightSensor light;
	private Thread sumThread;
	
	private int sum = 0;
	
	public LightSum(){
		light = new LightSensor(SensorPort.S2);
		initSumThread();
	}
	
	public void measure(){
		sumThread.start();
	}
	
	private void initSumThread(){
		sumThread = new Thread(){
			int current;
			int error;
			
			public void run(){
				while(true){
					current = light.getLightValue();
					error = current - TARGET;
					sum += error;
					
					LCD.drawInt(sum, 3, 0, 0);
					Delay.msDelay(200);
				}
			}
		};
	}
	
	public static void main(String[] args){
		LightSum l = new LightSum();
		l.measure();
	}
}