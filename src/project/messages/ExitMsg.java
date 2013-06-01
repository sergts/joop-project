package project.messages;

import project.ClientSession;
import project.client.Client;

public class ExitMsg extends Message {

	
	@Override
	public void action(Client cli) {
		
		
	}

	@Override
	public void action(ClientSession sess) {
		sess.stopSession();
		
	}

}
