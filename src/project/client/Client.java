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


public class Client {
	
	public static void main(String[] args) throws IOException, InterruptedException {
		int port = 8888;
		DirWatcher watcher = new DirWatcher("H:\\Projects\\test");
		Socket socket;
		BufferedReader netIn;
		LinkedList<String> inQueue = new LinkedList<String>(); // FIFO
		
		System.out.println("Mis on sinu nimi?");
		String myName = new Scanner(System.in).nextLine();
		
		InetAddress servAddr = InetAddress.getByName("localhost");

		try {
			socket = new Socket(servAddr, port); 				// JabberServer.PORT
		} catch (IOException e) {
			System.out.println("Sry - server pole nähtav :(((");
			return;
		}
		try {
			System.out.println("socket = " + socket);
			
			// Sokli sisend-väljund:
			netIn = new BufferedReader(
							new InputStreamReader(
									socket.getInputStream()));
			PrintWriter netOut = new PrintWriter(
						new BufferedWriter(
								new OutputStreamWriter(
										socket.getOutputStream())), true);
			
			// Klaviatuurisisend:
			BufferedReader stdin = new BufferedReader(
					new InputStreamReader(System.in));

			// Saabunud sõnumite kuulaja:
			SocketListener l = new SocketListener(socket, netIn, inQueue);
			l.start();

			netOut.println(myName); // saadame oma nime serverisse
			
			String msg;

			do { // JabberClient elutsükli põhiosa **********************
				System.out.println("Sisesta sõnum ja vajuta ENTER; lõpetamiseks toksi END: ");
				System.out.println("Saabunud sõnumite lugemiseks vajuta ENTER");
				
				msg = stdin.readLine(); 						// blocked...
				// System.out.println( "Kuulaja olek = " + k.isAlive() ); 	// debugging...
				
				if (msg.length() > 0)
					netOut.println(msg); 						// saadame oma sõnumi serverisse
				
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

		} finally {
			System.out.println("closing...");
			socket.close();
		}
	}

}
