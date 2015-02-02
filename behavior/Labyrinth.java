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


public class Labyrinth implements Behavior {
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
	Controls control;
	
	
	public Labyrinth() {
		control = Controls.Instance();
		sonicSensor = new UltrasonicSensor(SensorPort.S2);
		touch_l = new TouchSensor(SensorPort.S1);
		touch_r = new TouchSensor(SensorPort.S4);
		pilot = new DifferentialPilot(1.3f, 3.94f, Motor.A, Motor.C, false); 

	}

	public boolean takeControl() {

		if(Values.Instance().getScenario() == 5){
			return true;
		}
		return false;
	}

	public void action() {
		System.out.println("S: Labyrinth");
		
		pilot.setTravelSpeed(7);
		//long timeStart=System.currentTimeMillis();
		while(!suppressed){
			
			while(!control.line() && !contact() && LOWER_BOARDER < sonicSensor.getDistance() && UPPER_BOARDER > sonicSensor.getDistance()){
				pilot.forward();
				System.out.println("good Distance"+ sonicSensor.getDistance());
				
				
			}
			while(!control.line()&& !contact() && LOWER_BOARDER >=sonicSensor.getDistance()){
				pilot.steer(-SOFT_STEER);
				System.out.println("too close"+ sonicSensor.getDistance());
			}
			while(!control.line()&& !contact() && UPPER_BOARDER <= sonicSensor.getDistance()&&NO_WALL >= sonicSensor.getDistance()){
				pilot.steer(SOFT_STEER);
				System.out.println("too far"+ sonicSensor.getDistance());
			}
			
			while(!control.line()&&!contact() && NO_WALL <= sonicSensor.getDistance()){
				pilot.steer(HARD_STEER);
			//	System.out.println("no wall"+ sonicSensor.getDistance());
			}
			
			if(contact()){
				//timeStart=System.currentTimeMillis();
				//System.out.println("too far"+ sonicSensor.getDistance());
				//pilot.quickStop();
				pilot.backward();
				Delay.msDelay(500);
				pilot.rotate(-180);
				System.out.println("touch");
			}
			
		}
			
	}


		
	boolean contact(){
		return (touch_l.isPressed()||touch_r.isPressed()) ;
		
	}


	

	public void suppress() {
		suppressed = true;
	}


}
