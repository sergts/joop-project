package project;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class Server {
	private static final int PORT = 8888;

	public static void main(String[] args) throws IOException {
		ActiveSessions activeSessions = new ActiveSessions();
		OutboundMessages outQueue = new OutboundMessages();

		ServerSocket serv = new ServerSocket(PORT);
		System.out.println("Server startis...");
		
		new Broadcaster(activeSessions, outQueue);
		
		try {
			while (true) { 									// serveri töötsükkel
				Socket sock = serv.accept(); 				// blocked!
				try {
					new ClientSession(sock, outQueue, activeSessions); // sh. ClientSession.start()
				} catch (IOException e) {
					System.out.println("Socketi loomise avarii :(");
					sock.close();
				}
			} 

		} finally {
			System.out.println("Server lõpetas");
			serv.close();
		}
	}
}