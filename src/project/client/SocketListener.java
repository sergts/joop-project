package project.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import project.messages.Message;


class SocketListener extends Thread {
	private ObjectInputStream netIn;
	private Socket socket;
	
	private Client cli;
	
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

		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
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
	
	/*
	public boolean analyzeMessage(Message msg){
		
		if(msg.getMessageType() == MessageType.UPDATE){
			out.addMessage(new UpdFilesMsg(FileUtils.getFilesFormatted(Client.getWatcher().getMap()), MessageType.UPDATE));
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
	*/
}