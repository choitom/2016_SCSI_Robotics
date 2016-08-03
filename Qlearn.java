import lejos.nxt.*;
import lejos.util.*;
import java.util.Random;

/* 
	Initial Q-learning program.
	by Dave Musicant

	Modified by Tom Choi 07/24/2016
	for Carleton College Summer Computer Science Institute
	for high school students
	
	The update algorithm for Q-learning is
		Q_new(s,a) = Q_old(s,a) + alpha * (R_new - Q_old(s,a))
*/

public class Qlearn {
    private final int NUMSTATES = 3;
    private final int NUMACTIONS = 3;

    private final int TURNLEFT = 0;
    private final int FORWARD = 1;
    private final int TURNRIGHT = 2;

    private final int MINULTRA = 15;
    private final int MAXULTRA = 20;

    private final int TOOCLOSE = 0;
    private final int GOODENOUGH = 1;
    private final int TOOFAR = 2;

	private final double ALPHA = 0.5;
	private int distance = 0;
	private double reward = -1;
	
    private final String[] states = {"TC","GE","TF"};
	private double qvalues[][]; 
	private UltrasonicSensor sonic;
	private Random rand;
	
	public Qlearn(){
		initQvalues();
		sonic = new UltrasonicSensor(SensorPort.S1);
		rand = new Random();
	}
	
    // Display array of Q-values to the screen.
    public void display(){
        LCD.clear();
        for (int i=0; i < NUMSTATES; i++){
			LCD.drawString(states[i],0,i+1);
			for (int j=0; j < NUMACTIONS; j++) {
                LCD.drawInt((int)(qvalues[i][j]*100),3,3+j*5,i+1);
            }
		}
            
        LCD.drawString("    TL   GF   TR",0,0);
		LCD.drawString("Distance:",0,6);
		LCD.drawInt(distance,3,10,6);
    }
	
	public void startLearn(){
		int currentState = -1;
		
        while(!Button.ENTER.isDown()){
			distance = sonic.getDistance();
			display();
			
			// Too close
			if (distance < MINULTRA){
				qLearning(TOOCLOSE);
			}
			// Too far
			else if (distance > MAXULTRA){
				qLearning(TOOFAR);
			}
			// Just enough
			else{
				qLearning(GOODENOUGH);
			}
			Delay.msDelay(500);
        }
		
		while(true){
			distance = sonic.getDistance();
			display();
			
			// Too close
			if (distance < MINULTRA){
				qRunning(TOOCLOSE);
			}
			// Too far
			else if(distance > MAXULTRA){
				qRunning(TOOFAR);
			}
			// Just enough
			else{
				qRunning(GOODENOUGH);
			}
		}
	}
	
	private void qLearning(int state){
		int index = rand.nextInt(3);
		
		// set the reward value based on the current state and action
		if ((state == TOOCLOSE && index == TURNLEFT) ||
			(state == GOODENOUGH && index == FORWARD) ||
			(state == TOOFAR && index == TURNRIGHT)){
			reward = 1;
		}else {
			reward = 0;
		}
		updateQvalue(state, index);
		roll(index);
	}
	
	private void qRunning(int state){
		double max = qvalues[state][0];
		int index = 0;
		for(int i = 1; i < NUMACTIONS; i++){
			if (qvalues[state][i] > max){
				max = qvalues[state][i];
				index = i;
			}
		}
		roll(index);
	}
	
	private void roll(int index){
		if (index == TURNLEFT){
			Motor.A.setSpeed(180);
			Motor.C.setSpeed(250);
		}else if (index == FORWARD){
			Motor.A.setSpeed(180);
			Motor.C.setSpeed(180);
		}else{
			Motor.A.setSpeed(250);
			Motor.C.setSpeed(180);
		}
		Motor.A.forward();
		Motor.C.forward();
	}
	
	private void updateQvalue(int state, int action){
		qvalues[state][action] = qvalues[state][action] +
								ALPHA * (reward - qvalues[state][action]);
	}

	private void initQvalues(){
		qvalues = new double[NUMSTATES][NUMACTIONS];
		for (int i = 0; i < NUMSTATES; i++){
			for (int j = 0; j < NUMACTIONS; j++){
				qvalues[i][j] = 0;
			}
		}
	}

    public static void main(String[] args){
		Qlearn q = new Qlearn();
		q.startLearn();
    }
}