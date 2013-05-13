package project;

import java.util.LinkedList;

public class OutboundMessages {
	private LinkedList<Message> messages = new LinkedList<Message>(); 				// Saatmata sõnumite FIFO

	public synchronized void addMessage(Message m) { 		// this lukku!
		messages.add(m);
		//System.out.println(m.getContents() + " " + m.getMessageType());
		this.notifyAll(); 
		// Broadcaster.notify()
		
		
	}

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