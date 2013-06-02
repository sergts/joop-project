package project.messages;

import project.server.ClientSession;
import project.server.Server;
import project.client.Client;

@SuppressWarnings("serial")
public class LogMessage extends Message {

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
