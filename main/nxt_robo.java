package git.main;
/*import behaviors.DriveForward;
import behaviors.Endboss;

import behaviors.LabirinthGateBehavior;
import behaviors.LabyrinthLeft;
import behaviors.LabyrinthRight;
import behaviors.SpinningEncounter;
import behaviors.TestBehavior;
*/
import git.behavior.FollowLine;
import git.behavior.Bridge;
import git.behavior.Labyrinth;
import git.behavior.StartPhase;
import motors.SensorMotor;
import motors.LeftMotor;
import motors.RightMotor;
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
		
	  Behavior b0 = new StartPhase();
      Behavior b1 = new FollowLine();
      Behavior b2 = new Bridge();
      Behavior b3 = new Labyrinth();
     /* Behavior b3 = new FollowLine();
      Behavior b5 = new LabyrinthLeft();
      Behavior b6 = new LabirinthGateBehavior();
      Behavior b11 = new Endboss();
      Behavior b12  = new SpinningEncounter();*/
      Behavior [] bArray = {b1,b0};
      Arbitrator arby = new Arbitrator(bArray);
      arby.start();
   }
}