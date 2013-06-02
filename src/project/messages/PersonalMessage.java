package project.messages;

import java.util.Iterator;

import project.ClientSession;
import project.client.Client;

public class PersonalMessage extends Message{

	public PersonalMessage(String m, String to){
		super(m, to);
	}
	@Override
	public void action(Client cli) {
		
		
	}

	@Override
	public void action(ClientSession sess) {
		
		Iterator<ClientSession> activeSessions = sess.getActiveSessions().iterator();
		System.out.println(" sessions iterator accessed");
		
		String msg = this.contents;
		while (activeSessions.hasNext()) {
			ClientSession session = activeSessions.next();
			if (session.getName().equals(this.to)) {
				session.sendMessage(new TextMsg(msg));
				System.out.println("PM message sent");
			}
			
			
		}
		
		
	}
	
}
