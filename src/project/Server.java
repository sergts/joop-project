package project;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import project.utils.OutboundMessages;


public class Server {
	private static final int PORT = 8888;

	public static void main(String[] args) throws IOException {
		ActiveSessions activeSessions = new ActiveSessions();
		OutboundMessages outQueue = new OutboundMessages();

		ServerSocket serv = new ServerSocket(PORT);
		System.out.println("Server startis...");
		
		try {
			while (true) { 									
				Socket sock = serv.accept(); 				
				try {
					new ClientSession(sock, outQueue, activeSessions);
				} catch (IOException e) {
					System.out.println("Socketi loomise avarii :(");
					sock.close();
				}
			} 

		} finally {
			System.out.println("Server down");
			serv.close();
		}
	}
}