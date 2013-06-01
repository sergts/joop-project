package project.messages;

import project.ClientSession;
import project.client.Client;

public interface Actable {
	public abstract void action(Client cli);
	public abstract void action(ClientSession sess);
}
