package project.server;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import project.utils.OutboundMessages;


/**
 * This class implement the logic of a server used
 * for interacting with clients
 *
 */
public class Server {
	private static final int PORT = 8888;
	private static File logFile = new File("log.txt");

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
	
	 /**
	  * writes client activity into a log file
	 * @param contents - message contents
	 */
	public static synchronized void writeMsgIntoLogFile(String contents){
		   
			try {
				FileWriter writer = new FileWriter(logFile,true);
				BufferedWriter bw = new BufferedWriter(writer);
				PrintWriter pw = new PrintWriter(bw);
				pw.println(getTimeStamp()+contents);
				pw.close();
				bw.close();
				writer.close();
			} catch (IOException e) {
				
				System.out.println("logifaili kirjutamise viga!");
				//e.printStackTrace();
			}
		
		   
	   }
	   
	   /**
	 * @return - timestamp string
	 */
	public static synchronized String getTimeStamp(){
			Calendar cal = Calendar.getInstance();
	    	cal.getTime();
	    	SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
			return ("["+sdf.format(cal.getTime())+"] ");
		}
}