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



class ClientSession extends Thread {
	private Socket socket;
	private OutboundMessages outQueue;
	private ActiveSessions activeSessions;
	public ObjectInputStream netIn;
	public ObjectOutputStream netOut;
	
	
	
	public ClientSession(Socket s, OutboundMessages out, ActiveSessions as) throws IOException {
		
		socket = s;
		outQueue = out;
		activeSessions = as;
		netOut = new ObjectOutputStream(
				socket.getOutputStream());
		netOut.flush();
		netIn = new ObjectInputStream(
				socket.getInputStream());
		
		
		System.out.println( "ClientSession " + this + " stardib..." );
		start();
	}

	public void run() {
		try {
			/*
			netOut.reset();
			netOut.writeObject(new Message("Welcome"));
			netOut.reset();
			netOut.writeObject("Enter your username:");
			*/
			String name = ((Message)netIn.readObject()).getContents(); 	// blocked - ootab kliendi nime
			
			super.setName(name); 				// anname endale nime

			activeSessions.addSession(this); 	// registreerime end aktiivsete seansside loendis

			String str = name + " tuli sisse...";
			outQueue.addMessage(new Message(str, MessageType.TEXT)); 			// teatame sellest kõigile

			ClientSessionLoop:
			while (true) { 						// Kliendisessiooni elutsükli põhiosa ***
				//str = ((Message)netIn.readObject()).getContents(); 		// blocked...
				
				Message incomingMessage = (Message) netIn.readObject();
				
				// switch by message type and perform suitable action
				switch (incomingMessage.getMessageType()){
				
				case END:
					break ClientSessionLoop;
				case UPDATE:
					break;
				default:	
				
				}
				
				
				
			/*	if (str == null) {
					continue; 					// tuli EOF
				}
				if (str.equalsIgnoreCase("END")) {
					break;
				}
				
				//if(str.split(" ")[0].equals("SHARE")) watcher = new DirWatcher(str.split(" ")[1]);
				
				if(str.equals("FILES")) outQueue.addMessage(new Message("FILES", this.getName()));
				
				else if(str.equals("WHO")) outQueue.addMessage(new Message("WHO", this.getName()));
				
				else if(str.split(" ")[0].equals("DOWNLOAD")) outQueue.addMessage(new Message(str, this.getName()));
				
				else if(str.split(" ")[0].equals("TALK"))  outQueue.addMessage(new Message(this.getName() + " ütleb sulle: " + str.substring(5 + (str.split(" ")[1].length())), (str.split(" ")[1])));
				else outQueue.addMessage(new Message(str, null));
				*/
			} 									// **************************************
												
			outQueue.addMessage(new Message((getName() + " lahkus..."), MessageType.TEXT));
			
		} catch (IOException | ClassNotFoundException e) {
			outQueue.addMessage(new Message((getName() + " - avarii..."),  MessageType.TEXT));
		} finally {
			try {
				socket.close();
			} catch (IOException e) {}
		}
	}
	
	/*
	public void sendFile(String file, int port){
		new FileSender(file, port);
	}
	
	public void receiveFile(String ip, String fileName, int port){
		new FileReceiver(ip, fileName, port);
	}
	public String getIP() throws UnknownHostException{
		return InetAddress.getLocalHost().getHostAddress();
	}
	*/
	
	
	public void sendMessage(String msg) {
		try {
			if (!socket.isClosed()) {
				netOut.reset();
				netOut.writeObject(new Message(msg, MessageType.TEXT));
			} else {
				throw new IOException(); 			// tegelikult: CALL catch()
			}
		} catch (IOException eee) {
			outQueue.addMessage(new Message((getName() + " - avarii..."), MessageType.TEXT));
			try {
				socket.close();
			} catch (IOException ee) {}
		}
	}
}