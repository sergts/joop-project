package project.messages;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import project.client.Client;
import project.server.ClientSession;

@SuppressWarnings("serial")
public class WhoMessage extends Message {

	
	


	public WhoMessage() {
		super();
	}
	
	
	
	public WhoMessage(String m) {
		super(m);
	}



	@Override
	public void action(Client client) {
		
		List<String> users = Arrays.asList(getContents().split(" "));
		client.setUsersOnServer(users);
		
	}

	@Override
	public void action(ClientSession sess) {
		
		String namesOfActiveSessions = "";
		Iterator<String> names = sess.getActiveSessions().iteratorNames();
		while(names.hasNext()){
				namesOfActiveSessions += (names.next() + " ");
		}
		sess.sendMessage(new WhoMessage(namesOfActiveSessions));
		
		/*
		Iterator<ClientSession> activeSessions = sess.getActiveSessions().iterator();
		String namesOfActiveSessions = "";
		while(activeSessions.hasNext()){
			ClientSession session = activeSessions.next();
			if(session.isAlive()){
				namesOfActiveSessions += (session.getName()+ " ");
			}
			
		}*/
		
		
	}

	
}
