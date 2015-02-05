package behavior;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import javax.bluetooth.RemoteDevice;

import utils.Controls;
import utils.Values;
import lejos.nxt.LightSensor;
import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;
import lejos.nxt.UltrasonicSensor;
import lejos.nxt.comm.BTConnection;
import lejos.nxt.comm.Bluetooth;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.subsumption.Behavior;
import lejos.util.Delay;


public class TurnTable implements Behavior{

	
	UltrasonicSensor sonicSensor;
	DifferentialPilot pilot;
	TouchSensor touch_l;
	TouchSensor touch_r;
	Controls control;
	LightSensor ls;
	private Values values;
	
	private enum TurnTableCommand {
		HELLO, TURN, DONE, CYA, UNKNOWN;

		public static TurnTableCommand getByOrdinal(int commandOrdinal) {
			if (commandOrdinal >= values().length) {
				return UNKNOWN;
			}
			return values()[commandOrdinal];
		}
	}

	public TurnTable() {
		values = Values.Instance();
		control = Controls.Instance();
		sonicSensor = new UltrasonicSensor(SensorPort.S2);
		touch_l = new TouchSensor(SensorPort.S1);
		touch_r = new TouchSensor(SensorPort.S4);
		ls = new LightSensor(SensorPort.S3);
		pilot =  Values.Instance().getPilot(); 

	}

	public boolean takeControl() {

		if(Values.Instance().getScenario() == 8){
			return true;
		}
		return false;
		
	}

	private DataOutputStream dataOutputStream;
	private DataInputStream dataInputStream;

	public void action() {
		
		System.out.println("S: TurnTable");
    
				
		String deviceName = "TurnTable";
		RemoteDevice device = lookupDevice(deviceName);
		BTConnection connection = Bluetooth.connect(device);
		pilot.setTravelSpeed(25);

		try {
			dataOutputStream = connection.openDataOutputStream();
			dataInputStream = connection.openDataInputStream();

			TurnTableCommand command = receiveCommand();
			assertCommand(command, TurnTableCommand.HELLO);
			
			//========auf fahrt rampe zu turntable
			int aligned = 0;
			pilot.forward();
			Delay.msDelay(3000);
			pilot.stop();
			int counter = 0;
			boolean end_reached = false;

			int lastTurnDirection=1;
			int ThisTurnDirection=2;
			boolean l = false;
			boolean r = false;
			
			float turnSpeed = 50;
			float max_turnSpeed = pilot.getMaxRotateSpeed()-40;
			boolean justTurned=false;
			int not_online_counter = 0;
			int[] ret_turn = {0,50,150,100};
			int[] turn = {50,100,50};
			
			while(true){
				
				
				/* wir haben uns ausgerichtet */
				if(aligned >2){
				System.out.println("looking for wall");
				pilot.setTravelSpeed(20);
				
				/*	fahre in die box	*/
				while(true){
					
					
					pilot.forward();
					Delay.msDelay(100);
					l = touch_l.isPressed();
					 r = touch_r.isPressed();
					 /*wenn beide gedrueckt sind, gehe zum naechsten szenario*/
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
				
				/*fahre richtig in die box rein*/
				pilot.forward();
				Delay.msDelay(200);
				System.out.println("inc Secanrio");
				pilot.stop();
				//suppress();
				break;
				
			}
			
				/*	wir fahren die rampe hoch	*/
			if(online()){
					ThisTurnDirection=2;
					counter = 0;
					if(justTurned){
						justTurned=false;
						Delay.msDelay(10);//kleines delay um wirklich in die linie hinein zu schwenken
						pilot.stop();
						aligned++; 		//wir haben uns einmal mehr ausgerichtet
						
					}
					if(aligned >2){
						continue;		// haben wir uns mehr als 2 mal ausgerichtet gehte sofort weiter
					}

					pilot.forward();
					
					
					ThisTurnDirection=lastTurnDirection;
					not_online_counter = 0;
				}else{
					justTurned=true;
					not_online_counter++;
	    			System.out.println("counter ist bei " + not_online_counter);
	    			System.out.println("aligned ist bei " + aligned);
	    			
	    			/* checke ob wir wirklich von der linie runter sind*/
	    			if(not_online_counter >5){
	    				pilot.stop();
	    				
	    				//haben wir uns oft genug ausgerichtet
				if(counter >2){
						end_reached = true;
						aligned=3;
						continue;	//gehe ueber zu tast erkennung
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
	    			
			}
			
			
			}
		
			
		
			
			//=====================
			

			pilot.forward();
			while(!touch_l.isPressed() || !touch_r.isPressed())
				;
			pilot.stop();
			
			// drive forward

			sendCommand(TurnTableCommand.TURN);

			command = receiveCommand();
			assertCommand(command, TurnTableCommand.DONE);
			
			pilot.backward();
			Delay.msDelay(1800);
			pilot.stop();
			
		
			pilot.rotate(180);
			
			// drive backward

			sendCommand(TurnTableCommand.CYA);

		} catch (IOException e) {
			System.out.println("IOException");
		} finally {
			if (connection != null) {
				connection.close();
			}
		}

		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		System.out.println("incscenario");

		values.incScenario();
	}

	private RemoteDevice lookupDevice(String deviceName) {
		RemoteDevice device = Bluetooth.getKnownDevice(deviceName);
		if (device == null) {
			log("unknown device" + deviceName);
			log("cannot connect to TurnTable");
		}
		return device;
	}

	private void assertCommand(TurnTableCommand command,
			TurnTableCommand assertetedCommand) throws IOException {
		if (command != assertetedCommand) {
			log("Invalid command:");
			log("Expected:" + assertetedCommand);
			throw new IOException("Invalid Command");
		}
	}

	private TurnTableCommand receiveCommand() throws IOException {
		int commandOrdinal = dataInputStream.readInt();
		TurnTableCommand command = TurnTableCommand
				.getByOrdinal(commandOrdinal);
		log("Receive:" + command.name());
		return command;
	}

	private void sendCommand(TurnTableCommand command) throws IOException {
		dataOutputStream.writeInt(command.ordinal());
		dataOutputStream.flush();
		log("Send: " + command.name());
	}

	private void log(String message) {
		System.out.println(message);
	}


	@Override
	public void suppress() {
		// TODO Auto-generated method stub
		
	}
	
	boolean online(){
		return ls.getLightValue()>52;

	}


}