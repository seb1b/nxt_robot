package behaviors;


import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.subsumption.Behavior;
import lejos.nxt.comm.RConsole;
import lejos.util.*;
import lejos.nxt.I2CPort;
import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.Sound;
import lejos.nxt.TouchSensor;
import lejos.nxt.UltrasonicSensor;


public class Startphase implements Behavior {
	private boolean suppressed = false;
	
	
	TouchSensor touch;
	
	private static boolean LINE = true;
	private static boolean LEFT_CURVE = false;
	private static boolean RIGHT_CURVE = false;
	int counter = 0;
	DifferentialPilot pilot;
	
	
	
	public Startphase() {
		
		touch = new TouchSensor(SensorPort.S3);
		pilot = new DifferentialPilot(1.3f, 3.94f, Motor.A, Motor.C, true); 

	}

	public boolean takeControl() {

		
		return true;
	}

	public void action() {
		counter = 0;
		while(!suppressed){
			pilot.setTravelSpeed(100);
			
		while(LINE){
			pilot.backward();
			//is_touching = touch.isPressed();
			System.out.println("not pressed");
			if(touch.isPressed()&& counter <2){
				System.out.println("is touching");
				LEFT_CURVE = true;
				LINE = false;
				counter++;
			}
			if(touch.isPressed() && counter > 2){
				LINE = false;
				RIGHT_CURVE = true;
			}
			
			
		}
		
		while(LEFT_CURVE){
			System.out.println("left");
			//pilot.forward();
			//Delay.msDelay(100);
			pilot.rotate(-120);
			LEFT_CURVE = false;
			LINE = true;
			
		}
			
			
		}
			
		while(RIGHT_CURVE){
			System.out.println("right");
			//pilot.forward();
			//Delay.msDelay(100);
			pilot.rotate(120);
			RIGHT_CURVE = false;
			LINE = true;
			
			
		}
			
			
			
				
		
			
			
	}


		



	

	public void suppress() {
		suppressed = true;
	}


}
