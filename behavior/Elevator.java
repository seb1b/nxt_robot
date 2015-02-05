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
	private DifferentialPilot pilot;
	
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
		
		 pilot = Values.Instance().getPilot();
		 pilot.setTravelSpeed(5);
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

    
	@Override
	public void action() {

		
			System.out.println("elevator start");
//			//moveForward();
//			//stop();
//			
//			/* wir sind shcon verbunden, daher wird di platte grün sein für uns 
//			 * fahre also auf die platte drauf*/

			while (!BluetoothTest.openConnection(BluetoothTest.LIFT)) {
				BluetoothTest.sleep(1000); // waiting for free connection
			}
		

				/*auf der platte, fahre bis wir kein grün mehr sehen*/

				while(!touch_l.isPressed())
				{
					
					if(touch_r.isPressed()){
						//System.out.println("")
						pilot.stop();
						pilot.backward();
						Delay.msDelay(100);
						pilot.rotate(-10);
					}
					
					if(us.getDistance() < 9){
						//System.out.println("steer");
						pilot.steer(40);
						//continue;
				} else{
					//System.out.println("stop");
						//Fall tritt wsh nie ein
						pilot.stop();
						pilot.rotate(-10);
						//continue;
				} 
						
				}
				
				/* *********************im eingangsbereich*********************************** */
				System.out.println("eingangsbereich");
				pilot.stop();
				Delay.msDelay(1000);
				Motor.B.rotate(100); 		//rotiere sensor wieder seitlich
				pilot.stop();
				pilot.backward();
				Delay.msDelay(300);
				pilot.stop();
				pilot.rotate(-40); 
				System.out.println("ausgerichtet");
					//while(!touch_l.isPressed() || !touch_r.isPressed() ){ && !touch_r.isPressed() 
					while(!touch_l.isPressed()){
						System.out.println("auf gehts");
						if(touch_r.isPressed()){
							pilot.stop();
							pilot.backward();
							Delay.msDelay(100);
							pilot.rotate(10);
						}

						if(us.getDistance() > 5){	
							//zu weit we, also bewege dich nach links
							System.out.println("Distanz: " + us.getDistance() );
							pilot.steer(40);
							
								continue;
						} if(us.getDistance() <= 5){
								//Fall tritt wsh nie ein	//zu weit dran, also bewege dich etwas weg
							pilot.stop();//zu nah, also bewege dich nach rechts
							pilot.rotate(-10);
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
				
				ls.setFloodlight(true);
				pilot.forward();
				
				BluetoothTest.closeConnection();
				Values.Instance().incScenario();
				suppressed = true;
				suppress();
				
				
				
				
	}




@Override
public void suppress() {
					//System.out.println("S: bridge done");
					
					pilot.stop();
			  		
					
}
			

	
	
	
	


	
	

}
