package project.messages;

import project.client.Client;
import project.server.ClientSession;

public interface Actable {
	public abstract void action(Client cli);
	public abstract void action(ClientSession sess);
}
