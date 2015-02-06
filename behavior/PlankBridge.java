package behavior;

import utils.Controls;
import utils.Values;
import lejos.util.*;
import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;
import lejos.nxt.UltrasonicSensor;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.subsumption.Behavior;

public class PlankBridge implements Behavior {
	
	private Values values;
	private Controls controls;
	private DifferentialPilot pilot;
	private TouchSensor touch_l;
	private TouchSensor touch_r;
	
	public PlankBridge(){
		values = Values.Instance();
		controls = Controls.Instance();
		pilot = values.getPilot();
		touch_l = new TouchSensor(SensorPort.S1);
		touch_r = new TouchSensor(SensorPort.S4);
	}
	

	public boolean takeControl() {
		if(Values.Instance().getScenario() == 6){
			return true;
		}
		return false;
	}
	
	public void action() {
		
		System.out.println("S: PlankBridge");
		
		//System.out.println("beforealign");
		//controls.alignUntilDistance(12, 14, 90, 12);
		controls.alignForTime(12, 14, 4800, 15);
		//System.out.println("afteralign");
		
		
		pilot.stop();
		pilot.rotate(1);
		pilot.setTravelSpeed(18);
		
		long starttime = System.currentTimeMillis();
		int stoppedtime = 0;
		
		while((System.currentTimeMillis() - stoppedtime - starttime) < 13000) {
			
			if(getIsPressed()){
				pilot.stop();
				stoppedtime +=2100;
				//System.out.println("+500");
				Delay.msDelay(1500);
			}
			
			//System.out.println("TimeLeft: "+(13000 - (System.currentTimeMillis() - stoppedtime - starttime)));
			
			pilot.forward();
		}
		
		pilot.stop();
		System.out.println("Inc");
		values.incScenario();
	
	}
		
	public void suppress() {

	}
	
	public boolean getIsPressed(){
		boolean isPressed = false;
		if(touch_l.isPressed() || touch_r.isPressed()){
			isPressed = true;
		}
		return isPressed;
	}
}