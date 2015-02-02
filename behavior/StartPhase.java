package behavior;


import utils.Controls;
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
	//DifferentialPilot pilot;
	private static int LOWER_BOARDER = 13;
	private static int UPPER_BOARDER = 15;
	private static int NO_WALL = 60;
	private static int HARD_STEER = 75;
	private static int SOFT_STEER = 60;
	TouchSensor touch_l;
	TouchSensor touch_r;
	Controls control;

	private Values value = Values.Instance();
	
	public StartPhase() {

		sonicSensor = new UltrasonicSensor(SensorPort.S2);
		control = Controls.Instance();
		touch_l = new TouchSensor(SensorPort.S1);
		touch_r = new TouchSensor(SensorPort.S4);
		//pilot = new DifferentialPilot(1.3f, 3.94f, Motor.A, Motor.C, false); 
		//pilot.setTravelSpeed(10);
		//pilot.setRotateSpeed(50);

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
			Motor.A.setSpeed(900);
			Motor.C.setSpeed(900);
			while(!control.line() && !contact() && LOWER_BOARDER < sonicSensor.getDistance() && UPPER_BOARDER > sonicSensor.getDistance()){
				//pilot.forward();
				Motor.A.setSpeed(900);
				Motor.C.setSpeed(900);
				Motor.A.forward();
				Motor.C.forward();
				//System.out.println("good Distance"+ sonicSensor.getDistance());
				
				
			}
			while(!control.line() && !contact() && LOWER_BOARDER >=sonicSensor.getDistance()){
				Motor.A.setSpeed(900);
				Motor.C.setSpeed(500);
				Motor.A.forward();
				Motor.C.forward();
				//pilot.steer(-SOFT_STEER);
				//System.out.println("too close"+ sonicSensor.getDistance());
			}
			while(!control.line() && !contact() && UPPER_BOARDER <= sonicSensor.getDistance() && NO_WALL >= sonicSensor.getDistance()){
				Motor.A.setSpeed(500);
				Motor.C.setSpeed(900);
				Motor.A.forward();
				Motor.C.forward();
				//pilot.steer(SOFT_STEER);
				//System.out.println("too far"+ sonicSensor.getDistance());
			}
			
			while(!control.line() &&!contact() && NO_WALL <= sonicSensor.getDistance()){
				Motor.A.setSpeed(100);
				Motor.C.setSpeed(900);
				//pilot.steer(HARD_STEER);
			//	System.out.println("no wall"+ sonicSensor.getDistance());
			}
			
			if(control.found_line){
				suppressed = true;
			}
			
			
			
			if(contact()){
			//	System.out.println("touch");
				//timeStart=System.currentTimeMillis();
				//System.out.println("too far"+ sonicSensor.getDistance());
				//pilot.stop();
				Motor.A.setSpeed(0);
				Motor.C.setSpeed(0);
				Motor.A.backward();
				Motor.C.backward();
				Delay.msDelay(200);
				Motor.A.forward();
				Motor.C.backward();
				//Delay.msDelay(500);
				//pilot.backward();
				Delay.msDelay(600);
				//pilot.rotate(-180);
				
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
