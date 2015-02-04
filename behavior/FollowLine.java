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



public class FollowLine implements Behavior {

	private boolean suppressed = false;
	private DifferentialPilot pilot;
	//private PIDController pid;
	private LightSensor detector;
	int start_run = 0;
	private Values value = Values.Instance();
	TouchSensor touch_l;
	TouchSensor touch_r;
	UltrasonicSensor sonicSensor;
	//Controls control;
	
	public FollowLine() {
		
		System.out.println("S: construct done");
		  pilot = value.getPilot();
	      pilot.setTravelSpeed(12);

	      detector = new LightSensor(SensorPort.S3);
	      
	}

	
      
    public boolean takeControl() {

    	
  		if(value.getScenario() == 1){
  			//System.out.println(" foloow line"+value.getScenario());
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
		int value = 0;
		boolean end_reached = false;
		int factor = 10;
       while (!suppressed) {
    	   if(start_run == 0){
       		System.out.println("looking for line");
       		pilot.setTravelSpeed(12);
       		while(dark()){
       			
       			pilot.forward();
       		}
       		
       			pilot.stop();
    			start_run = 1;
    			 
       		}


        		
        		//value = detector.getLightValue();

        		//System.out.println(value);
   		
        		if(online()){
        			pilot.forward();
        			counter = 1;
        		}else{
        			pilot.stop();
        			while(!online()){        				
        				if(!pilot.isMoving()){        					
        					if(counter >7){
        						end_reached = true;
        						break;
        					}
        					if(counter > 2){
        						factor = 25;
        					}
        					
        					if(counter %2 == 0){
        						pilot.travel(1);
        						pilot.rotate(7+(counter-1)*factor,true); //left    					
        					}else{       						
        						pilot.rotate(-7-(counter-1)*factor,true);//right
        					}        					
        						counter++;
        				}       				
        			}
        			
        			if(end_reached){
						System.out.println("end reached");
						pilot.rotate((10+(counter-1)*factor)/2);
						pilot.stop();
        				suppress();
        			}
       			
        		}    		
        	}   		
}

  			


boolean online(){
	return detector.getLightValue()>55;
	
}

private boolean dark(){
	
	boolean dark = true;
	System.out.println(detector.getLightValue());
	if(detector.getLightValue() >50 ){
		dark = false;
		
	}
	return dark;
}

  		  	

  	public void suppress() {
  		System.out.println("suppressed follow line");
  		pilot.setTravelSpeed(30);
  		while(!online()){
  			pilot.forward();
  		}
  		
  		detector.setFloodlight(false);
  		Delay.msDelay(2000);
  		pilot.stop();
  		//System.out.println("looking for distance");
    	//control.alignUntilDistance(12, 15,40);
    	//Delay.msDelay(1500);;
    	value.incScenario();
  		suppressed = true;
  	}
}

 