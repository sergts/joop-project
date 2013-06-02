package project.client;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Constructor;



import project.messages.Message;
import project.utils.OutboundMessages;

/**
 * This class implemets the logic of client's
 * message sending oject
 * @author Roman
 *
 */
public class ClientMessageSender extends Thread {
	
	private OutboundMessages outQueue;
	private ObjectOutputStream netOut;
	
	/**
	 * {@link Constructor}
	 * @param o - outboundmessages
	 * @param netOut - {@link OutputStream}
	 */
	ClientMessageSender(OutboundMessages o, ObjectOutputStream netOut) {
		outQueue = o;
		this.netOut = netOut;
		start();
	}

	public void run() {
		while (true) {  
			Message msg = outQueue.getMessage(); 
			try {
				netOut.reset();
				netOut.writeObject(msg); // writes messages on outputstream
				
			} catch (IOException e) {
				System.out.println("Socket closed");
				
			}
			
		}
		
	}
}
