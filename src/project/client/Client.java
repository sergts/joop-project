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

import project.Message;


public class Client {
	
	public static void main(String[] args) throws IOException, InterruptedException {
		int port = 8888;
		DirWatcher watcher = new DirWatcher("H:\\Projects\\test");
		Socket socket;
		LinkedList<String> inQueue = new LinkedList<String>(); // FIFO
		ObjectInputStream netIn;
		ObjectOutputStream netOut;
		System.out.println("Mis on sinu nimi?");
		String myName = new Scanner(System.in).nextLine();
		
		InetAddress servAddr = InetAddress.getByName("localhost");  //choose ip for server to connect to

		try {
			socket = new Socket(servAddr, port); 				// JabberServer.PORT
		} catch (IOException e) {
			System.out.println("Sry - server pole nähtav :(((");
			return;
		}
		try {
			System.out.println("socket = " + socket);
			
			
			netOut = new ObjectOutputStream(
					socket.getOutputStream());
			netOut.flush();
			netIn = new ObjectInputStream(
					socket.getInputStream());
			
			
			
			
			// Klaviatuurisisend:
			BufferedReader stdin = new BufferedReader(
					new InputStreamReader(System.in));

			// Saabunud sõnumite kuulaja:
			SocketListener l = new SocketListener(socket, netIn, inQueue);
			l.start();
			
			netOut.reset();
			netOut.writeObject(new Message(myName)); // saadame oma nime serverisse
			
			String msg;

			do { // JabberClient elutsükli põhiosa **********************
				System.out.println("Sisesta sõnum ja vajuta ENTER; lõpetamiseks toksi END: ");
				System.out.println("Saabunud sõnumite lugemiseks vajuta ENTER");
				
				msg = stdin.readLine(); 						// blocked...
				// System.out.println( "Kuulaja olek = " + k.isAlive() ); 	// debugging...
				
				if (msg.length() > 0){
					netOut.reset();
					netOut.writeObject(new Message(msg));						// saadame oma sõnumi serverisse
				}
				
				if (!inQueue.isEmpty()) { 						// kas on midagi saabunud?
					synchronized (inQueue) { 					// lukku !!!
						Iterator<String> incoming = inQueue.iterator();
						while (incoming.hasNext()) {
							System.out.println(">> " + incoming.next());
							incoming.remove();
						}
					}
				}
			} while (!msg.equals("END")); // END lõpetab kliendi töö

		} catch(Exception e){
			e.printStackTrace();
		}
		finally {
			System.out.println("closing...");
			socket.close();
		}
	}

}
