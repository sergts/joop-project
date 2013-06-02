package project.utils;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import java.lang.reflect.Constructor;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Logger {
	private Collection<String> logs;
	
	/**
	 * {@link Constructor}
	 */
	public Logger(){
		logs = new LinkedList<String>();
	}
	
	/**
	 * add new log
	 * @param log - new log
	 */
	public synchronized void add(String log){
		Calendar cal = Calendar.getInstance();
    	cal.getTime();
    	SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		logs.add("["+sdf.format(cal.getTime())+"] "+log);
	}
	
	/**
	 * @return log iterator
	 */
	public Iterator<String> iterator() { 			 
		return logs.iterator();
	}
	
	/**
	 * @return logs size
	 */
	public int getSize(){
		return logs.size();
	}
}
