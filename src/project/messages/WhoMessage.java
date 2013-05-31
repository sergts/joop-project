package project.messages;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import project.ClientSession;
import project.FileInfo;
import project.client.Client;

public class WhoMessage extends Message {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public WhoMessage() {
		super();
	}
	
	
	
	public WhoMessage(String m) {
		super(m);
	}



	@Override
	public void action(Client cli) {
		
		List<String> users = Arrays.asList(getContents().split(" "));
		cli.setUsersOnServer(users);
		
	}

	@Override
	public void action(ClientSession sess) {
		
		
		Iterator<ClientSession> activeSessions = sess.getActiveSessions().iterator();
		String namesOfActiveSessions = "";
		while(activeSessions.hasNext()){
			ClientSession session = activeSessions.next();
			if(session.isAlive()){
				namesOfActiveSessions += (session.getName()+ " ");
			}
			
		}
		
		sess.sendMessage(new WhoMessage(namesOfActiveSessions));
	}

	
}
