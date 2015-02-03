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
	private PIDController pid;
	private LightSensor detector;
	int start_run = 0;
	private Values value = Values.Instance();
	TouchSensor touch_l;
	TouchSensor touch_r;
	UltrasonicSensor sonicSensor;
	Controls control;
	
	public FollowLine() {
		/*SLOW BUT WORKING BACKUP VALUES
		 * 
		 *  pid = new PIDController(45, 5);
	      pid.setPIDParam(PIDController.PID_KP, 10.0f);
	      pid.setPIDParam(PIDController.PID_KI, 0.01f);
	      pid.setPIDParam(PIDController.PID_KD, 20f);
	      pid.setPIDParam(PIDController.PID_LIMITHIGH, 180);
	      pid.setPIDParam(PIDController.PID_LIMITLOW, -180);
	      
	      pilot = new DifferentialPilot(1.3f, 3.94f, Motor.A, Motor.C, false); 
	      pilot.setTravelSpeed(3);
	      pilot.setRotateSpeed(70);
	      
	      //working too
	       * 		  
	      pid = new PIDController(45, 5);
	      pid.setPIDParam(PIDController.PID_KP, 10.0f);
	      pid.setPIDParam(PIDController.PID_KI, 0.011f);
	      pid.setPIDParam(PIDController.PID_KD, 200f);
	      pid.setPIDParam(PIDController.PID_LIMITHIGH, 200);
	      pid.setPIDParam(PIDController.PID_LIMITLOW, -200);
	       pilot = new DifferentialPilot(1.3f, 3.94f, Motor.A, Motor.C, false); 
	      	      pilot.setTravelSpeed(5);
	      pilot.setRotateSpeed(180);
		 * 
		 */
		

		//System.out.println("FOLLOW LINEE");
		control = Controls.Instance();
		touch_l = new TouchSensor(SensorPort.S1);
		touch_r = new TouchSensor(SensorPort.S4);
		sonicSensor = new UltrasonicSensor(SensorPort.S2);
		
	      pid = new PIDController(43, 5);
	      pid.setPIDParam(PIDController.PID_KP, 10.0f);
	      pid.setPIDParam(PIDController.PID_KI, 0.01f);
	      pid.setPIDParam(PIDController.PID_KD, 150f);
	      pid.setPIDParam(PIDController.PID_LIMITHIGH, 200);
	      pid.setPIDParam(PIDController.PID_LIMITLOW, -200);
	       pilot = new DifferentialPilot(1.3f, 3.94f, Motor.A, Motor.C, false); 
	      	      pilot.setTravelSpeed(4.5);
	     // pilot.setRotateSpeed(140);
	      
	      detector = new LightSensor(SensorPort.S3);
	}

	
      
    public boolean takeControl() {

    	
  		if(value.getScenario() == 1){
  			//System.out.println(" foloow line"+value.getScenario());
  			return true;
  		}else{
  			return false;
  		}
  		//return true;


  	}

  	public void action() {
  		
  		System.out.println("S: Follow Line");
  		//long start_time = System.currentTimeMillis();
        while (!suppressed) {
        	if(start_run == 0){
        		
        		control.align(pilot,20, 22,2300);
        		Delay.msDelay(2300);
        		//pilot.setTravelSpeed(4.5);
     			start_run = 1;
     			 
     		 }
                          // range is from 200 to 500
           int sensor = detector.getLightValue();         
           System.out.println("light" + sensor);
           int speedDelta = pid.doPID(sensor);
          System.out.println("steering" + speedDelta);
           if(Math.abs(speedDelta)>90){
        	   pilot.setTravelSpeed(4);
           }else if(Math.abs(speedDelta)>30){
        	   pilot.setTravelSpeed(4.5);
           }
           else if(Math.abs(speedDelta)>20){
        	   pilot.setTravelSpeed(5.5);
           }else{
        	   pilot.setTravelSpeed(7); 
           }
        	
           pilot.steer(speedDelta);
              
           if(reached_dest()){
        	   System.out.println("ddest reached");
        	   suppressed = true;
           }
           
           
        }
        
        	suppress();
		boolean LINE_LEFT = true;
		boolean LINE_RIGHT = false;
		int l_angle = 0;
		int r_angle = 0;
        	/*while(!suppressed){
        		int value = detector.getLightValue();  
        		if(value >= 46 &&value <= 52) {
        			System.out.println("Forward");
    				pilot.forward();
    				l_angle = 0;
    				r_angle = 0;
    				continue;
    			}
    				
    			if(value<46) {
    				pilot.rotate(-l_angle, true);
    				System.out.println("LEFT");
    				l_angle++;
    				continue;
    			}
    			
    			// Good distance
    			if(value>52) {
    				System.out.println("RIGHT");
    				pilot.rotate(r_angle, true);
    				r_angle++;
    				continue;
    			}
        		
        		
        	}*/

  			
  		}


  		


 boolean reached_dest(){
	 boolean reached_destination = false;
	 if((touch_l.isPressed()||touch_r.isPressed()) && detector.getLightValue()<40){
		 reached_destination = true;
		 
		 
	 }
	 return reached_destination;
 }
  	

  	public void suppress() {
		pilot.rotate(-160);
    	control.align(pilot,10, 15,1500);
    	Delay.msDelay(1500);
    	pilot.setTravelSpeed(30);
    	pilot.forward();
    	Delay.msDelay(500);
    	value.incScenario();
  		suppressed = true;
  	}
  	


}

 

    /*  Motor.A.setSpeed(SPEED);
      Motor.C.setSpeed(SPEED);
      Motor.A.forward();
      Motor.C.forward();
      final LightSensor detector = new LightSensor(SensorPort.S3);
      while (!Button.ESCAPE.isDown()) {
                        // range is from 200 to 500
         int sensor = detector.getLightValue();         
        // System.out.println(sensor);
         int speedDelta = pid.doPID(sensor);
          System.out.println(speedDelta);
         //speedDelta = speedDelta;
         System.out.println("A steering" + (SPEED-speedDelta));
         System.out.println("C steering" + (SPEED+speedDelta));
         Motor.A.setSpeed(SPEED-speedDelta);
                        Motor.C.setSpeed(SPEED+speedDelta);
         
         
      }*/

