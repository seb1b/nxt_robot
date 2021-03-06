package utils;

import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.robotics.navigation.DifferentialPilot;


public class Values {

	private static Values instance = null;

	private int scenario;
	private boolean callCodeReader;

	private boolean suppressed;
	
	private boolean resetStarted;
	private boolean is_startphase_running = false;
	private boolean elevatorGreen;
	
	private DifferentialPilot pilot;

	private Values() {
		this.pilot =  new DifferentialPilot(3.5, 22.2, Motor.A, Motor.C, false);
		this.scenario = 0;
		this.resetStarted = false;
		this.callCodeReader = false;

	}
	
	public static Values Instance() {
		if (instance != null)
			return instance;

		else {
			instance = new Values();
			return instance;
		}

	}

	
	public boolean isSuppressed() {
		return suppressed;
	}

	public void setSuppressed(boolean suppressed) {

		this.suppressed = suppressed;
	}

	public boolean isCallCodeReader() {
		return callCodeReader;
	}

	public void setCallCodeReader(boolean callCodeReader) {
		this.callCodeReader = callCodeReader;
	}

	public boolean isStartphaseRunning() {
		return is_startphase_running;
	}
	
	public void setStartphaseRunning(boolean t) {
		is_startphase_running = t;
			

	}

	public boolean getElevatorGreen(LightSensor ls){
		elevatorGreen = false;
		ls.setFloodlight(false);
		if(ls.getLightValue() <= 45 && ls.getLightValue() >= 40 ){
			elevatorGreen = true;
		}
		return elevatorGreen;
	}
	
	public int getScenario() {
		return scenario;
	}

	public void setScenario(int s) {
		this.scenario = s;
	}
	
	
	public void incScenario() {
		this.scenario++;
	}

	public boolean isResetStarted() {
		return resetStarted;
	}
	
	public DifferentialPilot getPilot() {
	/*	if(pilot == null){
			pilot = new DifferentialPilot(1.3f, 3.94f, Motor.A, Motor.C, false);
		}*/
		
		return pilot;
	}

	public void setResetStarted(boolean resetStarted) {
		this.resetStarted = resetStarted;
	}
	
	public boolean justStarted(long time) {
		return (System.currentTimeMillis() - time) < 2000;
	}
}
