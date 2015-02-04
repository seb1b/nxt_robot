package behavior;

import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.subsumption.Behavior;
import lejos.util.Delay;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import javax.bluetooth.RemoteDevice;

import utils.Controls;
import utils.Values;
import lejos.nxt.Button;
import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;
import lejos.nxt.comm.BTConnection;
import lejos.nxt.comm.Bluetooth;

public class Gate  implements Behavior {

	private RemoteDevice remoteDevice;
	private BTConnection connection;
	private DataInputStream inputStream;
	private DataOutputStream outputStream;
	private Gate client;
	private boolean success;
	private Values values;
	private Controls controls;
	private DifferentialPilot pilot;


	TouchSensor touch_l;
	TouchSensor touch_r;

	public Gate() {
		values = Values.Instance();
		controls = Controls.Instance();
		pilot = values.getPilot();
		touch_l = new TouchSensor(SensorPort.S1);
		touch_r = new TouchSensor(SensorPort.S4);

	}

	@Override
	public boolean takeControl() {
		if(Values.Instance().getScenario() == 5){
			return true;
		}
		return false;
	}

	@Override
	public void action() {

		success = false;
		
		System.out.println("S: Calling Gate");
		
		controls.alignForTime(12, 14, 4200, 15);
		pilot.stop();
		
		// Wait for connection
		while (!connectionToGateSuccessful()) {
			sleep(50);
		}

		System.out.println("Connected to the gate.");

		// Robot drives through the gate
		System.out.println("Driving through.");

		Delay.msDelay(600);
		pilot.forward();
		pilot.setTravelSpeed((int) Math.floor(pilot.getMaxTravelSpeed()));
		Delay.msDelay(4800);
		
		pilot.stop();
		pilot.rotate(-20);


		// Tell the gate that robot passed, send a "I passed" signal
		try {
			outputStream.writeBoolean(true);
			outputStream.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("Sended passing signal");


		// Wait for answer from gate

		try {
			success = inputStream.readBoolean();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if(success) {
			// success, gate received the "I passed" signal
			// gate is closed & connection is closed
		}
		else {
			// no success, connection timeouted before gate recieved anything from robot
			// gate is closed & connection is closed -> robot has to try again
		}

		System.out.println("Received: " + success);
		
		
		//System.out.println("beforealign");
		//controls.alignUntilDistance(12, 14, 90, 12);
		controls.alignForTime(12, 14, 5000, 15);
		//System.out.println("afteralign");
		
		
		pilot.stop();
		pilot.rotate(1);
		pilot.setTravelSpeed(18);
		
		long starttime = System.currentTimeMillis();
		int stoppedtime = 0;
		
		while((System.currentTimeMillis() - stoppedtime - starttime) < 13000) {
			
			if(getIsPressed()){
				pilot.stop();
				stoppedtime +=1000;
				System.out.println("+500");
				Delay.msDelay(700);
			}
			
			System.out.println("TimeLeft: "+(13000 - (System.currentTimeMillis() - stoppedtime - starttime)));
			
			pilot.forward();
		}
		
		pilot.stop();
		System.out.println("Inc");
		values.incScenario();

	}

	@Override
	public void suppress() {
		// TODO Auto-generated method stub

	}

	/**
	 * Tries to connect to the gate
	 * @return true if connection is establish, false otherwise
	 */
	public boolean connectionToGateSuccessful() {	

		remoteDevice = new RemoteDevice("TestName", "00165304779A", 0);
		if (remoteDevice == null) {
			System.out.println("Found no remote device");
			return false;
		}


		connection = Bluetooth.connect(remoteDevice);
		if (connection == null) {
			System.out.println("Connection is null");
			return false;
		}


		inputStream = connection.openDataInputStream();
		outputStream = connection.openDataOutputStream();

		return (inputStream != null && outputStream != null);
	}

	/**
	 * Puts the thread to sleep.
	 * @param millis how long the thread sleeps
	 */
	private static void sleep(int millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			// ignore
		}
	}
	
	public boolean getIsPressed(){
		boolean isPressed = false;
		if(touch_l.isPressed() || touch_r.isPressed()){
			isPressed = true;
		}
		return isPressed;
	}

}
