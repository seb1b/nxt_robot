package behavior;

import utils.Values;
import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.subsumption.Behavior;
import lejos.util.Delay;

public class ReadBarcode implements Behavior {

	private boolean suppressed = false;

	private Values values = Values.Instance();
	private LightSensor light = new LightSensor(SensorPort.S3);
	private DifferentialPilot pilot = new DifferentialPilot(1.3f, 3.94f, Motor.A, Motor.C, false); 

	
	private boolean onLine;
	private boolean started;
	private int nLines = 0;

	public boolean takeControl() {

		if (values.isCallCodeReader()) {
			//System.out.println("barrcode");
			return true;
		}
		return false;
	}

	public void action() {
		
		System.out.println("S: Read Barcode");

		values.setCallCodeReader(false);
		
		//Delay.msDelay(2000);
		values.setSuppressed(false);
		suppressed = false;
		
		pilot.setTravelSpeed(pilot.getMaxTravelSpeed());

		pilot.backward();
		Delay.msDelay(600);
		
		pilot.forward();

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
		boolean reading =true;
		long startTime = 0;
		startTime = System.currentTimeMillis();
		int lineCount = 0;

		while (reading) {

			if (!onLine && light.getLightValue() >= 50) {
				
				started = true;
				onLine  = true;
				lineCount++;
				
				System.out.println(">"+lineCount);
				
			} else if(started && light.getLightValue() < 50) {
				onLine = false;
			} 
			
			// Stop if more than one Line is found or 2 seconds passed
			if((System.currentTimeMillis() - startTime) > 6000 || lineCount > 3) {
				reading = false;
			}
		}

		return lineCount;

	}

	private void driveStop() {
		
		Motor.A.setSpeed(0);
		Motor.C.setSpeed(0);
	}
}
