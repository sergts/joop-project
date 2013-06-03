package project.utils;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import java.lang.reflect.Constructor;


public class Logger {
	private Collection<String> logs;
	private LoggerBehaviour logBeh;
	
	/**
	 * {@link Constructor}
	 */
	public Logger(){
		setLogs(new LinkedList<String>());
		logBeh = new LoggerAddToList(this);
	}
	
	public Logger(LoggerBehaviour beh){
		setLogs(new LinkedList<String>());
		logBeh = beh;
	}
	
	
	public void add(String log){
		synchronized(getLogs()){
			if(logBeh!=null) logBeh.add(log);
		}
	}
	
	
	
	public synchronized void addToList(String log){
		logs.add(log);
	}
	
	
	
	/**
	 * @return log iterator
	 */
	public Iterator<String> iterator() { 			 
		return getLogs().iterator();
	}
	
	/**
	 * @return logs size
	 */
	public int getSize(){
		return getLogs().size();
	}
	
	public void setBehaviour(LoggerBehaviour b){
		this.logBeh = b;
	}

	public Collection<String> getLogs() {
		return logs;
	}

	public void setLogs(Collection<String> logs) {
		this.logs = logs;
	}
}
