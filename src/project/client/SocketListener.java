package project.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.Collection;

import project.Message;
import project.MessageType;
import project.OutboundMessages;

class SocketListener extends Thread {
	private ObjectInputStream netIn;
	private Socket socket;
	private Collection<String> inQueue;
	OutboundMessages out;
	
	public SocketListener(Socket socket, ObjectInputStream in, Collection<String> inQueue, OutboundMessages out) {
		this.netIn = in;
		this.socket = socket;
		this.inQueue = inQueue;
		this.out = out;
		start();
	}

	public void run() {
		try {
			while (true) { 		
				Message msg = (Message) netIn.readObject(); 				// blocked...
				if(!analyzeMessage(msg)){
					synchronized (inQueue) { 					// lukku!
						inQueue.add(msg.getContents());
					}
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
	
	public boolean analyzeMessage(Message msg){
		
		if(msg.getMessageType() == MessageType.UPDATE){
			out.addMessage(new Message(FileUtils.getFilesFormatted(Client.getWatcher().getMap()), MessageType.UPDATE));
			return true;
		}
		
		if(msg.getMessageType() == MessageType.OPEN_DOWNLOAD_CONNECTION){
			String ip = msg.getContents().split(" ")[0];
			String file = msg.getContents().split(" ")[1];
			int port = Integer.parseInt(msg.getContents().split(" ")[2]);
			System.out.println(msg.getContents());
			Client.downloadConn(file, ip,  port);
			return true;
		}
		else if(msg.getMessageType() == MessageType.OPEN_UPLOAD_CONNECTION){
			String file = msg.getContents().split(" ")[0];
			int port = Integer.parseInt(msg.getContents().split(" ")[1]);
			System.out.println(msg.getContents());
			Client.uploadConn(file, port);
			return true;
		}
		
		return false;
		
	}
	
}