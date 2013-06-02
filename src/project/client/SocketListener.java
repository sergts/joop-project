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
	private boolean run;
	
	/**
	 * @param cli - client using this SocketListener
	 */
	public SocketListener(Client cli) {
		this.cli = cli;
		this.netIn = cli.getNetIn();
		this.socket = cli.getSocket();
		run = true;
		start();
	}

	public void run() {
		try {
			while (run) { 		
				Message msg = (Message) netIn.readObject();
				msg.action(cli);
			}

		} catch (Exception e) {
			if(run) cli.getLogger().add("Problem in socket listener");
		} finally {
			try {
				if (!socket.isClosed()) {
					socket.close();
					return;
				}
			} catch (IOException ee) {}
		} 
	}
	
	public void stopThread(){
		run = false;
	}
	
	
}