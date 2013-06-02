package project.messages;

import project.client.Client;
import project.server.ClientSession;


/**
 * This class implements the type of message used 
 * to stop session of the client
 *
 */
@SuppressWarnings("serial")
public class ExitMsg extends Message {

	

	@Override
	public void action(Client cli) {
		
		
	}

	@Override
	public void action(ClientSession sess) {
		sess.stopSession();
		
	}

}
