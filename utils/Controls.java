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
	
	public void alignUntilLight(int lower_border, int upper_border, int light_value) {
		
		pilot.setTravelSpeed(20);
		
		while(lightSensor.getLightValue() < light_value) {
			int distance = sonicSensor.getDistance();

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
	

	public void alignUntilDistance(int lower_border, int upper_border, int threshold, int speed) {
		int distance = -1;
		pilot.setTravelSpeed(speed);
		pilot.forward();
		int count_thresh = 0;
		
		while(count_thresh > 20) {
			distance = sonicSensor.getDistance();
			
			if(distance > threshold) {
				count_thresh++;
				System.out.println("++"+distance);
			}

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
	
	public void alignForTime(int lower_border, int upper_border, int duration, int speed) {
		long time = System.currentTimeMillis();
		int distance = 999;
		pilot.setTravelSpeed(speed);
		
		while((System.currentTimeMillis() - time) < duration) {
			distance = sonicSensor.getDistance();

			//System.out.println(distance);
			

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
		if(!lightSensor.isFloodlightOn()){
			lightSensor.setFloodlight(true);
		}

		if(lightSensor.getLightValue() > 58){

			System.out.println("gotlight");
			on_line = true;
		}
			return on_line;
		
	}

	
	private boolean doAlign(long time, int time_limit) {
		return (System.currentTimeMillis() - time) < time_limit;
	}
}
