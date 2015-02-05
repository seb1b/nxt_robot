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


public class Quali_Labyrinth implements Behavior {
	private boolean suppressed = false;
	
	UltrasonicSensor sonicSensor;
	DifferentialPilot pilot;
	Values value;
	private static int LOWER_BORDER = 12;
	private static int UPPER_BORDER = 15;
	private static int NO_WALL = 45;
	private static int HARD_STEER = 70;
	private static int SOFT_STEER = 30;
	TouchSensor touch_l;
	TouchSensor touch_r;
	private LightSensor ls;
	
	public Quali_Labyrinth() {
		value = Values.Instance();
		sonicSensor = new UltrasonicSensor(SensorPort.S2);
		touch_l = new TouchSensor(SensorPort.S1);
		touch_r = new TouchSensor(SensorPort.S4);
		pilot = value.getPilot();
		//control = Controls.Instance();
		ls = new LightSensor(SensorPort.S3);
	}

	public boolean takeControl() {

		if(Values.Instance().getScenario() == 12){
			//System.out.println("Construc Labyrinth " +Values.Instance().getScenario());
			return true;
		}
		return false;
	}

	public void action() {
		System.out.println("S: Labyrinth " + Values.Instance().getScenario());
			
		boolean contact = false;
		int distance = 9999;
		
		pilot.setTravelSpeed(25);
		
		while(!suppressed) {
			contact = contact();
			distance = sonicSensor.getDistance();

			//System.out.println(distance);
			
			
			if(contact) {
				pilot.stop();
				
				pilot.backward();
				Delay.msDelay(200);
				pilot.rotate(-90);
				
				continue;
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


		
	boolean contact(){
		return (touch_l.isPressed()||touch_r.isPressed()) ;
		
	}

	private boolean foundLine(){
		boolean on_line = false;

		if(ls.getLightValue() > 58){

			System.out.println("gotlight");
			on_line = true;
		}
			return on_line;
		
	}
	

	public void suppress() {
		suppressed = true;
	}


}
