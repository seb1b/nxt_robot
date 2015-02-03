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

public class Bridge implements Behavior {
	boolean suppressed = false;
	LightSensor ls;
	UltrasonicSensor us;
	TouchSensor touch_l;
	TouchSensor touch_r;
	long zstVorher;
	long zstNachher;
	boolean rampe=false;
	
	int speedFactor = 2; //2: Maximale Geschwindigkeit, 1: Halbe Geschwindigkeit
	
	private int treshold = 15;
	
	public Bridge(){
		ls = new LightSensor(SensorPort.S3);
		us = new UltrasonicSensor(SensorPort.S2);
		touch_l = new TouchSensor(SensorPort.S1);
		touch_r = new TouchSensor(SensorPort.S4);
	}
	
	
	public boolean getIsPressed(){
		boolean isPressed = false;
		if(touch_l.isPressed() || touch_r.isPressed()){
			isPressed = true;
		}
		return isPressed;
	}
	
	

	public boolean takeControl() {
		if(Values.Instance().getScenario() == 2){
			return true;
		}
		return false;
	}

	public void action() {
		
		System.out.println("S: Bridge");
		
		Motor.B.rotate(-90);
		DifferentialPilot pilot = Values.Instance().getPilot();
		float distance = 0;
		pilot.setTravelSpeed(30);
		leftCurve(1000/speedFactor);
		while(dark()){ //nicht auf der Lichtkachel 
			distance = us.getDistance();  //hole neuen wert vom sonar
			
			while(getIsPressed()){
				pilot.stop();
				
				//				Motor.A.stop();
//				Motor.C.stop();
			}
			
				if(distance < treshold){
					pilot.steer(50);
					continue;
			} if(distance >= treshold){
					//Fall tritt wsh nie ein
					pilot.stop();
					pilot.rotate(-20);
			continue;
			} 
			
//			while(us.getDistance() < treshold && !getIsPressed()){
//				moveForward();
//			} while(us.getDistance() > treshold && !getIsPressed()){
//				/*if(treshold>30){ //Rampe wurde hochgefahren. Nötig, um Spalt zu überfahren
//					treshold = 30;
//				}*/
//				moveRight();
//			}
		}
		suppress();
	}
	
	private boolean dark(){
		
		boolean dark = true;
		ls.setFloodlight(false);
		System.out.println(ls.getLightValue());
		if(ls.getLightValue() >50 ){
			dark = false;
			
		}
		return dark;
	}
	
	public void leftCurve(long delay){
		Motor.C.setSpeed(400*speedFactor);
		Motor.C.forward();
		Motor.A.setSpeed(100*speedFactor);
		Motor.A.forward();
		Delay.msDelay(delay);
		
	}
	

	
	
	public void moveForward(){
		Motor.C.setSpeed(500);
		Motor.C.forward();
		Motor.A.setSpeed(300);
		Motor.A.forward();
	}
	
	public void moveRight(){
		Motor.C.backward();
		//Motor.A.setSpeed(300);
		Motor.A.forward();
	}
	
		
	

	public void suppress() {
		System.out.println("S: bridge done");
		Values.Instance().incScenario();
		Motor.A.setSpeed(0);
		Motor.C.setSpeed(0);
  		suppressed = true;

	}

}


