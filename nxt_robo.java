
import behavior.Bootstrap;
import behavior.FollowLine;
import behavior.Bridge;
import behavior.Labyrinth;
import behavior.PlankBridge;
import behavior.StartPhase;
import behavior.ReadBarcode;
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
<<<<<<< HEAD
      //Behavior b2 = new Bridge();
      //Behavior b3 = new Labyrinth();
      Behavior b4 = new ReadBarcode();
=======
      Behavior b2 = new Bridge();
      Behavior b3 = new PlankBridge();
      Behavior b4 = new Labyrinth();
      Behavior b5 = new ReadBarcode();
      
      
      Behavior b6 = new Bootstrap();
>>>>>>> 73a488f07bda48277b933fe94924f49de7103357
      
     /* Behavior b3 = new FollowLine();
      Behavior b5 = new LabyrinthLeft();
      Behavior b6 = new LabirinthGateBehavior();
      Behavior b11 = new Endboss();
      Behavior b12  = new SpinningEncounter();*/

<<<<<<< HEAD
      Behavior [] bArray = {b1};
=======
      Behavior [] bArray = {b0, b1, b2, b3, b4, b5, b6};
>>>>>>> 73a488f07bda48277b933fe94924f49de7103357

      Arbitrator arby = new Arbitrator(bArray);
      arby.start();
   }
}