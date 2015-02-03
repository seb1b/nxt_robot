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
	Controls control;
	
	public FollowLine() {
		
		System.out.println("S: construct done");
	      //pilot = new DifferentialPilot(1.3f, 3.94f, Motor.A, Motor.C, false); 
		  pilot = value.getPilot();
	      pilot.setTravelSpeed(12);
	     // pilot.setRotateSpeed(70);
	      control = new Controls();
	      detector = new LightSensor(SensorPort.S3);
	      
	}

	
      
    public boolean takeControl() {

    	
  		if(value.getScenario() == 1){
  			System.out.println(" foloow line"+value.getScenario());
  			return true;
  		}else{
  			return false;
  		}
  	}

  	public void action() {
  		
  	  System.out.println("S: Follow Line");
    
        //	suppress();
       
		boolean ON_LINE = true;
		
		//boolean LINE_RIGHT = false;
		int counter = 1;
		int value = 0;
		boolean end_reached = false;
       while (!suppressed) {
    	   if(start_run == 0){
       		
       		control.alignUntilLight(20, 22,60);
       		pilot.setTravelSpeed(15);
    			start_run = 1;
    			 
       		}


        		
        		value = detector.getLightValue();

        		System.out.println(value);

        		
        		if(online()){
        			pilot.forward();
        			counter = 1;
        		}else{
        			pilot.stop();
        			while(!online()){
        				
        				if(!pilot.isMoving()){
        					pilot.travel(1);
        					if(counter >5){
        						end_reached = true;
        					}
        					
        					if(counter %2 == 0){
        						pilot.rotate(10+(counter-1)*30,true);//left
        					
        					}else{
        						pilot.rotate(-10-(counter-1)*30,true);//right
        					}
        				
        					
        						counter++;
        				}
        				
        				
        			}
        			
        			if(end_reached){
        				suppress();
        			}
        			
        			
        		}
        		
        		
        	}
        		
        		
        		
}

  			


boolean online(){
	return detector.getLightValue()>55;
	
}
  		


 boolean reached_dest(){
	 boolean reached_destination = false;
	 if((touch_l.isPressed()||touch_r.isPressed()) && detector.getLightValue()<40){
		 reached_destination = true;
		 
		 
	 }
	 return reached_destination;
 }
  	

  	public void suppress() {
    	control.alignUntilDistance(12, 15,40);
    	//Delay.msDelay(1500);;
    	value.incScenario();
  		suppressed = true;
  	}
}

 