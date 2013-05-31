package project;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import project.messages.*;




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
		netOut = new ObjectOutputStream(
				getSocket().getOutputStream());
		netOut.flush();
		netIn = new ObjectInputStream(
				getSocket().getInputStream());
		files = new ConcurrentHashMap<String, FileInfo>();
		files.put("null", new FileInfo(0 ,null, null));
		
		name = null;
		
		//new FileUpdateRoutine(this);
		
		System.out.println( "ClientSession " + this + " stardib..." );
		start();
	}

	public void run() {
		try {
			
			
			while(name == null){
				((Message)netIn.readObject()).action(this);
			}
			
			
			super.setName(name); 				// anname endale nime

			activeSessions.addSession(this); 	// registreerime end aktiivsete seansside loendis

			//String str = name + " tuli sisse...";
			//outQueue.addMessage(new Message(str)); 			// teatame sellest kõigile

			
			while (socket.isConnected()) { 						
				 		
				
				Message incomingMessage = (Message) netIn.readObject();
				
				incomingMessage.action(this);
				
				
				
			} 									
												
			
			
		} catch (IOException | ClassNotFoundException e) {
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
				throw new IOException(); 			// tegelikult: CALL catch()
			}
		} catch (IOException eee) {
			//outQueue.addMessage(new Message((getName() + " - avarii..."), MessageType.LOCAL));
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
		// TODO Auto-generated method stub
		return activeSessions;
	}
}