package project.server;


import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 * This class is used to keep all the client session 
 * within a single object
 *
 */
public class ActiveSessions {
	
	
	
	private Map<String, ClientSession> sessionMap = new HashMap<String, ClientSession>();
	
	/**
	 * adds session to active session if it was not there
	 * @param name - client name
	 * @param s - incoming clientsession
	 * @return -  {@link Boolean}
	 */
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
	
	/**
	 * removes the specified client session
	 * @param sess - session to be removed
	 */
	public void removeSession(ClientSession sess){
		synchronized(sessionMap){
			sessionMap.remove(sess.getName());
		}
	}
	
	
	
	
	/**
	 * @return - iterator of session names
	 */
	public Iterator<String> iteratorNames() { 			 
		return sessionMap.keySet().iterator();
	}
	/**
	 * @return - iterator of sessions
	 */
	public Iterator<ClientSession> iteratorSessions() { 			 
		return sessionMap.values().iterator();
	}
	
	
	
	/**
	 * @param name - name of the session
	 * @return session specified by the name
	 */
	public ClientSession get(String name){
		synchronized(sessionMap){
			if(sessionMap.containsKey(name)) return sessionMap.get(name);
			return null;
		}
	}
	
	
	
}