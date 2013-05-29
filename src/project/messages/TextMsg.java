package project.messages;

import project.ClientSession;
import project.client.Client;

public class TextMsg extends Message {

	@Override
	public void action(Client cli) {
		cli.getInQueue().add(getContents());
		
	}

	@Override
	public void action(ClientSession sess) {
		// TODO Auto-generated method stub
		
	}

}
