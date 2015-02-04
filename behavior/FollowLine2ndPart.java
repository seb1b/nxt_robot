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
		int value = 0;
		boolean end_reached = false;
		int factor = 10;
		boolean ramp_reached  = false;
		
       while (!suppressed) {
    	  /*if(start_run == 0 || end_reached){
       		System.out.println("looking for line");
       		control.alignUntilLight(20, 22,60);
       		Delay.msDelay(1500);
       		pilot.setTravelSpeed(15);
    			start_run = 1;
    			 
       		}*/


        		
        		value = detector.getLightValue();

        		System.out.println(value);

        		
        		if(online()){
        			pilot.forward();
        			counter = 1;
        		}else{
        			pilot.stop();
        			while(!online()){
        				
        				if(!pilot.isMoving()){
        					if(ramp_reached && (touch_l.isPressed() || touch_r.isPressed())){
        						end_reached = true;
        						break;
        					}
        					
        					if(counter >7){

        						end_reached = true;
        						break;
        					}
        					if(counter > 3){
        						factor = 25;
        					}
        					
        					if(counter %2 == 0){
        						pilot.travel(1);
        						pilot.rotate(10+(counter-1)*factor,true); //left    					
        					}else{
        						
        						pilot.rotate(-10-(counter-1)*factor,true);//right

        					}
        				
        					
        						counter++;
        				}
        				
        				
        			}
        			
        			if(end_reached){
						System.out.println("end reached");
						pilot.rotate((10+(counter-1)*factor)/2);

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