package project.server;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import project.utils.*;


/**
 * This class implement the logic of a server used
 * for interacting with clients
 *
 */
public class Server {
	private static final int PORT = 8888;
	private static final String LOGFILE = "server_log.txt";
	private static Logger logger;

	public static void main(String[] args) throws IOException {
		ActiveSessions activeSessions = new ActiveSessions();
		
		logger = new Logger(new LoggerWriteToFile(logger, LOGFILE));

		ServerSocket serv = new ServerSocket(PORT);
		System.out.println("Server started");
		writeMsgIntoLogFile("Server started");
		
		try {
			while (true) { 									
				Socket sock = serv.accept(); 				
				try {
					new ClientSession(sock, activeSessions);
				} catch (IOException e) {
					System.out.println("Socket creation error");
					writeMsgIntoLogFile("Socket creation error");
					sock.close();
				}
			} 

		} finally {
			System.out.println("Server down");
			serv.close();
		}
	}
	
	 /**
	  * writes client activity into a log file
	 * @param contents - message contents
	 */
	public static synchronized void writeMsgIntoLogFile(String contents){
			logger.add(contents);
	   }
	   
	
}