package project.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import java.net.*;
import java.io.*;
import java.util.*;


import project.*;

public class Client {
	
	private static DirWatcher watcher;
	
	public static void main(String[] args) throws IOException, InterruptedException {
		int port = 8888;
		watcher = new DirWatcher(
				"H:\\Projects\\test");
		Socket socket;
		LinkedList<String> inQueue = new LinkedList<String>(); // FIFO
		ObjectInputStream netIn;
		ObjectOutputStream netOut;
		OutboundMessages out = new OutboundMessages();
		
		
		
		
		
		
		System.out.println("Mis on sinu nimi?");
		String myName = new Scanner(System.in).nextLine();

		InetAddress servAddr = InetAddress.getByName("localhost");// choose
																		// ip
																		// for
																		// server
																		// to
																		// connect
																		// to

		try {
			socket = new Socket(servAddr, port); // JabberServer.PORT
		} catch (IOException e) {
			System.out.println("Server not visible");
			return;
		}
		try {
			System.out.println("socket = " + socket);

			netOut = new ObjectOutputStream(socket.getOutputStream());
			netOut.flush();
			netIn = new ObjectInputStream(socket.getInputStream());
			
			new ClientMessageSender(out, netOut);
			
			// Klaviatuurisisend:
			BufferedReader stdin = new BufferedReader(new InputStreamReader(
					System.in));

			// Saabunud s�numite kuulaja:
			SocketListener l = new SocketListener(socket, netIn, inQueue, out);
			l.start();
/*
			netOut.reset();
			netOut.writeObject(new Message(myName, MessageType.QUERY)); // saadame
																				// oma
																				// nime
		*/																		// serverisse
			out.addMessage(new Message(myName, MessageType.QUERY));

			String msg;

			do { 
				msg = stdin.readLine(); 
				
				if (msg.length() > 0) {
					
					if(msg.equalsIgnoreCase("myfiles")){
						System.out.println(FileUtils.getFilesFormatted(watcher.getMap()));
					}
					else
						out.addMessage(new Message(msg, MessageType.QUERY));
					
				}

				if (!inQueue.isEmpty()) { // kas on midagi saabunud?
					synchronized (inQueue) { // lukku !!!
						Iterator<String> incoming = inQueue.iterator();
						while (incoming.hasNext()) {
							System.out.println(">> " + incoming.next());
							incoming.remove();
						}
					}
				}
			} while (!msg.equals("END"));

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.out.println("closing...");
			socket.close();
		}
	}
	
	public static DirWatcher getWatcher(){
		return watcher;
		
	}
	
	public static void downloadConn(String ip, String fileName, int port){
		new FileReceiver(ip, fileName, port);
	}
	public static void uploadConn(String fileName, int port){
		new FileSender(fileName, port);
	}

}
