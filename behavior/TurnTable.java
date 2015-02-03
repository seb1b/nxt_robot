package behavior;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import javax.bluetooth.RemoteDevice;

import utils.Controls;
import utils.Values;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;
import lejos.nxt.UltrasonicSensor;
import lejos.nxt.comm.BTConnection;
import lejos.nxt.comm.Bluetooth;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.subsumption.Behavior;


public class TurnTable implements Behavior{

	
	UltrasonicSensor sonicSensor;
	DifferentialPilot pilot;
	TouchSensor touch_l;
	TouchSensor touch_r;
	Controls control;
	
	
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
		control = Controls.Instance();
		sonicSensor = new UltrasonicSensor(SensorPort.S2);
		touch_l = new TouchSensor(SensorPort.S1);
		touch_r = new TouchSensor(SensorPort.S4);
		pilot =  Values.Instance().getPilot(); // new  DifferentialPilot(1.3f, 3.94f, Motor.A, Motor.C, false); 

	}

	public boolean takeControl() {

		if(Values.Instance().getScenario() == 9){
			return true;
		}
		return false;
	}

	private DataOutputStream dataOutputStream;
	private DataInputStream dataInputStream;

	public void action() {
    
		String deviceName = "TurnTable";
		RemoteDevice device = lookupDevice(deviceName);
		BTConnection connection = Bluetooth.connect(device);
		try {
			dataOutputStream = connection.openDataOutputStream();
			dataInputStream = connection.openDataInputStream();

			TurnTableCommand command = receiveCommand();
			assertCommand(command, TurnTableCommand.HELLO);

			pilot.forward();
			while(!touch_l.isPressed() || !touch_r.isPressed())
				;
			pilot.stop();
			
			// drive forward

			sendCommand(TurnTableCommand.TURN);

			command = receiveCommand();
			assertCommand(command, TurnTableCommand.DONE);

			pilot.backward();
			
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

}