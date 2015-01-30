
import behaviors.FollowLine;
import behaviors.Bridge;
import behaviors.Labyrinth;
import behaviors.Startphase;
import behaviors.ReadBarcode;

import lejos.nxt.ADSensorPort;
import lejos.nxt.SensorPort;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;

public class nxt_robo {
	
   public static void main(String [] args) {
	   
		/*RightMotor motorR = new RightMotor();
		motorR.start();
		
		LeftMotor motorL = new LeftMotor();
		motorL.start();	
		
		HeadMotor motorH = new HeadMotor();		
		motorH.start();*/
//		
		
	  Behavior b0 = new Startphase();
      //Behavior b1 = new FollowLine();
      //Behavior b2 = new Bridge();
      //Behavior b3 = new Labyrinth();
      Behavior b4 = new ReadBarcode();
      
     /* Behavior b3 = new FollowLine();
      Behavior b5 = new LabyrinthLeft();
      Behavior b6 = new LabirinthGateBehavior();
      Behavior b11 = new Endboss();
      Behavior b12  = new SpinningEncounter();*/
<<<<<<< HEAD:nxt_robo.java
      Behavior [] bArray = {b4};
=======
      Behavior [] bArray = {b1,b0};
>>>>>>> fc51cb58f4f5a2667bae3d6b3dcba83f5a84f0a3:main/nxt_robo.java
      Arbitrator arby = new Arbitrator(bArray);
      arby.start();
   }
}