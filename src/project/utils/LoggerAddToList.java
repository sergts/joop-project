package project.utils;

public class LoggerAddToList extends LoggerBehaviour {

	
	public LoggerAddToList(Logger logger) {
		super(logger);
		
		
	}

	@Override
	public synchronized void add(String log) {
		addToList(getShortTimeStamp() + log);
		
	}

}
