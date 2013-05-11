package project.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.Collection;

import project.Message;

class SocketListener extends Thread {
	private ObjectInputStream netIn;
	private Socket socket;
	private Collection<String> inQueue;
	
	public SocketListener(Socket socket, ObjectInputStream in, Collection<String> inQueue) {
		this.netIn = in;
		this.socket = socket;
		this.inQueue = inQueue;
	}

	public void run() {
		try {
			while (true) { 		
				Message str = (Message) netIn.readObject(); 				// blocked...
				synchronized (inQueue) { 					// lukku!
					inQueue.add(str.getContents());
				}
			}

		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				if (!socket.isClosed()) {
					socket.close();
					System.out.println("closing...");
					return;
				}
			} catch (IOException ee) {}
		} 
	}
}