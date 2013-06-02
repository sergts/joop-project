package project.messages;

import project.client.Client;
import project.server.ClientSession;

/**
 *This interface implements the message actions
 *
 */
public interface Actable {
	/**
	 * initiates message-related action on client side
	 * @param cli - client receiving a message
	 */
	public abstract void action(Client cli);
	/**
	 * initiates message-related action on server side
	 * @param sess - clientSession receiving the message
	 */
	public abstract void action(ClientSession sess);
}
