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
	private Values value;
	
	public Controls() {
		sonicSensor = new UltrasonicSensor(SensorPort.S2);
		lightSensor = new LightSensor(SensorPort.S3);
		value = Values.Instance();
		
		
	}
	
	public static Controls Instance() {
		if (instance != null)
			return instance;

		else {
			instance = new Controls();
			return instance;
		}
	}
	
	public void align(int lower_border, int upper_border,int time_limit){
	//	long start_time = System.currentTimeMillis();

		//doAlign(start_time,time_limit)
		pilot = value.getPilot();
		pilot.setTravelSpeed(10);
		while(lightSensor.getLightValue()<55){
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
	
	public void setSuppressed(boolean suppressed) {

		this.suppressed = suppressed;
	}
	
	public boolean isSuppressed() {
		return suppressed;
	}

	
	public boolean foundLine(){
		boolean on_line = false;
		if(lightSensor.getLightValue() > 50){
			System.out.println("gotlight");
			Values.Instance().getPilot().stop();
			on_line = true;
		}
			return on_line;
		
	}

	
	private boolean doAlign(long time, int time_limit) {
		return (System.currentTimeMillis() - time) < time_limit;
	}
}
