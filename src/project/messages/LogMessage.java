package project.messages;

import java.lang.reflect.Constructor;

import project.server.ClientSession;
import project.server.Server;
import project.client.Client;

/**
 *This type of message implements the logic of the message 
 *used to initiate writing client interactian into log file 
 *on the server side
 *
 */
@SuppressWarnings("serial")
public class LogMessage extends Message {

	/**{@link Constructor}
	 * 
	 * @param m - message content
	 */
	public LogMessage(String m) {
		super(m);
	}

	@Override
	public void action(Client cli) {

	}

	@Override
	public void action(ClientSession sess) {
		Server.writeMsgIntoLogFile(getContents());

	}

}
