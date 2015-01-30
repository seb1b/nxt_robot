package behaviors;


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


public class Bridge implements Behavior {
	private boolean suppressed = false;
	
	private static int DARK = 40;
	LightSensor lightSensor;
	SensorPort lightPort;
	UltrasonicSensor sonicSensor;
	
	
	
	
	public Bridge() {
		
		System.out.println("inizialiese light");
		lightPort = SensorPort.S4;
		
		

		lightSensor = new LightSensor(lightPort);
		sonicSensor = new UltrasonicSensor(SensorPort.S1);
		

	}

	public boolean takeControl() {

		
		return true;
	}

	public void action() {
		
		while(!suppressed){
		//	RConsole.open();
		int distance = sonicSensor.getDistance();
		int light_value = lightSensor.getLightValue();
		
		
		
		//RConsole.println("Distance"+ distance);
		//RConsole.println("Light"+ light_value);
		
		System.out.println("Distance"+ distance);
		System.out.println("Light"+ light_value);
		//RConsole.close();
		}
			
			
	}



	public void suppress() {
		suppressed = true;
	}


}