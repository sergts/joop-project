package project.utils;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;


public class Logger {
	private Collection<String> logs;
	
	public Logger(){
		logs = new LinkedList<String>();
	}
	
	public synchronized void add(String log){
		logs.add(log);
	}
	
	public Iterator<String> iterator() { 			 
		return logs.iterator();
	}
	
	public int getSize(){
		return logs.size();
	}
}
