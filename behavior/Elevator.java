package behavior;

import utils.Values;
import utils.BluetoothTest;
import lejos.nxt.LCD;
import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;
import lejos.nxt.UltrasonicSensor;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.subsumption.Behavior;
import lejos.util.Delay;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import javax.bluetooth.RemoteDevice;

import lejos.nxt.LCD;
import lejos.nxt.comm.BTConnection;
import lejos.nxt.comm.Bluetooth;

public class Elevator implements Behavior{
	UltrasonicSensor us;
	static TouchSensor touch_l;
	static TouchSensor touch_r;
	LightSensor ls;
	private Values value;
	private boolean lightOver;
	private boolean suppressed = false;
	
	private boolean connected = false;
	
	private void setLightOver(){
		if(ls.getLightValue() == 16){
			lightOver = false;
		}
	}
	
	
	
	public boolean getIsPressed(){
		boolean isPressed = false;
		if(touch_l.isPressed() || touch_r.isPressed()){
			isPressed = true;
		}
		return isPressed;
	}

	
	
	public Elevator(){
		
		 us = new UltrasonicSensor(SensorPort.S2);
		 touch_l = new TouchSensor(SensorPort.S1);
		 touch_r = new TouchSensor(SensorPort.S4); 
		 ls = new LightSensor(SensorPort.S3);
		 value = Values.Instance();

	}	
	
	
	
    public boolean takeControl() {

    	
  		if(value.getScenario() == 3){
  			//System.out.println(" foloow line"+value.getScenario());
  			return true;
  		}else{
  			return false;
  		}
  		//return true;


  	}
		 
		 /*
			while(true){
				
				if(!touch_l.isPressed() || !touch_r.isPressed() ){
					System.out.println("min einer gedrückt");
				}else{
					System.out.println("beide gedrückt");
					
				}
			}
			*/
    
    public void rotateRight(int degrees){
    	Motor.C.rotate(degrees);
    	Motor.A.setSpeed(200);
    	Motor.C.setSpeed(200);
    	Delay.msDelay(200);

    	
    }
    
    /*
    public static void main(String[] args){
    	//Elevator el = new Elevator();
    	//DifferentialPilot pilot = Values.Instance().getPilot();
    	//pilot.rotate(180);
    	LightSensor ls = new LightSensor(SensorPort.S3); //30 als Schwellwert passt
    	
    }*/
    
	@Override
	public void action() {

		
		
		
		
		
		DifferentialPilot pilot = Values.Instance().getPilot();
		
//		pilot.forward();
//		pilot.stop();
//		pilot.rotate(-20);
//		Motor.B.rotate(-90);
//		Delay.msDelay(1000);
//		Motor.B.rotate(90);
//		Delay.msDelay(1000);
		
		
			System.out.println("elevator start");
//			//moveForward();
//			//stop();
//			
//			/* wir sind shcon verbunden, daher wird di platte grün sein für uns 
//			 * fahre also auf die platte drauf*/
//			while(!Values.Instance().getElevatorGreen(ls)){
//				System.out.println(ls.getLightValue());
//				while(us.getDistance() < 9){
//					moveForward();
//				} while(us.getDistance() > 9){
//					//Fall tritt wsh nie ein
//					moveRight();
//				}
//			}
			
			while (!BluetoothTest.openConnection(BluetoothTest.LIFT)) {
				BluetoothTest.sleep(1000); // waiting for free connection
			}
		

				/*auf der platte, fahre bis wir kein grün mehr sehen*/

				while(!getIsPressed())
				{
					if(us.getDistance() < 9){
						pilot.steer(20);
						continue;
				} if(us.getDistance() >= 9){
						//Fall tritt wsh nie ein
						pilot.stop();
						pilot.rotate(-10);
				continue;
				} 
						
				}
				
				/* *********************im eingangsbereich*********************************** */

				pilot.stop();
				Delay.msDelay(1000);
				Motor.B.rotate(90); 		//rotiere sensor wieder seitlich
				pilot.stop();
				pilot.backward();
				Delay.msDelay(1000);
				pilot.stop();
				pilot.rotate(-60); 
				
					//while(!touch_l.isPressed() || !touch_r.isPressed() ){
					while(!touch_l.isPressed() && !touch_r.isPressed() ){
						System.out.println("auf gehts");
						
						/*
						 if(touch_l.isPressed()){
							moveBackward();
							Motor.A.setSpeed(200);
							Motor.C.setSpeed(200);
							Delay.msDelay(100); //TODO Wert gut?
//							BluetoothTest.sleep (200);
							//moveRight();
							pilot.rotate(-40);
							//BluetoothTest.sleep (100);
							continue;
						} if(touch_r.isPressed()){
							moveBackward();
							Motor.A.setSpeed(200);
							Motor.C.setSpeed(200);
							Delay.msDelay(100);
//							BluetoothTest.sleep (200);
							//moveLeft();
							pilot.rotate(40);
							//BluetoothTest.sleep (100);
							continue;
						}*/
						if(us.getDistance() > 5){	
							//zu weit we, also bewege dich nach links
							System.out.println("Distanz: " + us.getDistance() );
							pilot.steer(20);
							
								continue;
						} if(us.getDistance() <= 5){
								//Fall tritt wsh nie ein	//zu weit dran, also bewege dich etwas weg
							pilot.stop();//zu nah, also bewege dich nach rechts
							pilot.rotate(-20);
							System.out.println("Distanz: " + us.getDistance() );
						continue;
						} 
	}
			
					/*im aufzug*/
					
					pilot.stop();
					
					/* verbinde mit bluetooth */

					
					BluetoothTest.sleep(100);	//warte kurz im aufzug
					System.out.println("IM AUFZUG");
			
					
					BluetoothTest.goDown();

			LCD.drawString("Going down", 0, 1);
 
			while (!BluetoothTest.canExit()) {
				LCD.drawString("Can exit: No", 0, 2);
				BluetoothTest.sleep(100);

			}
			LCD.drawString("Can exit: Yes", 0, 2);
			
			/* ***********************wir sind draußen **************************** */
			
			
			
//			while((!touch_l.isPressed() || !touch_r.isPressed()) && (us.getDistance()<20)){
//				System.out.println("auf gehts");
//				
//				 if(touch_l.isPressed()){
//					moveBackward();
//					BluetoothTest.sleep (100);
//					moveRight();
//					BluetoothTest.sleep (50);
//					continue;
//				} if(touch_r.isPressed()){
//					moveBackward();
//					BluetoothTest.sleep (100);
//					moveLeft();
//					BluetoothTest.sleep (50);
//					continue;
//				}
//				if(us.getDistance() > 9){			//zu weit we, also bewege dich nach links
//					moveLeft();	
//					moveForward();
//						continue;
//				} if(us.getDistance() <= 9){
//						//Fall tritt wsh nie ein	//zu weit dran, also bewege dich etwas weg
//						moveRight();
//						moveForward();
//				continue;
//				} 
//			}		
			
				pilot.forward();
				
				BluetoothTest.closeConnection();
				
				suppress();
				
				
				
	}




				@Override
				public void suppress() {
					//System.out.println("S: bridge done");
					Values.Instance().incScenario();
					Motor.A.setSpeed(0);
					Motor.C.setSpeed(0);
			  		suppressed = true;
					
				}
			

	
	
	
	
	private static void moveForward(){
		Motor.C.setSpeed(500);
		Motor.C.forward();
		Motor.A.setSpeed(300);
		Motor.A.forward();
	}
	
	public static void stop(){
		Motor.C.setSpeed(0);
		
		Motor.A.setSpeed(0);
		
	}
	
	private static void moveBackward(){
		Motor.C.backward();
		//Motor.A.setSpeed(300);
		Motor.A.backward();
	}
	
	
	
	private static void moveRight(){
		Motor.C.backward();
		//Motor.A.setSpeed(300);
		Motor.A.forward();
	}
	
	private static void moveLeft(){
		Motor.C.forward();
		//Motor.A.setSpeed(300);
		Motor.A.backward();
	}
	
	


	
	

}
