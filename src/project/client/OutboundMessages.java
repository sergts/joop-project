package project.client;

import java.lang.reflect.Constructor;
import java.util.LinkedList;

import project.messages.Message;

/**
 * This class implements the logic of and object used to store
 * the outbound messages of the client or client session
 *
 */
public class OutboundMessages {
	private LinkedList<Message> messages = new LinkedList<Message>(); 				// Saatmata sõnumite FIFO

	/**
	 * {@link Constructor}
	 * @param m - message to be added to the outcoming message list
	 */
	public synchronized void addMessage(Message m) { 		// this lukku!
		messages.add(m);
		
		this.notifyAll(); 
	
		
		
	}

	/**
	 * @return -  first messge from the outbound messages
	 */
	public synchronized Message getMessage() { 
		try {
			while (messages.isEmpty())
				this.wait(); 
		} catch (InterruptedException e) {}

		Message s = messages.getFirst();
		messages.removeFirst();
		return s;
	}
}