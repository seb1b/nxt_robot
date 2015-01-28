package git.behavior;


import lejos.robotics.subsumption.Behavior;
import lejos.util.*;
import lejos.nxt.I2CPort;
import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.Sound;
import lejos.nxt.TouchSensor;
import lejos.nxt.UltrasonicSensor;


public class FollowLine implements Behavior {
	private boolean suppressed = false;
	
	private static int DARK = 40;
	private boolean ON_LINE = true;
	private boolean SEARCH_LINE_LEFT = false;
	private boolean SEARCH_LINE_RIGHT = false;
	
	LightSensor lightSensor;
	SensorPort lightPort;
	
	public FollowLine() {
		
		System.out.println("inizialiese light");
		lightPort = SensorPort.S4;

		lightSensor = new LightSensor(lightPort);

	}

	public boolean takeControl() {

		
		return true;
	}

	public void action() {
		int forward_speed = 400;
		int turn_speed = 400;
		int turning_counter = 0;
		int turning_radius = 10;
		while(!suppressed){
			
			while(ON_LINE){
				
				Motor.A.setSpeed(forward_speed);
				Motor.C.setSpeed(forward_speed);
				Motor.A.forward();
				Motor.C.forward();
				System.out.println("LINE" + lightSensor.getLightValue());
					if(lightSensor.getLightValue() < DARK){
						
						ON_LINE = false;
						SEARCH_LINE_LEFT = true;
						turning_counter = 0;
						turning_radius = 10;
					}
			}
			
			
			while(SEARCH_LINE_LEFT || turning_radius < turning_counter){
				
				Motor.A.setSpeed(turn_speed);
				Motor.C.setSpeed(turn_speed);
				
				turnleft();
				System.out.println("Search LEFT" + lightSensor.getLightValue());
				if(isLine()){
					ON_LINE = true;
					SEARCH_LINE_LEFT = false;
					turning_radius = turning_radius *2;
					turning_counter = 0;
				}

				turning_counter++;
				
				
			}
			
			while(SEARCH_LINE_RIGHT || turning_radius < turning_counter){
				
				Motor.A.setSpeed(turn_speed);
				Motor.C.setSpeed(turn_speed);
				
				turnright();
				System.out.println("Search RIGHT" + lightSensor.getLightValue());
				if(isLine()){
					ON_LINE = true;
					SEARCH_LINE_RIGHT = false;
					turning_radius = turning_radius *2;
					turning_counter = 0;
				}

				turning_counter++;
				
				
			}
			
			
				
				
			
			
		}


		



	}

	public void suppress() {
		suppressed = true;
	}
	
	private void turnleft(){
		Motor.B.forward();
		Motor.A.backward();
	}
	
	private void turnright(){
		Motor.A.forward();
		Motor.B.backward();
	}
	
	private boolean isLine(){
		boolean line_available = false;
		if(lightSensor.getLightValue() > DARK){
			
			line_available = true;
		}
		return line_available;
	}


}
