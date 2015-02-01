package behavior;

import utils.Values;
import lejos.util.*;
import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import lejos.robotics.subsumption.Behavior;

public class PlankBridge implements Behavior {
	LightSensor ls;
	UltrasonicSensor us;
	long zstVorher;
	long zstNachher;
	boolean endOfBridge=false;
	
	int speedFactor = 2; //2: Maximale Geschwindigkeit, 1: Halbe Geschwindigkeit
	
	private int treshold = 10;
	
	public PlankBridge(){
		//ls = new LightSensor(SensorPort.S4);
		us = new UltrasonicSensor(SensorPort.S2);
	}
	

	public boolean takeControl() {
		if(Values.Instance().getScenario() == 7){
			return true;
		}
		return false;
	}
	public void action() {
		
		System.out.println("S: PlankBridge");
		
		Motor.B.rotate(-90);
		leftCurve(1000/speedFactor);
		int counterAbgrund = 0;
		//int counterDrehung = 0;
		//int timesAdjusted = 0;
		while(true){	
			while(us.getDistance() < treshold){
				counterAbgrund = 0;
				moveForward();
			} while(us.getDistance() > treshold && counterAbgrund <= 10){ //TODO rausfinden: ist Wert von 10 gut? 
				counterAbgrund ++;
			}
			while(us.getDistance() > treshold && counterAbgrund >10 )
					//&& 
					//(counterDrehung < (2*treshold)) && timesAdjusted < 4){ //TODO Wert rausfinden für timesAdjusted
																		//und CounterDrehung
				//counterDrehung ++;
				moveRight();
			} //timesAdjusted ++;
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

	}

}


