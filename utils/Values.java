package utils;


public class Values {

	private static Values instance = null;

	private int scenario;
	private boolean callCodeReader;

	private boolean suppressed;
	
	private boolean resetStarted;
	private boolean is_startphase_running = false;

	private Values() {
		
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

	public void setResetStarted(boolean resetStarted) {
		this.resetStarted = resetStarted;
	}
}
