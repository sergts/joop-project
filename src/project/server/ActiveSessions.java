package project.server;


import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;


public class ActiveSessions {
	
	//private CopyOnWriteArrayList<ClientSession> sessionList = new CopyOnWriteArrayList<ClientSession>(); 
	
	private Map<String, ClientSession> sessionMap = new HashMap<String, ClientSession>();
	
	public synchronized boolean checkInSession(String name , ClientSession s) { 	
		if(!sessionMap.containsKey(name)){
			sessionMap.put(name, s);
			System.out.println("Saabus uus klient: " + name +" "+ s);
			return true;
		}
		else{
			return false;
		}	
	}
	
	public synchronized void removeSession(ClientSession sess){
		sessionMap.remove(sess.getName());
	}
	
	public Iterator<Entry<String, ClientSession>> iteratorMap() { 			 
		return sessionMap.entrySet().iterator();
	}
	
	public Iterator<String> iteratorNames() { 			 
		return sessionMap.keySet().iterator();
	}
	public Iterator<ClientSession> iteratorSessions() { 			 
		return sessionMap.values().iterator();
	}
	
	
	public synchronized ClientSession get(String name){
		if(sessionMap.containsKey(name)) return sessionMap.get(name);
		return null;
	}
	
	
	
	/*
	
	public synchronized void addSession(ClientSession s) { 	
		sessionList.add(s);
		System.out.println("Saabus uus klient: " + s);
	}

	public Iterator<ClientSession> iterator() { 			 
		return sessionList.iterator();
	}
	
	
	public synchronized void removeSess(ClientSession sess){
		sessionList.remove(sess);
	}*/
}