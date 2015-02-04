package behavior;

import utils.Values;
import lejos.nxt.Button;
import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;
import lejos.robotics.subsumption.Behavior;

public class Bootstrap implements Behavior {
	
	TouchSensor touch_r;
	TouchSensor touch_l;
	long starttime;
	int scenario;
	boolean isSet;
	
	public Bootstrap() {
		touch_r = new TouchSensor(SensorPort.S4);
		starttime = System.currentTimeMillis();
		scenario = 0;
		isSet = false;
	}

	// Give control to this behavior if robot has just been started
	public boolean takeControl() {
		if(Values.Instance().justStarted(starttime)) {
			return true;
		}
		return false;
	}

	@Override
	public void action() {
		
		System.out.print("Scenario: 0");

		while(Values.Instance().justStarted(starttime) || 
				scenario > 0 && !Button.ENTER.isDown()) {
			
			if(Button.RIGHT.isDown()) {
				scenario++;
				System.out.print(scenario);
				Button.RIGHT.waitForPressAndRelease();
			}
			
			if(Button.LEFT.isDown() && scenario > 0) {
				scenario--;
				System.out.print(scenario);
				Button.LEFT.waitForPressAndRelease();
			}
		}
		

		Values.Instance().setScenario(scenario);
		isSet = true;
	}

	@Override
	public void suppress() {
		
		
	}

}
