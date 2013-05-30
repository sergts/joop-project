package project.messages;

import project.ClientSession;
import project.client.Client;

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
