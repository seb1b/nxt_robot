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
	DifferentialPilot pilot;
	private static int LOWER_BORDER = 14;
	private static int UPPER_BORDER = 17;
	private static int NO_WALL = 70;
	private static int HARD_STEER = 70;
	private static int SOFT_STEER = 45;
	TouchSensor touch_l;
	TouchSensor touch_r;
	private LightSensor ls;

	private Values value = Values.Instance();
	
	public StartPhase() {

		sonicSensor = new UltrasonicSensor(SensorPort.S2);
		touch_l = new TouchSensor(SensorPort.S1);
		touch_r = new TouchSensor(SensorPort.S4);
		pilot = Values.Instance().getPilot();
		ls = new LightSensor(SensorPort.S3);
		//pilot.setTravelSpeed(30);
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
		System.out.println("S: Startphase" + value.getScenario());
		
		boolean contact = false;
		boolean lineFound = false;
		int distance = 9999;
		
		pilot.setTravelSpeed(25);
		
	
		if(!value.isStartphaseRunning()){
			value.setStartphaseRunning(true);
		
		
		while(!suppressed) {
			contact = contact();
			distance = sonicSensor.getDistance();
			lineFound = foundLine();
			//System.out.println(distance);
			
			
			
			if(contact) {
				pilot.stop();
				
				pilot.backward();
				Delay.msDelay(200);
				pilot.rotate(-90);
				
				continue;
			}
				
			if(lineFound) {
				//Values.Instance().setCallCodeReader(true);
				pilot.forward();
				Delay.msDelay(700);
				
				pilot.stop();
				value.incScenario();
				//suppressed = true;
				suppress();
				//continue;
			}
			
			// Good distance
			if(LOWER_BORDER < distance && UPPER_BORDER > distance) {
				pilot.forward();
				continue;
			}
			
			// Too far
			if(LOWER_BORDER >= distance) {
				pilot.steer(-SOFT_STEER);
				continue;
			}
						
			// Too near
			if(UPPER_BORDER <= distance && NO_WALL >= distance) {
				pilot.steer(SOFT_STEER);
				continue;
			}
			
			if(NO_WALL <= distance)  {
				pilot.steer(HARD_STEER);
				continue;
			}
		}
	}	
	}
	

		
	boolean contact(){
		return (touch_l.isPressed()||touch_r.isPressed()) ;
		
	}


	

	public void suppress() {
		
		suppressed = true;
		System.out.println("suppress start phase");
		
	}

	
	private boolean foundLine(){
		boolean on_line = false;

		if(ls.getLightValue() > 58){

			System.out.println("gotlight");
			on_line = true;
		}
			return on_line;
		
	}

}
