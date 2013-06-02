package project.messages;

import project.client.Client;
import project.server.ClientSession;


/**
 * This class implements the logic of the message used to
 * assing ip of the client to the ClientSession
 *
 */
@SuppressWarnings("serial")
public class InitIp extends Message {

	


	public InitIp(String hostAddress) {
		super(hostAddress);
	}

	@Override
	public void action(Client cli) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void action(ClientSession sess) {
		sess.ip = getContents();
		
	}

}
