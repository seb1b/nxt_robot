package git.behavior;


import lejos.nxt.Button;
import lejos.nxt.ColorSensor;
import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
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
		 * 
		 */
		System.out.println("FOLLOW LINEE");
		  
	      pid = new PIDController(45, 5);
	      pid.setPIDParam(PIDController.PID_KP, 10.0f);
	      pid.setPIDParam(PIDController.PID_KI, 0.01f);
	      pid.setPIDParam(PIDController.PID_KD, 20f);
	      pid.setPIDParam(PIDController.PID_LIMITHIGH, 180);
	      pid.setPIDParam(PIDController.PID_LIMITLOW, -180);
	      
	      pilot = new DifferentialPilot(1.3f, 3.94f, Motor.A, Motor.C, false); 
	      pilot.setTravelSpeed(3.5);
	      pilot.setRotateSpeed(75);
	      
	      detector = new LightSensor(SensorPort.S3);

	}

	
      
    public boolean takeControl() {

  		
  		return true;
  	}

  	public void action() {
  		
  		 
        while (!suppressed) {
        	if(start_run == 0){
     			 pilot.setTravelSpeed(100);
     			 pilot.forward();
     			Delay.msDelay(1000);
     			pilot.setTravelSpeed(3.5);
     			pilot.steer(30);
     			Delay.msDelay(500);
     			start_run = 1;
     			 
     		 }
                          // range is from 200 to 500
           int sensor = detector.getLightValue();         
           System.out.println("light" + sensor);
           int speedDelta = pid.doPID(sensor);
          // System.out.println("steering" + speedDelta);
        
           pilot.steer(speedDelta);
           
           
        }
  			
  		}


  		



  	

  	public void suppress() {
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

