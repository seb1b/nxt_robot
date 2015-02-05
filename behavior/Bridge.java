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
	private DifferentialPilot pilot;
	
	
	public Bridge(){
		ls = new LightSensor(SensorPort.S3);
		us = new UltrasonicSensor(SensorPort.S2);
		touch_l = new TouchSensor(SensorPort.S1);
		touch_r = new TouchSensor(SensorPort.S4);
		pilot = Values.Instance().getPilot();
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
		
		float distance = 0;
		pilot.setTravelSpeed(30);
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
					pilot.backward();
					Delay.msDelay(100);
					pilot.rotate(-45);

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
		Values.Instance().incScenario();
  		suppressed = true;
		suppress();
	}
	
	private boolean dark(){
		
		boolean dark = true;
		ls.setFloodlight(false);
		//System.out.println(ls.getLightValue());
		if(ls.getLightValue() >50 ){
			dark = false;
			
		}
		return dark;
	}
	
	
		
	

	public void suppress() {
		System.out.println("S: bridge done");

		pilot.stop();


	}

}


