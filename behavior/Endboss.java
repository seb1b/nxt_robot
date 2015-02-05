package behavior;

import utils.Controls;
import utils.Values;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;
import lejos.nxt.UltrasonicSensor;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.subsumption.Behavior;
import lejos.util.Delay;

public class Endboss implements Behavior{
	
	private TouchSensor touch_l;
	private TouchSensor touch_r;
	private UltrasonicSensor us;
	private Values values;
	private Controls controls;
	private DifferentialPilot pilot;
	
	private int counterUltraSonic;
	
	

	
	public Endboss(){
		
		values = Values.Instance();
		controls = Controls.Instance();
		pilot = values.getPilot();
		touch_l = new TouchSensor(SensorPort.S1);
		touch_r = new TouchSensor(SensorPort.S4);
		us = new UltrasonicSensor(SensorPort.S2);
			
	}
	


	@Override
	public boolean takeControl() {
		if(Values.Instance().getScenario() == 10){
			return true;
		}
		return false;
	}
	

	
	public int ultraSonicOn(){
		int distance;
		if (counterUltraSonic > 15){
			counterUltraSonic = 0;
		} counterUltraSonic ++;
			us.setContinuousInterval(counterUltraSonic);
			distance = us.getDistance();
		return distance;
	}
	
	

	@Override
	public void action() {
		System.out.println("S: Endboss");
		int ultraSonic;
		//Motor.B.rotate(-90);
		while(!touch_r.isPressed()){
			ultraSonic = ultraSonicOn();
			
			pilot.steer(-20);
			
		} if(!(touch_r.isPressed() && touch_l.isPressed())){
			pilot.steer(80);
			Delay.msDelay(500);
			ultraSonic = ultraSonicOn();
		}
		
		if(touch_r.isPressed() && touch_l.isPressed()){
			pilot.rotate(90);
		}

	}

	@Override
	public void suppress() {
		// TODO Auto-generated method stub
		
	}
	
	
	
	
	
	
	

}
