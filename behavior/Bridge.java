import lejos.util.*;
import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import lejos.robotics.subsumption.Behavior;

public class Bridge implements Behavior {
	LightSensor ls;
	UltrasonicSensor us;
	long zstVorher;
	long zstNachher;
	boolean rampe=false;
	
	int speedFactor = 2; //2: Maximale Geschwindigkeit, 1: Halbe Geschwindigkeit
	
	private int treshold = 10;
	
	public Bridge(){
		//ls = new LightSensor(SensorPort.S4);
		us = new UltrasonicSensor(SensorPort.S2);
	}
	

	public boolean takeControl() {
		return true;
	}

	public void action() {
		
		Motor.B.rotate(-90);
		leftCurve(1000/speedFactor);
		while(true){			
			while(us.getDistance() < treshold){
				moveForward();
			} while(us.getDistance() > treshold){
				if(treshold>30){ //Rampe wurde hochgefahren. Nötig, um Spalt zu überfahren
					treshold = 30;
				}
				moveRight();
			}
		}
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


