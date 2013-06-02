package project.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

import project.messages.*;
import project.utils.FileInfo;
import project.utils.OutboundMessages;




/**
 * This class implement the logic of the server side communicating with 
 * the client
 *
 */
public class ClientSession extends Thread {
	private Socket socket;
	private OutboundMessages outQueue;
	public ActiveSessions activeSessions;
	public ObjectInputStream netIn;
	public ObjectOutputStream netOut;
	public ConcurrentHashMap<String, FileInfo> files;
	public String ip;
	public String name;
	private boolean run;
	
	
	/**
	 * @param s - socket used for connection
	 * @param out - OutboundMessages of the client
	 * @param as - activesessions on the server
	 * @throws IOException
	 */
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
		run = true;
		System.out.println( "ClientSession " + this + " stardib..." );
		start();
	}

	public void run() {
		try {
			while(name == null){
				((Message)netIn.readObject()).action(this);
			}
			super.setName(name); 				
			 
			Server.writeMsgIntoLogFile(name + " connected, " + socket.toString());
			
			while (run) { 						
				Message incomingMessage = (Message) netIn.readObject();
				incomingMessage.action(this);
			} 									
												
			
			
		} catch (Exception e) {
			System.out.println(getName() + " crashed");
			Server.writeMsgIntoLogFile(name + " crashed,  " + socket.toString());
		} finally {
			System.out.println(getName() + " leaves");
			Server.writeMsgIntoLogFile(name + " left,  " + socket.toString());
			activeSessions.removeSession(this);
			try {
				getSocket().close();
			} catch (IOException e) {}
		}
	}
	
	
	
	
	/**
	 * @param msg - message sent to client
	 */
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

	/**
	 * @return socket
	 */
	public Socket getSocket() {
		return socket;
	}

	/**
	 * @param socket - socket to be set
	 */
	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	/**
	 * @return active session on the server
	 */
	public ActiveSessions getActiveSessions() {
		return activeSessions;
	}

	/**
	 * stops the session
	 */
	public void stopSession() {
		run = false;
		
	}
}