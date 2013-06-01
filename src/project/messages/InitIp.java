package project.messages;

import project.ClientSession;
import project.client.Client;

public class InitIp extends Message {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

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
