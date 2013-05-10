package project;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

class ActiveSessions {
	private Collection<ClientSession> sessionList 
			= new ArrayList<ClientSession>(); // Jutustajate kollektsioon
	
	
	
	
	public synchronized void addSession(ClientSession s) { 	// this lukku!
		sessionList.add(s);
		System.out.println("Saabus uus klient: " + s);
	}

	public Iterator<ClientSession> iterator() { 			// kus sünkronriseeritakse? 
		return sessionList.iterator();
	}
	
	public ArrayList<ClientSession> getSessions(){
		return (ArrayList<ClientSession>) sessionList;
	}
}