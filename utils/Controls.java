package utils;

import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.util.Delay;

public class Controls {

	private static Controls instance = null;
	private boolean suppressed;
	UltrasonicSensor sonicSensor;
	public boolean found_line =false;
	private static int SOFT_STEER = 40;
	LightSensor lightSensor;
	DifferentialPilot pilot;
	//private Values value;
	
	public Controls() {
		sonicSensor = new UltrasonicSensor(SensorPort.S2);
		lightSensor = new LightSensor(SensorPort.S3);
		//value = Values.Instance();
		pilot = Values.Instance().getPilot();
		
	}
	
	public static Controls Instance() {
		if (instance != null)
			return instance;

		else {
			instance = new Controls();
			return instance;
		}
	}
	
	public void alignUntilLight(int lower_border, int upper_border) {
		
		pilot.setTravelSpeed(10);
		
		while(lightSensor.getLightValue() < 55) {
			int distance = sonicSensor.getDistance();

<<<<<<< HEAD
			System.out.println(distance);
			
			// Good distance
			if(lower_border < distance && upper_border > distance) {
				pilot.forward();
				continue;
			}
			
			// Too far
			if(lower_border >= distance) {
				pilot.steer(-SOFT_STEER);
				continue;
			}
						
			// Too near
			if(upper_border <= distance) {
				pilot.steer(SOFT_STEER);
				continue;
			}
			
		}
}
	
	public void alignUntilDistance(int lower_border, int upper_border, int threshold) {
		int distance = 999;
		pilot.setTravelSpeed(10);
		
		while(distance < threshold) {
			distance = sonicSensor.getDistance();

			System.out.println(distance);
			
=======
			System.out.println(distance);			
>>>>>>> d8d9fc9ad7054dd8b8cfcf71434244449d63a481
			// Good distance
			if(lower_border < distance && upper_border > distance) {
				pilot.forward();
				continue;
			}
			
			// Too far
			if(lower_border >= distance) {
				pilot.steer(-SOFT_STEER);
				continue;
			}
						
			// Too near
			if(upper_border <= distance) {
				pilot.steer(SOFT_STEER);
				continue;
			}
		}
	}
	
	public void setSuppressed(boolean suppressed) {

		this.suppressed = suppressed;
	}
	
	public boolean isSuppressed() {
		return suppressed;
	}

	
	public boolean foundLine(){
		boolean on_line = false;
<<<<<<< HEAD
		if(lightSensor.getLightValue() > 58){
=======
		if(lightSensor.getLightValue() > 57){
>>>>>>> d8d9fc9ad7054dd8b8cfcf71434244449d63a481
			System.out.println("gotlight");
			on_line = true;
		}
			return on_line;
		
	}

	
	private boolean doAlign(long time, int time_limit) {
		return (System.currentTimeMillis() - time) < time_limit;
	}
}
