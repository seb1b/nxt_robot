package behavior;

import utils.Values;
import utils.BluetoothTest;
import lejos.nxt.LCD;
import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;
import lejos.nxt.UltrasonicSensor;
import lejos.robotics.subsumption.Behavior;

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
	
	private void setLightOver(){
		if(ls.getLightValue() == 16){
			lightOver = false;
		}
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
	@Override
	public void action() {
		// TODO Auto-generated method stub
		
			System.out.println("elevator start");
			//moveForward();
			//stop();
			
			/* wir sind shcon verbunden, daher wird di platte grün sein für uns 
			 * fahre also auf die platte drauf*/
			while(!Values.Instance().getElevatorGreen(ls)){
				System.out.println(ls.getLightValue());
				while(us.getDistance() < 9){
					moveForward();
				} while(us.getDistance() > 9){
					//Fall tritt wsh nie ein
					moveRight();
				}
			}
		

				/*auf der platte, fahre bis wir kein grün mehr sehen*/

				while(Values.Instance().getElevatorGreen(ls))
				{
					if(us.getDistance() < 9){
						moveForward();
						continue;
				} if(us.getDistance() > 9){
						//Fall tritt wsh nie ein
						moveRight();
				continue;
				} 
						
				}
				
				/*im eingangsbereich*/
				Motor.B.rotate(70); 		//rotiere sensor wieder seitlich
				
					while(!touch_l.isPressed() || !touch_r.isPressed() ){
						System.out.println("auf gehts");
						
						 if(touch_l.isPressed()){
//							moveBackward();
//							BluetoothTest.sleep (200);
							moveRight();
							BluetoothTest.sleep (100);
							continue;
						} if(touch_r.isPressed()){
//							moveBackward();
//							BluetoothTest.sleep (200);
							moveLeft();
							BluetoothTest.sleep (100);
							continue;
						}
						if(us.getDistance() > 9){			//zu weit we, also bewege dich nach links
							moveLeft();	
							moveForward();
								continue;
						} if(us.getDistance() <= 9){
								//Fall tritt wsh nie ein	//zu weit dran, also bewege dich etwas weg
								moveRight();
								moveForward();
						continue;
						} 

	}
			
					/*im aufzug*/
					
					stop();
					
					/* verbinde mit bluetooth */
					while (!BluetoothTest.openConnection(BluetoothTest.LIFT)) {
						BluetoothTest.sleep(1000); // waiting for free connection
					}
					
					BluetoothTest.sleep(100);	//warte kurz im aufzug
					System.out.println("IM AUFZUG");
			
					
					BluetoothTest.goDown();

			LCD.drawString("Going down", 0, 1);

			while (!BluetoothTest.canExit()) {
				LCD.drawString("Can exit: No", 0, 2);
				BluetoothTest.sleep(100);

			}
			LCD.drawString("Can exit: Yes", 0, 2);
			
			/*wir sind draußen*/
			
			
			while((!touch_l.isPressed() || !touch_r.isPressed()) && (us.getDistance()<20)){
				System.out.println("auf gehts");
				
				 if(touch_l.isPressed()){
					moveBackward();
					BluetoothTest.sleep (100);
					moveRight();
					BluetoothTest.sleep (50);
					continue;
				} if(touch_r.isPressed()){
					moveBackward();
					BluetoothTest.sleep (100);
					moveLeft();
					BluetoothTest.sleep (50);
					continue;
				}
				if(us.getDistance() > 9){			//zu weit we, also bewege dich nach links
					moveLeft();	
					moveForward();
						continue;
				} if(us.getDistance() <= 9){
						//Fall tritt wsh nie ein	//zu weit dran, also bewege dich etwas weg
						moveRight();
						moveForward();
				continue;
				} 
			}		
				
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
