
import behavior.Bootstrap;
import behavior.Elevator;
import behavior.FollowLine;
import behavior.Bridge;
import behavior.Labyrinth;
import behavior.PlankBridge;
import behavior.StartPhase;
import behavior.ReadBarcode;
import behavior.TurnTable;
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
  	  Elevator b3 = new Elevator();
      Behavior b4 = new PlankBridge();
      Behavior b5 = new Labyrinth();
      Behavior b6 = new ReadBarcode();
      
     // Behavior b8 = new TurnTable();
      
      
      Behavior b7 = new Bootstrap();

      
     /* Behavior b3 = new FollowLine();
      Behavior b5 = new LabyrinthLeft();
      Behavior b6 = new LabirinthGateBehavior();
      Behavior b11 = new Endboss();
      Behavior b12  = new SpinningEncounter();*/

      //Behavior [] bArray = {b1};
      Behavior [] bArray = {b0, b1, b2, b3, b4, b5, b6,b7};


      Arbitrator arby = new Arbitrator(bArray);
      arby.start();
   }
}