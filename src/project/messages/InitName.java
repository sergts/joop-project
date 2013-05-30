package project.messages;

import project.ClientSession;
import project.client.Client;

public class InitName extends Message {

	public InitName(String myName) {
		super(myName);
	}

	@Override
	public void action(Client cli) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void action(ClientSession sess) {
		sess.name = getContents();
		
	}

}
