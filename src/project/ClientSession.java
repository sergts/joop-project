package project;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Timer;


class ClientSession extends Thread {
	private Socket socket;
	private OutboundMessages outQueue;
	private ActiveSessions activeSessions;
	private BufferedReader netIn;
	private PrintWriter netOut;
	public DirWatcher watcher;
	
	
	public ClientSession(Socket s, OutboundMessages out, ActiveSessions as) throws IOException {
		//watcher = new DirWatcher("H:\\Projects\\test");
		socket = s;
		outQueue = out;
		activeSessions = as;
		netIn = new BufferedReader(
					new InputStreamReader(
						socket.getInputStream()));
		netOut = new PrintWriter(
					new BufferedWriter(
						new OutputStreamWriter(
							socket.getOutputStream())), true);
		System.out.println( "ClientSession " + this + " stardib..." );
		start();
	}

	public void run() {
		try {
			netOut.println("Welcome");
			netOut.println("Enter your username:");
			String name = netIn.readLine(); 	// blocked - ootab kliendi nime
			super.setName(name); 				// anname endale nime

			activeSessions.addSession(this); 	// registreerime end aktiivsete seansside loendis

			String str = name + " tuli sisse...";
			outQueue.addMessage(new Message(str)); 			// teatame sellest kõigile

			while (true) { 						// Kliendisessiooni elutsükli põhiosa ***
				str = netIn.readLine(); 		// blocked...
				
				
				
				
				
				if (str == null) {
					continue; 					// tuli EOF
				}
				if (str.equalsIgnoreCase("END")) {
					break;
				}
				
				if(str.split(" ")[0].equals("SHARE")) watcher = new DirWatcher(str.split(" ")[1]);
				
				if(str.equals("FILES")) outQueue.addMessage(new Message("FILES", this.getName()));
				
				if(str.equals("WHO")) outQueue.addMessage(new Message("WHO", this.getName()));
				
				if(str.split(" ")[0].equals("DOWNLOAD")) outQueue.addMessage(new Message(str, this.getName()));
				
				else if(str.split(" ")[0].equals("TALK"))  outQueue.addMessage(new Message(this.getName() + " ütleb sulle: " + str.substring(5 + (str.split(" ")[1].length())), (str.split(" ")[1])));
				else outQueue.addMessage(new Message(str, null));
				
			} 									// **************************************
												
			outQueue.addMessage(new Message(getName() + " lahkus..."));
			
		} catch (IOException e) {
			outQueue.addMessage(new Message(getName() + " - avarii..."));
		} finally {
			try {
				socket.close();
			} catch (IOException e) {}
		}
	}
	
	public void sendFile(String file, int port){
		new FileSender(file, port);
	}
	
	public void receiveFile(String ip, String fileName, int port){
		new FileReceiver(ip, fileName, port);
	}
	public String getIP() throws UnknownHostException{
		return InetAddress.getLocalHost().getHostAddress();
	}
	
	
	
	public void sendMessage(String msg) {
		try {
			if (!socket.isClosed()) {
				netOut.println(msg);
			} else {
				throw new IOException(); 			// tegelikult: CALL catch()
			}
		} catch (IOException eee) {
			outQueue.addMessage(new Message(getName() + " - avarii..."));
			try {
				socket.close();
			} catch (IOException ee) {}
		}
	}
}