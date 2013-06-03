package project.client;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Constructor;



import project.messages.Message;

/**
 * This class implemets the logic of client's
 * message sending oject
 * @author Roman
 *
 */
public class ClientMessageSender extends Thread {
	
	private OutboundMessages outQueue;
	private ObjectOutputStream netOut;
	private boolean run;
	
	/**
	 * {@link Constructor}
	 * @param o - outboundmessages
	 * @param netOut - {@link OutputStream}
	 */
	ClientMessageSender(OutboundMessages o, ObjectOutputStream netOut) {
		outQueue = o;
		this.netOut = netOut;
		run = true;
		start();
	}

	public void run() {
		while (run) {  
			Message msg = outQueue.getMessage(); 
			try {
				netOut.reset();
				netOut.writeObject(msg); // writes messages on outputstream
				
			} catch (IOException e) {
				System.out.println("Socket closed");
				
			}
			
		}
		
	}
	
	public void stopThread(){
		run = false;
	}
}
