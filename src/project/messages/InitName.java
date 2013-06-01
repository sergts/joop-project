package project.messages;

import java.util.Iterator;

import project.ClientSession;
import project.client.Client;

public class InitName extends Message {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InitName(String myName) {
		super(myName);
	}

	@Override
	public void action(Client cli) {
		cli.setState(Integer.parseInt(getContents()));	
	}

	@Override
	public void action(ClientSession sess) {
		
		boolean nameNotUsed = true;
		Iterator<ClientSession> activeSessions = sess.getActiveSessions().iterator();
		while (activeSessions.hasNext()) {
			ClientSession session = activeSessions.next();
			if (session.getName().equals(getContents())) {
				sess.sendMessage(new InitName("2"));
				nameNotUsed = false;
				break;
			}
		}
		if(nameNotUsed){
			sess.sendMessage(new InitName("3"));
			sess.name = getContents();
		}
		
	}

}
