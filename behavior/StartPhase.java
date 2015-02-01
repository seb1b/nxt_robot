package behavior;


import utils.Values;
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


public class StartPhase implements Behavior {
	private boolean suppressed = false;
	
	UltrasonicSensor sonicSensor;
	DifferentialPilot pilot;
	private static int LOWER_BOARDER = 10;
	private static int UPPER_BOARDER = 14;
	private static int NO_WALL = 60;
	private static int HARD_STEER = 75;
	private static int SOFT_STEER = 40;
	TouchSensor touch_l;
	TouchSensor touch_r;
	LightSensor lightSensor;
	private Values value = Values.Instance();
	
	public StartPhase() {

		sonicSensor = new UltrasonicSensor(SensorPort.S2);
		lightSensor = new LightSensor(SensorPort.S3);
		touch_l = new TouchSensor(SensorPort.S1);
		touch_r = new TouchSensor(SensorPort.S4);
		pilot = new DifferentialPilot(1.3f, 3.94f, Motor.A, Motor.C, false); 
		pilot.setTravelSpeed(10);

	}

	public boolean takeControl() {

		if(value.getScenario() == 0){
			return true;
		}else {
			return false;
		}

	}

	public void action() {
		System.out.println("S: Startphase");
		
		//long timeStart=System.currentTimeMillis();
	//	System.out.println("ROTATE"+pilot.getRotateSpeed());
	//	Delay.msDelay(2000);
		if(!value.isStartphaseRunning()){
			value.setStartphaseRunning(true);
		while(!suppressed){
			
			while(!contact() && LOWER_BOARDER < sonicSensor.getDistance() && UPPER_BOARDER > sonicSensor.getDistance()){
				pilot.forward();
				System.out.println("good Distance"+ sonicSensor.getDistance());
				
				
			}
			while(!contact() && LOWER_BOARDER >+sonicSensor.getDistance()){
				pilot.steer(-SOFT_STEER);
				System.out.println("too close"+ sonicSensor.getDistance());
			}
			while(!contact() && UPPER_BOARDER <= sonicSensor.getDistance()&&NO_WALL >= sonicSensor.getDistance()){
				pilot.steer(SOFT_STEER);
				System.out.println("too far"+ sonicSensor.getDistance());
			}
			
			while(!contact() && NO_WALL <= sonicSensor.getDistance()){
				pilot.steer(HARD_STEER);
				System.out.println("no wall"+ sonicSensor.getDistance());
			}
			
			if(contact()){
				System.out.println("touch");
				//timeStart=System.currentTimeMillis();
				//System.out.println("too far"+ sonicSensor.getDistance());
				//pilot.quickStop();
				pilot.backward();
				Delay.msDelay(500);
				pilot.rotate(-180);
				
			}
			
			if(lightSensor.getLightValue() > 50){
				System.out.println("gotlight");
				pilot.stop();
				Values.Instance().setCallCodeReader(true);
				suppressed = true;
			}
			
		}
		}
			
			
			
				
				
			
			
	}


		
	boolean contact(){
		return (touch_l.isPressed()||touch_r.isPressed()) ;
		
	}


	

	public void suppress() {
		System.out.println("suppress start phase");
		suppressed = true;
	}


}
