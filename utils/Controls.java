package utils;

import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import lejos.robotics.navigation.DifferentialPilot;

public class Controls {

	private static Controls instance = null;
	private boolean suppressed;
	UltrasonicSensor sonicSensor;
	public boolean found_line =false;
	private static int SOFT_STEER = 40;
	LightSensor lightSensor;
	
	public Controls() {
		sonicSensor = new UltrasonicSensor(SensorPort.S2);
		lightSensor = new LightSensor(SensorPort.S3);
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
		long start_time = System.currentTimeMillis();
		Motor.A.setSpeed(900);
		Motor.C.setSpeed(900);
		System.out.println("align");
		while(doAlign(start_time,time_limit) && lower_border < sonicSensor.getDistance() && upper_border > sonicSensor.getDistance()){
			//pilot.forward();
			Motor.A.forward();
			Motor.C.forward();
			//System.out.println("good Distance"+ sonicSensor.getDistance());
			
			
		}
		while(doAlign(start_time,time_limit)&& lower_border >=sonicSensor.getDistance()){
			Motor.A.setSpeed(900);
			Motor.C.setSpeed(500);
			Motor.A.forward();
			Motor.C.forward();
			//pilot.steer(-SOFT_STEER);
			//System.out.println("too close"+ sonicSensor.getDistance());
		}
		while(doAlign(start_time,time_limit)&&upper_border <= sonicSensor.getDistance()){
			Motor.A.setSpeed(500);
			Motor.C.setSpeed(900);
			Motor.A.forward();
			Motor.C.forward();
			//pilot.steer(SOFT_STEER);
			//System.out.println("too far"+ sonicSensor.getDistance());
		}
		
}
	
	public void setSuppressed(boolean suppressed) {

		this.suppressed = suppressed;
	}
	
	public boolean isSuppressed() {
		return suppressed;
	}

	public void driveForward(int speed) {

		Motor.A.setSpeed(speed);
		Motor.C.setSpeed(speed);

		Motor.A.forward();
		Motor.C.forward();
	}

	public void driveBackward(int speed) {
		
		Motor.A.setSpeed(speed);
		Motor.C.setSpeed(speed);

		Motor.A.backward();
		Motor.C.backward();
	}

	public void driveStop() {  
		
		Motor.A.stop();
		Motor.C.stop();
	}
	//DifferentialPilot pilot
	public boolean line(){
		boolean on_line = false;
		if(lightSensor.getLightValue() > 50){
			System.out.println("gotlight");
			//pilot.setTravelSpeed(0);
			Motor.A.setSpeed(0);
			Motor.C.setSpeed(0);
			on_line = true;
			Values.Instance().setCallCodeReader(true);
			found_line = true;
			//suppressed = true;
		}
			return on_line;
		
	}
	
	private boolean doAlign(long time, int time_limit) {
		return (System.currentTimeMillis() - time) < time_limit;
	}
}
