package project.messages;

import project.client.Client;
import project.server.ClientSession;

@SuppressWarnings("serial")
public class TextMsg extends Message {

	

	

	public TextMsg(String string) {
		super(string);
	}

	@Override
	public void action(Client cli) {
		cli.getLogger().add(getContents());
	}

	@Override
	public void action(ClientSession sess) {
		
	}

}
