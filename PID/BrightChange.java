import lejos.nxt.*;
import lejos.util.*;

public class BrightChange{
	private LightSensor sensor;
	
	public BrightChange(){
		this.sensor = new LightSensor(SensorPort.S2);
	}
	
	public void measure(){
		boolean dErrorInitialized = false;
		int brightness = 0;
		int error = 0;
		int dError = 0;
		int pError = 0;
		
		while(!Button.ENTER.isDown()){
			brightness = sensor.getLightValue();
			error = brightness - 53;
			
			if (!dErrorInitialized){
				pError = error;
				dErrorInitialized = true;
			}else{
				dError = error - pError;
				pError = error;
			}
			LCD.drawString("dError: ", 0, 0);
			LCD.drawInt(dError, 3, 8, 0);
			LCD.drawString("error: ", 0, 1);
			LCD.drawInt(error, 3, 8, 1);
			LCD.drawString("pError: ", 0, 2);
			LCD.drawInt(pError, 3, 8, 2);
			Delay.msDelay(100);
		}
	}
	
	public static void main(String[] args){
		BrightChange b = new BrightChange();
		b.measure();
	}
}