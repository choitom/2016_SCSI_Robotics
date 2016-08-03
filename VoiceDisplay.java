import lejos.util.*;
import lejos.nxt.*;
import javax.microedition.lcdui.Graphics;

/*
	Author : Tom Choi (Research Assistant)
	Date : 07/28/2016
	Class : Robotics (leJOS NXT)
	
	Written for Carleton College Summer Computer Science Institute
	for high school students to facilitate the visibility of the sound
	in terms of its magnitude and the change of magnitude over time.
	
	VoiceDisplay has three screens that display
		1. The magnitude of voice
		2. The rate of change in the magnitude of voice
		3. Screen 1 and 2 splitted into half
*/

public class VoiceDisplay{
	public final int width = LCD.SCREEN_WIDTH;
	public final int height = LCD.SCREEN_HEIGHT;
	public final int screenH0 = 12;
	public final int screenH1 = 61; // for volume lower limit
	public final int screenH2 = 64; // for change lower limit
	public final int midPoint = (screenH0 + screenH2)/2;
	
	private int[] volumes;
	private int size = 0;
	private String page = "volume";
	
	private int v = 0;			// volume
	private int v_scaled = 0;	// scaled volume for display
	private int dv = 0;			// change in volume
	private int dv_scaled = 0;	// scaled change in volume for display
	private int pv = 0;			// previous volume
	
	private boolean leftDown;	// is left button down
	private boolean rightDown;	// is right button down
	
	private SoundSensor sound;
	private Graphics g;
	
	
	public VoiceDisplay(){
		sound = new SoundSensor(SensorPort.S1);
		g = new Graphics();
		volumes = new int[100];
	}
	
	// Displays the header of each page
	public void display(){
		while(true){
			g.clear();
			if(page.equals("volume")){
				volumeDisplay();
			}else if(page.equals("change")){
				changeDisplay();
			}else{
				bothDisplay();
			}
			Delay.msDelay(500);
		}
	}
	
	// Displays the volume page
	private void volumeDisplay(){
		drawVolumeMenu();
		initPressed();
		
		Delay.msDelay(300);
		while(!leftDown && !rightDown){
			v = sound.readValue();
			updateVolume();
			LCD.drawInt(v, 2, 0, 0);
			
			drawHalfLine();
			drawVolumeHorizontalLine(0, width);
			updateDisplay(v, screenH1);
			drawVolumeBar(size, v_scaled);
			checkButtonDown();
			Delay.msDelay(100);
		}
		reinitialize();
		setPage("volume");
	}
	
	// Displays the rate of change in volume page
	private void changeDisplay(){
		drawChangeMenu();
		initPressed();
		pv = -9999;
		
		while(!leftDown && !rightDown){
			v = sound.readValue();
			updateRate();
			LCD.drawInt(dv, 3, 0, 0);
			drawHalfLine();
			drawChangeHorzontalLine(0, width);			
			updateDisplay(dv, screenH2);
			drawChangeBar(size, dv_scaled);
			checkButtonDown();
			Delay.msDelay(100);
		}
		reinitialize();
		setPage("change");
	}
	
	// Displays the screen of both volume and the rate page
	private void bothDisplay(){
		drawBothMenu();
		initPressed();
		pv = -9999;
	
		while(!leftDown && !rightDown){
			v = sound.readValue();
			updateVolume();
			updateRate();
			
			LCD.drawInt(v, 2, 0, 0);
			LCD.drawInt(dv, 3, 13, 0);
			drawHalfLine();
			drawChangeHorzontalLine(50, width);
			drawVolumeHorizontalLine(0, width/2);
			
			updateDisplay(v, screenH1);
			updateDisplay(dv, height);
			
			drawVolumeBar(size/2, v_scaled);
			drawChangeBar(size/2 + width/2, dv_scaled);

			checkButtonDown();
			Delay.msDelay(100);
		}
		reinitialize();
		setPage("both");
	}
	
	private void drawVolumeMenu(){
		LCD.drawString("<< VOLUME >>", 3, 0);
		initVolumeInterface();
	}
	
	private void drawChangeMenu(){
		LCD.drawString("<< RATE >>", 4, 0);
		initChangeInterface();
	}
	
	private void drawBothMenu(){
		LCD.drawString("<< BOTH >>", 3, 0);
		initBothInterface();
	}
	
	private void initVolumeInterface(){
		commonInterface();
		
		// lower horizontal line
		drawVolumeHorizontalLine(0, width);
	}
	
	private void initChangeInterface(){
		commonInterface();
		drawChangeHorzontalLine(0, width);
	}
	
	private void initBothInterface(){
		commonInterface();
		drawChangeHorzontalLine(50, width);
		drawVolumeHorizontalLine(0, width/2);
	}
	
	// The interface that all three screens share
	private void commonInterface(){
		// draw scattered dots underneath the title
		for(int i = 0; i < width; i = i + 2){
			g.drawLine(i, 8, i+4, screenH0);
		}
		
		// upper horizontal line
		g.drawLine(0, screenH0, width, screenH0);
		
		// half line
		drawHalfLine();
	}
	
	// Draw the x-axis for the rate of change in volume on the screen
	private void drawChangeHorzontalLine(int start, int endPoint){
		for(int i = start; i < endPoint; i +=3){
			g.drawLine(i, midPoint, i+1, midPoint);	
		}
	}
	
	// Draw the x-axis for the magnitude of volume on the screen
	private void drawVolumeHorizontalLine(int start, int endPoint){
		g.drawLine(start,screenH1,endPoint,screenH1);
		
		// draw marking underneath the lower horizontal line
		for(int i = start; i < endPoint; i += 5){
			g.drawLine(i,screenH1, i, screenH1+2);
		}
	}
	
	// Draw the half demarcation line
	private void drawHalfLine(){
		for(int j = screenH0+2; j <= height; j += 3){
			g.drawLine(50,j,50,j+1);
		}
	}
	
	// Draw the magnitude of a given volume
	private void drawVolumeBar(int index, int volume){
		g.drawLine(index, screenH1 - volume - 1, index, screenH1 - 1);
	}
	
	// Draw the magnitude of the change in volume
	private void drawChangeBar(int index, int change){
		g.drawLine(index, midPoint - change, index, midPoint);
	}
	
	// Once a screen refreshes or changes, reinitialize the settings
	private void reinitialize(){
		for(int i = 0; i < volumes.length; i++){
			volumes[i] = 0;
		}
		size = 0;
	}
	
	// Initialize the left, right button down settings
	private void initPressed(){
		leftDown = false;
		rightDown = false;
	}
	
	// Set the next page to see based on the current page and the button pressed
	private void setPage(String currentPage){
		if (currentPage.equals("volume")){
			page = (leftDown) ? "both" : "change";
		}else if(currentPage.equals("change")){
			page = (leftDown) ? "volume" : "both";
		}else{
			page = (leftDown) ? "change" : "volume";
		}
	}
	
	private void checkButtonDown(){
		if (Button.LEFT.isDown()){
			leftDown = true;
		}else if (Button.RIGHT.isDown()){
			rightDown = true;
		}
	}
	
	// For each volume or rate added, update the list that stores the value
	private void updateDisplay(int value, int height){
		g.setColor(0,0,0);
		if(size < volumes.length){
			volumes[size] = value;
		}else{
			g.setColor(255, 255, 255);
			g.fillRect(0, screenH0+1, 100, height-screenH0 + 2);
			reinitialize();
			volumes[size] = value;
		}
		size++;
	}
	
	// Update the current volume
	private void updateVolume(){
		v_scaled = (int)(v / 1.4);
		
		// above max, set to max
		if(v_scaled > 48){
			v_scaled = 48;
		}
	}
	
	// Update the current change in volume
	private void updateRate(){
		// update the change in volume for every 100ms
		if (pv == -9999){
			pv = v;
		}else{
			dv = v - pv;
			dv_scaled = (int)(dv / 2);
			pv = v;
		}

		if (dv_scaled > (screenH2 - midPoint)){
			dv_scaled = screenH2 - midPoint;
		}
	}
	
	public static void main(String[] args){
		VoiceDisplay vd = new VoiceDisplay();
		vd.display();
	}
}