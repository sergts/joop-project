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

import project.messages.*;




public class ClientSession extends Thread {
	private Socket socket;
	private OutboundMessages outQueue;
	private ActiveSessions activeSessions;
	public ObjectInputStream netIn;
	public ObjectOutputStream netOut;
	public HashMap<String, FileInfo> files;
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
		files = new HashMap<String, FileInfo>();
		files.put("null", new FileInfo(0 ,null, null));
		
		new FileUpdateRoutine(this);
		
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
			
			((Message)netIn.readObject()).action(this);
			
			((Message)netIn.readObject()).action(this); 	// blocked - ootab kliendi nime
			
			super.setName(name); 				// anname endale nime

			activeSessions.addSession(this); 	// registreerime end aktiivsete seansside loendis

			String str = name + " tuli sisse...";
			//outQueue.addMessage(new Message(str)); 			// teatame sellest kõigile

			ClientSessionLoop:
			while (true) { 						// Kliendisessiooni elutsükli põhiosa ***
				//str = ((Message)netIn.readObject()).getContents(); 		// blocked...
				
				Message incomingMessage = (Message) netIn.readObject();
				
				incomingMessage.action(this);
				
				
				/*
				if(incomingMessage.getMessageType() == MessageType.QUERY){
					str = incomingMessage.getContents();
					if(str.equalsIgnoreCase("END")) break;
					else if(str.equals("FILES")) outQueue.addMessage(new Message("FILES", this.getName(), MessageType.LOCAL));
					else if(str.equals("WHO")) outQueue.addMessage(new Message("WHO", this.getName(), MessageType.LOCAL));
					else if(str.split(" ")[0].equals("DOWNLOAD")) outQueue.addMessage(new Message(str, this.getName(), MessageType.LOCAL));
					else if(str.split(" ")[0].equals("TALK")) 
						outQueue.addMessage(new Message(this.getName() + " tells you: " + str.substring(5 + (str.split(" ")[1].length())), (str.split(" ")[1]), MessageType.LOCAL));
					else outQueue.addMessage(new Message(str, null, MessageType.LOCAL ));
				}
				else if(incomingMessage.getMessageType() == MessageType.UPDATE){
					files = incomingMessage.getFilesInCurrentDirectory();
				}
				*/
				
				
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
												
			//outQueue.addMessage(new TextMsg((getName() + " lahkus...")));
			
		} catch (IOException | ClassNotFoundException e) {
			outQueue.addMessage(new TextMsg((getName() + " - avarii...")));
		} finally {
			try {
				getSocket().close();
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
	
	
	public void sendMessage(Message msg) {
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
}