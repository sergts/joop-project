package project.utils;


public abstract class LoggerBehaviour {
	
	
	private Logger logger;
	
	public LoggerBehaviour(Logger logger){
		this.logger = logger;
	}
	
	public LoggerBehaviour(Logger logger, String path){
		this.logger = logger;
	}
	
	public abstract void add(String log);
	
	public synchronized void addToList(String content){
		logger.addToList(content);
	}
   
	   /**
	 * @return - timestamp string
	 */
	public String getFullTimeStamp(){
			return TimeStamp.getFullTimeStamp();
		}

	public String getShortTimeStamp(){
		return TimeStamp.getShortTimeStamp();
	}


	
}
