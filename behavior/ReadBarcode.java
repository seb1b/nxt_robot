package behaviors;

import utils.Values;
import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.robotics.subsumption.Behavior;
import lejos.util.Delay;

public class ReadBarcode implements Behavior {

	private boolean suppressed = false;

	private Values values = Values.Instance();
	private LightSensor light = new LightSensor(SensorPort.S3);
	
	private boolean onLine;
	private boolean started;
	private int nLines = 0;

	public boolean takeControl() {

		if (values.isCallCodeReader()) {
			return true;
		} else {
			return false;
		}
	}

	public void action() {

		values.setCallCodeReader(false);
		
		//Delay.msDelay(2000);
		values.setSuppressed(false);
		suppressed = false;

		driveBackward();
		Delay.msDelay(400);
		
		driveForward();

		nLines = countLines();
		
		System.out.println("CountLines done");
		
		
		if(nLines > 2) {
			driveStop();
			
			System.out.println(nLines);

			//values.setScenario(nLines);
			values.incScenario();
		}
	}

	public void suppress() {

		driveStop();
		suppressed = true;
		values.setSuppressed(true);
	}

	private int countLines() {

		started = false;
		onLine  = false;
		long startTime = 0;
		startTime = System.currentTimeMillis();
		int lineCount = 0;

		while (true) {

			if (!onLine && light.getLightValue() >= 50) {
				
				started = true;
				onLine  = true;
				lineCount++;
				
				System.out.println(">"+lineCount);
				
			} else if(started && light.getLightValue() < 50) {
				onLine = false;
			} 
			
			// Stop if more than one Line is found or 2 seconds passed
			if((System.currentTimeMillis() - startTime) > 6000 || lineCount > 2) {
				break;
			}
		}

		return lineCount;

	}

	private void driveForward() {

		Motor.A.setSpeed(600);
		Motor.C.setSpeed(600);

		Motor.A.forward();
		Motor.C.forward();
	}

	private void driveBackward() {
		
		Motor.A.setSpeed(900);
		Motor.C.setSpeed(900);

		Motor.A.backward();
		Motor.C.backward();
	}

	private void driveStop() {
		
		Motor.A.stop();
		Motor.C.stop();
	}
}
