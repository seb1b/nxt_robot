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
	private boolean justTurned=false;
	private int lastTurnDirection=1;

	public FollowLine2ndPart() {

		//pilot = new DifferentialPilot(1.3f, 3.94f, Motor.A, Motor.C, false); 
		pilot = value.getPilot();
		pilot.setTravelSpeed(22);
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


		if(value.getScenario() == 7) {
			suppressed =false;
			return true;
		}else if(value.getScenario() == 9){
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
		int counter = 0;
		//int value = 0;
		boolean end_reached = false;
		boolean ramp_reached  = false;
		int factor = 30;
		int not_online_counter = 0;
		int ThisTurnDirection=1;
		int[] ret_turn = {0,50,150,100};
		int[] turn = {50,100,50};
		
		int[] second_ret_turn = {0,50,50};
		int[] second_turn = {50,50};
		float turnSpeed = 50;
		float max_turnSpeed = pilot.getMaxRotateSpeed()-40;
		int aligned =0;
		boolean r;
		boolean l;
		while (!suppressed) {


			if(ramp_reached && aligned >2){
				System.out.println("looking for wall");
				pilot.setTravelSpeed(20);
				while(true){
					
					
					pilot.forward();
					Delay.msDelay(10);
					l = touch_l.isPressed();
					 r = touch_r.isPressed();
					if(r &&l){
						System.out.println("both pressed");
						break;
					}
					
					if(r){
						System.out.println("r");
						pilot.stop();
						pilot.backward();
						Delay.msDelay(150);
						pilot.rotate(20);
					}
					if(l){
						System.out.println("l");
						pilot.stop();
						pilot.backward();
						Delay.msDelay(150);
						pilot.rotate(-20);
					}
				}
				pilot.forward();
				Delay.msDelay(200);
				System.out.println("inc Secanrio");
				value.incScenario();
				suppressed = true;
				pilot.stop();
				//suppress();
				//break;
				
			}

		

			if(online()){
				if(justTurned){
					justTurned=false;
					Delay.msDelay(10);//kleines delay um wirklich in die linie hinein zu schwenken
					pilot.stop();
					aligned++;
					
				}
				if(aligned >2 &&ramp_reached){
					continue;
				}

				pilot.forward();
				
				counter = 0;
				ThisTurnDirection=lastTurnDirection;
				if(ramp_reached){
					ThisTurnDirection = 2;
				}
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

						if(!ramp_reached){

							if(counter >2){
								end_reached = true;
								break;
							}

							if(ThisTurnDirection==2){
								
								lastTurnDirection=2;
								ThisTurnDirection=1;
								System.out.println("turn direct 2");
								pilot.setRotateSpeed(max_turnSpeed);
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
								System.out.println("turn direct 1");
								pilot.setRotateSpeed(max_turnSpeed);
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
							
						//second line part after turn table	
						}else{
							if(counter >1 ||getIsPressed()){
								end_reached = true;
								break;
							}



							if(ThisTurnDirection==2){

								lastTurnDirection=2;
								ThisTurnDirection=1;
								pilot.setRotateSpeed(max_turnSpeed);
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
								pilot.setRotateSpeed(max_turnSpeed);
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
						}	
						//}       				
					}

				if(end_reached){
					System.out.println("end reached");
					pilot.setRotateSpeed(pilot.getRotateMaxSpeed());
					
					
					if(lastTurnDirection ==1){
						pilot.rotate(ret_turn[counter]);
					}else{
						pilot.rotate(-ret_turn[counter]);

					}
					

					if(final_part){
						value.incScenario();
						suppressed = true;
						//suppress();
					}else if(!ramp_reached){
						aligned = 0;
						pilot.forward();
						Delay.msDelay(3000);
						pilot.stop();
						counter = 0;
						end_reached = false;
						ramp_reached = true;
						lastTurnDirection=1;
						ThisTurnDirection=2;
						
					}else if(ramp_reached){
						aligned = 3;
					}
					
				}


			}
			}

		}      		
	}



	boolean online(){
		return detector.getLightValue()>52;

	}


	public void suppress() {

		pilot.stop();
		System.out.println("suppress");
		//suppressed = true;
	}
} 