package project.messages;

import project.ClientSession;
import project.client.Client;

public class TextMsg extends Message {

	

	public TextMsg(String string) {
		super(string);
	}

	@Override
	public void action(Client cli) {
		cli.getInQueue().add(getContents());
		
	}

	@Override
	public void action(ClientSession sess) {
		
	}

}
