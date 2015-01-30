package utils;


public class Values {

	private static Values instance = null;

	private int scenario;
	private boolean callCodeReader;

	private boolean suppressed;
	
	private boolean resetStarted;

	private Values() {
		
		this.scenario = 0;
		this.resetStarted = false;
		this.callCodeReader = true;
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

	public static Values Instance() {
		if (instance != null)
			return instance;

		else {
			instance = new Values();
			return instance;
		}
	}


	public int getSzenario() {
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
