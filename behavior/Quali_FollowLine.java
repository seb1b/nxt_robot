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



public class Quali_FollowLine implements Behavior {

	private boolean suppressed = false;
	private DifferentialPilot pilot;
	private LightSensor detector;
	int start_run = 0;
	private Values value;
	TouchSensor touch_l;
	TouchSensor touch_r;
	UltrasonicSensor sonicSensor;
	int initialTurn = 60;
	int turnSpeed = 60;
	//Controls control;
	private int lastTurnDirection=1;
	private boolean justTurned=false;
	
	public Quali_FollowLine() {
		  value = Values.Instance();
		  pilot = value.getPilot();
	      pilot.setTravelSpeed(20);
	      pilot.setRotateSpeed(turnSpeed);
	      
	      touch_l = new TouchSensor(SensorPort.S1);
			touch_r = new TouchSensor(SensorPort.S4);

	      detector = new LightSensor(SensorPort.S3);
	      
	}

	
      
    public boolean takeControl() {

    	
  		if(value.getScenario() == 11){
  			//System.out.println(" foloow line"+value.getScenario());
  			return true;
  		}else{
  			return false;
  		}
  	}

  	public void action() {
     
		boolean ON_LINE = true;
		
		//boolean LINE_RIGHT = false;
		int counter = 0;
		boolean end_reached = false;
		//int factor = 40;
		int not_online_counter = 0;
		int ThisTurnDirection=1;
		//System.out.println("started line follower");
		//int[] ret_turn = {0,40,100,170,110};
		//int[] turn = {40,60,70,50};
		int[] ret_turn = {0,50,150,100};
		int[] turn = {50,100,50};
		pilot.setTravelSpeed(22);
       while (!suppressed) {

   		
        		if(online()){
        			if(justTurned){
        				justTurned=false;
        				//Delay.msDelay(10);//kleines delay um wirklich in die linie hinein zu schwenken
        				pilot.stop();
        				
        			}
        			pilot.forward();
        			counter = 0;
        			ThisTurnDirection=lastTurnDirection;
        			not_online_counter = 0;
        		}else{
        			not_online_counter++;
        			System.out.println("counter ist bei " + not_online_counter);
        			if(not_online_counter >5){
        				pilot.stop();
        			
        			/*if(!online()){		//zweiter check um wirklich von der linie weg zu sein
        			pilot.stop();
        			System.out.println("wirklich gestopped");
        			}		// wir sind wirklich weg
        			else
        			{continue;}	*/   		//letzte wahr fehlmessung
        			while(!online()){ 
        				justTurned=true;
        				//if(!pilot.isMoving()){        					
        					if(counter >2){
        						end_reached = true;
        						break;
        					}
        					
        					if(ThisTurnDirection==2){
     
        						lastTurnDirection=2;
        						ThisTurnDirection=1;
        						pilot.setRotateSpeed(pilot.getRotateMaxSpeed());
        						pilot.rotate(ret_turn[counter],false);
        						pilot.setRotateSpeed(turnSpeed);
        						//	only move forward if value is high enough
        						pilot.rotate(turn[counter],true); //left  
        						while(pilot.isMoving()){
        							if(online()){
        								
        								
        								break;
        								
        							}
        						}
        						
        						
        					}else{       			
        						lastTurnDirection=1;
        						ThisTurnDirection=2;
        						pilot.setRotateSpeed(pilot.getRotateMaxSpeed());
        						pilot.rotate(-ret_turn[counter],false);
        						pilot.setRotateSpeed(turnSpeed);
        						pilot.rotate(-turn[counter],true);//right
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
        				pilot.setRotateSpeed(pilot.getRotateMaxSpeed());
        				if(lastTurnDirection ==1){
        					pilot.rotate(ret_turn[counter]);
        				}else{
        					pilot.rotate(-ret_turn[counter]);
        					
        				}
						//System.out.println("end reached");
						//while()
        				pilot.setTravelSpeed(pilot.getRotateMaxSpeed());
        				pilot.forward();
        				Delay.msDelay(1000);
						pilot.stop();
				    	value.incScenario();
				 		suppressed = true;
        				suppress();
        			}
        			}
        		}    		
        	}   		
}

  			


boolean online(){
	return detector.getLightValue()>55;
	
}


public boolean getIsPressed(){
	boolean isPressed = false;
	if(touch_l.isPressed() || touch_r.isPressed()){
		isPressed = true;
	}
	return isPressed;
}	  	

 public void suppress() {
  	System.out.println("suppressed follow line");
  		/*pilot.setTravelSpeed(30);
  		System.out.println(" Scenario"+value.getScenario());
  		while(!online()){
  			pilot.forward();
  		}
  		
  		detector.setFloodlight(false);
  		Delay.msDelay(2000);
  		pilot.stop();*/
  		//System.out.println("looking for distance");
    	//control.alignUntilDistance(12, 15,40);
    	//Delay.msDelay(1500);;

 
  	}
}

 