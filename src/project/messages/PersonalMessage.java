package project.messages;

import java.lang.reflect.Constructor;

import project.client.Client;
import project.server.ClientSession;
import project.server.Server;

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
		
		if(sess.getActiveSessions().contains(this.getTo())){
			ClientSession session = sess.getActiveSessions().get(this.getTo());
			session.sendMessage(new TextMessage(this.getContents()));
			Server.writeMsgIntoLogFile("PM message sent from " + sess + " to " + session);
		}
		
		
		
		
		
	}
	
}
