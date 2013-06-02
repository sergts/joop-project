package project.messages;

import project.client.Client;
import project.server.ClientSession;


@SuppressWarnings("serial")
public class InitName extends Message {



	

	public InitName(String myName) {
		super(myName);
	}

	@Override
	public void action(Client cli) {
		cli.setState(Integer.parseInt(getContents()));	
	}

	@Override
	public void action(ClientSession sess) {
		
		
		if(sess.activeSessions.checkInSession(getContents(), sess)){
			sess.sendMessage(new InitName("3"));
			sess.name = getContents();
		}else{
			sess.sendMessage(new InitName("2"));
		}
		
		
		
		/*
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
		*/
	}

}
