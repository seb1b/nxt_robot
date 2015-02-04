package behavior;


import utils.Controls;
import utils.Values;
import lejos.nxt.Button;
import lejos.nxt.ColorSensor;
import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;
import lejos.nxt.UltrasonicSensor;
import lejos.nxt.comm.Bluetooth;
import lejos.nxt.comm.NXTConnection;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.subsumption.Behavior;
import lejos.util.Delay;
import lejos.util.NXTDataLogger;
import lejos.util.PIDController;



public class FollowLine2ndPart implements Behavior {

	private boolean suppressed = false;
	private DifferentialPilot pilot;
	//private PIDController pid;
	private LightSensor detector;
	int start_run = 0;
	private Values value = Values.Instance();
	TouchSensor touch_l;
	TouchSensor touch_r;
	UltrasonicSensor sonicSensor;
	Controls control;
	private boolean final_part = false;
	private boolean justTurned=false;
	private int lastTurnDirection=1;
	
	public FollowLine2ndPart() {
		
	      //pilot = new DifferentialPilot(1.3f, 3.94f, Motor.A, Motor.C, false); 
		  pilot = value.getPilot();
	      pilot.setTravelSpeed(15);
	     // pilot.setRotateSpeed(70);
			touch_l = new TouchSensor(SensorPort.S1);
			touch_r = new TouchSensor(SensorPort.S4);
	      control = new Controls();
	      detector = new LightSensor(SensorPort.S3);
	      
	}

	public boolean getIsPressed(){
		boolean isPressed = false;
		if(touch_l.isPressed() || touch_r.isPressed()){
			isPressed = true;
		}
		return isPressed;
	}
      
    public boolean takeControl() {

    	
  		if(value.getScenario() == 6) {
  			suppressed =false;
  			return true;
  		}else if(value.getScenario() == 8){
  			final_part = true;
  			suppressed =false;
  			return true;
  		}else{
  			return false;
  		}
  	}

  	public void action() {
  		
  	  //System.out.println("S: Follow Line");
    
        //	suppress();
       
		boolean ON_LINE = true;
		
		//boolean LINE_RIGHT = false;
		int counter = 1;
		//int value = 0;
		boolean end_reached = false;
		boolean ramp_reached  = false;
		int factor = 30;
		int not_online_counter = 0;
		int ThisTurnDirection=1;
		
       while (!suppressed) {
    	  


        		
    	   while(getIsPressed()){
				pilot.stop();
   	   }

  		
       		if(online()){
       			if(justTurned){
       				justTurned=false;
       				Delay.msDelay(10);//kleines delay um wirklich in die linie hinein zu schwenken
       				pilot.stop();
       				
       			}
       			pilot.forward();
       			counter = 0;
       			ThisTurnDirection=lastTurnDirection;
       			not_online_counter = 0;
       		}else{
       			not_online_counter++;
       			System.out.println("counter ist bei " + not_online_counter);
       			if(not_online_counter >5)
       			/*if(!online()){		//zweiter check um wirklich von der linie weg zu sein
       			pilot.stop();
       			System.out.println("wirklich gestopped");
       			}		// wir sind wirklich weg
       			else
       			{continue;}	*/   		//letzte wahr fehlmessung
       			while(!online()){ 
       				justTurned=true;
       				//if(!pilot.isMoving()){        					
       					if(counter >5){
       						end_reached = true;
       						break;
       					}
       					//if(counter > 3){
       					//	factor = 30;
       					//}
       					
       					if(ThisTurnDirection==2){
    
       						lastTurnDirection=2;
       						ThisTurnDirection=1;
       						if(counter>3)
       							pilot.travel(1);	//	only move forward if value is high enough
       						pilot.rotate(60+(counter-1)*factor,true); //left  
       						while(pilot.isMoving()){
       							if(online()){
       								
       								
       								break;
       								
       							}
       						}
       						
       						
       					}else{       			
       						lastTurnDirection=1;
       						ThisTurnDirection=2;
       						pilot.rotate(-60-(counter-1)*factor,true);//right
       						while(pilot.isMoving()){
       							if(online()){					
       								break;       								
       							}
       						}
       					}        					
       					counter++;
       						
       				//}       				
       			}
        			
        			if(end_reached){
						System.out.println("end reached");
						if(lastTurnDirection ==1){
        					pilot.rotate((60+(counter-1)*factor)/2 + 5);
        				}else{
        					pilot.rotate((-60-(counter-1)*factor)/2 -5);
        					
        				}

						if(ramp_reached){
							suppress();
						}else{
							pilot.forward();
							Delay.msDelay(4000);
							pilot.stop();
							counter = 1;
							end_reached = false;
							ramp_reached = true;
						}
        			}
        			
        			
        		}
        		
        	}      		
}

  			

boolean online(){
	return detector.getLightValue()>55;
	
}
  		  	

  	public void suppress() {

  		pilot.stop();
  		System.out.println("looking for distance");
    	//control.alignUntilDistance(12, 15,40);
    	//Delay.msDelay(1500);;
    	value.incScenario();
  		suppressed = true;
  	}
} 