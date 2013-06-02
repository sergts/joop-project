package project.messages;

import project.client.Client;
import project.server.ClientSession;

/**
 * This class implements the logic of usual text message
 * used to transfer text content between client and server
 *
 */
@SuppressWarnings("serial")
public class TextMessage extends Message {

	

	

	/**
	 * @param string - contents
	 */
	public TextMessage(String string) {
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
