package project;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

import project.messages.*;
import project.utils.FileInfo;
import project.utils.OutboundMessages;




public class ClientSession extends Thread {
	private Socket socket;
	private OutboundMessages outQueue;
	public ActiveSessions activeSessions;
	public ObjectInputStream netIn;
	public ObjectOutputStream netOut;
	public ConcurrentHashMap<String, FileInfo> files;
	public String ip;
	public String name;
	
	
	public ClientSession(Socket s, OutboundMessages out, ActiveSessions as) throws IOException {
		
		setSocket(s);
		outQueue = out;
		activeSessions = as;
		netOut = new ObjectOutputStream(getSocket().getOutputStream());
		netOut.flush();
		netIn = new ObjectInputStream(getSocket().getInputStream());
		files = new ConcurrentHashMap<String, FileInfo>();
		files.put("null", new FileInfo(0 ,null, null));
		name = null;
		System.out.println( "ClientSession " + this + " stardib..." );
		start();
	}

	public void run() {
		try {
			while(name == null){
				((Message)netIn.readObject()).action(this);
			}
			super.setName(name); 				
			activeSessions.addSession(this); 
			
			while (socket.isConnected()) { 						
				Message incomingMessage = (Message) netIn.readObject();
				incomingMessage.action(this);
			} 									
												
			
			
		} catch (Exception e) {
			System.out.println(getName() + " crashed");
		} finally {
			System.out.println(getName() + " leaves");
			activeSessions.removeSess(this);
			try {
				getSocket().close();
			} catch (IOException e) {}
		}
	}
	
	
	
	
	public synchronized void sendMessage(Message msg) {
		try {
			if (!getSocket().isClosed()) {
				netOut.reset();
				netOut.writeObject(msg);
			} else {
				throw new IOException();
			}
		} catch (IOException eee) {
			try {
				getSocket().close();
			} catch (IOException ee) {}
		}
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public ActiveSessions getActiveSessions() {
		return activeSessions;
	}
}