package utils;

import lejos.nxt.Motor;

public class Controls {

	private static Controls instance = null;
	private boolean suppressed;
	
	public Controls() {
		
	}
	
	public static Controls Instance() {
		if (instance != null)
			return instance;

		else {
			instance = new Controls();
			return instance;
		}
	}
	
	public void setSuppressed(boolean suppressed) {

		this.suppressed = suppressed;
	}
	
	public boolean isSuppressed() {
		return suppressed;
	}

	public void driveForward(int speed) {

		Motor.A.setSpeed(speed);
		Motor.C.setSpeed(speed);

		Motor.A.forward();
		Motor.C.forward();
	}

	public void driveBackward(int speed) {
		
		Motor.A.setSpeed(speed);
		Motor.C.setSpeed(speed);

		Motor.A.backward();
		Motor.C.backward();
	}

	public void driveStop() {  
		
		Motor.A.stop();
		Motor.C.stop();
	}
}
