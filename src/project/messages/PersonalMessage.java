package project.messages;

import java.lang.reflect.Constructor;

import project.client.Client;
import project.server.ClientSession;

/**
 * This class implements the logic of the message
 * sent to other users as a private message
 * 
 *
 */
@SuppressWarnings("serial")
public class PersonalMessage extends Message{

	

	/**
	 * {@link Constructor}
	 * @param m  - content
	 * @param to - receiver
	 */
	public PersonalMessage(String m, String to){
		super(m, to);
	}
	@Override
	public void action(Client cli) {
		
		
	}

	@Override
	public void action(ClientSession sess) {
		
		
		ClientSession session = sess.activeSessions.get(this.getTo());
		if (session!=null) {
			session.sendMessage(new TextMsg(this.getContents()));
			System.out.println("PM message sent");
		}
		/*
		Iterator<ClientSession> activeSessions = sess.getActiveSessions().iterator();
		System.out.println(" sessions iterator accessed");
		
		String msg = this.contents;
		while (activeSessions.hasNext()) {
			ClientSession session = activeSessions.next();
			if (session.getName().equals(this.to)) {
				session.sendMessage(new TextMsg(msg));
				System.out.println("PM message sent");
			}
			
			
		}*/
		
		
	}
	
}
