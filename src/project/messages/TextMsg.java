package project.messages;

import project.ClientSession;
import project.client.Client;

public class TextMsg extends Message {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

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
