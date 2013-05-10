package project.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;
import java.util.Collection;

class SocketListener extends Thread {
	private BufferedReader netIn;
	private Socket socket;
	private Collection<String> inQueue;
	
	public SocketListener(Socket socket, BufferedReader in, Collection<String> inQueue) {
		this.netIn = in;
		this.socket = socket;
		this.inQueue = inQueue;
	}

	public void run() {
		try {
			while (true) { 		
				String str = netIn.readLine(); 				// blocked...
				synchronized (inQueue) { 					// lukku!
					inQueue.add(str);
				}
			}

		} catch (IOException e) {
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