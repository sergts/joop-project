package project.client;

import java.io.IOException;
import java.io.ObjectOutputStream;



import project.messages.Message;
import project.utils.OutboundMessages;

public class ClientMessageSender extends Thread {
	
	private OutboundMessages outQueue;
	ObjectOutputStream netOut;
	
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
				netOut.writeObject(msg);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		
	}
}
