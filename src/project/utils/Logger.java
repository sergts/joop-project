package project.utils;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Logger {
	private Collection<String> logs;
	
	public Logger(){
		logs = new LinkedList<String>();
	}
	
	public synchronized void add(String log){
		Calendar cal = Calendar.getInstance();
    	cal.getTime();
    	SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		logs.add("["+sdf.format(cal.getTime())+"] "+log);
	}
	
	public Iterator<String> iterator() { 			 
		return logs.iterator();
	}
	
	public int getSize(){
		return logs.size();
	}
}
