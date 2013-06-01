package project;


import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

public class ActiveSessions {
	private CopyOnWriteArrayList<ClientSession> sessionList = new CopyOnWriteArrayList<ClientSession>(); 
	
	
	
	
	public synchronized void addSession(ClientSession s) { 	
		sessionList.add(s);
		System.out.println("Saabus uus klient: " + s);
	}

	public Iterator<ClientSession> iterator() { 			 
		return sessionList.iterator();
	}
	
	
	public synchronized void removeSess(ClientSession sess){
		sessionList.remove(sess);
	}
}