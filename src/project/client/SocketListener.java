package project.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import project.messages.Message;


/**
 * Implements the logic of the message-receiving
 * mechanism, which makes messages initiate corresponding actions
 *
 */
class SocketListener extends Thread {
	private ObjectInputStream netIn;
	private Socket socket;
	
	private Client cli;
	
	/**
	 * @param cli - client using this SocketListener
	 */
	public SocketListener(Client cli) {
		this.cli = cli;
		this.netIn = cli.getNetIn();
		this.socket = cli.getSocket();
		start();
	}

	public void run() {
		try {
			while (true) { 		
				Message msg = (Message) netIn.readObject();
				msg.action(cli);
			}

		} catch (Exception e) {
			cli.getLogger().add("Problem in socket listener");
		} finally {
			try {
				if (!socket.isClosed()) {
					socket.close();
					cli.getLogger().add("closing...");
					System.out.println("closing...");
					return;
				}
			} catch (IOException ee) {}
		} 
	}
	
	
}