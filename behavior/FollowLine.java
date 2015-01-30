package git.behavior;


import lejos.robotics.navigation.DifferentialPilot;
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
	private boolean SEARCH_RADIUS_LEFT = false;
	private boolean SEARCH_RADIUS_RIGHT = false;
	private int turning_limit = 0;
	LightSensor lightSensor;
	SensorPort lightPort;
	DifferentialPilot pilot;
	
	public FollowLine() {
		
		System.out.println("inizialiese light");
		lightPort = SensorPort.S1;

		lightSensor = new LightSensor(lightPort);
		pilot = new DifferentialPilot(1.3f, 3.94f, Motor.A, Motor.C, false); 
		

	}

	public boolean takeControl() {

		
		return true;
	}

	public void action() {
		
		//long timeStart=System.currentTimeMillis();
		pilot.setTravelSpeed(5);
		int counter = 0;
		// cm per second
		while(!suppressed){
			System.out.println("ON LINE: " + lightSensor.getLightValue());
			
			/*while(DARK < lightSensor.getLightValue()){
				
				pilot.forward();
				 
				System.out.println("ON LINE: " + lightSensor.getLightValue());
				turning_limit = 0;

			}
			counter = 0;
			turning_limit++;
			
			while(turning_limit > counter && DARK > lightSensor.getLightValue()){				
				System.out.println("left " + lightSensor.getLightValue());
				pilot.rotateLeft();		
				counter++;
				
			}
			counter = 0;
			turning_limit++;
			while(turning_limit > counter && DARK > lightSensor.getLightValue()){
				System.out.println("right " + lightSensor.getLightValue());
				pilot.rotateRight();	
				counter++;
			}
			
			
				*/
				
			
			
		}


		



	}

	public void suppress() {
		suppressed = true;
	}
	
	private void turnleft(){
		Motor.A.backward();
		Motor.C.forward();
		
	}
	
	private void turnright(){
		Motor.A.forward();
		Motor.C.backward();
	}
	
	private boolean isLine(){
		boolean line_available = false;
		if(lightSensor.getLightValue() > DARK){
			
			line_available = true;
		}
		return line_available;
	}


}
