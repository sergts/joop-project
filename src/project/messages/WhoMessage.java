package project.messages;

import java.util.Iterator;

import project.ClientSession;
import project.client.Client;

public class WhoMessage extends Message {

	
	public WhoMessage() {
		super();
	}
	
	
	
	@Override
	public void action(Client cli) {
		cli.getInQueue().add(getContents());
		
	}

	@Override
	public void action(ClientSession sess) {
		
		Iterator<ClientSession> activeSessions = sess.getActiveSessions().iterator();
		String namesOfActiveSessions = "";
		while(activeSessions.hasNext()){
			ClientSession session = activeSessions.next();
			if(session.isAlive()){
				namesOfActiveSessions = namesOfActiveSessions + (session.getName()+ "  ");
			}
			
		}
		sess.sendMessage(new TextMsg("Active users: "+ namesOfActiveSessions));
	}

	
}
