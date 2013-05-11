package project.client;

import java.awt.TrayIcon.MessageType;
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

import com.sun.org.apache.bcel.internal.generic.NEW;

import project.Message;

public class Client {

	public static void main(String[] args) throws IOException,
			InterruptedException {
		int port = 8888;
		DirWatcher watcher = new DirWatcher(
				"C:\\Users\\Roman\\Documents\\book\\IT");
		Socket socket;
		LinkedList<String> inQueue = new LinkedList<String>(); // FIFO
		ObjectInputStream netIn;
		ObjectOutputStream netOut;
		System.out.println("Mis on sinu nimi?");
		String myName = new Scanner(System.in).nextLine();

		InetAddress servAddr = InetAddress.getByName("82.147.186.77");// choose
																		// ip
																		// for
																		// server
																		// to
																		// connect
																		// to

		try {
			socket = new Socket(servAddr, port); // JabberServer.PORT
		} catch (IOException e) {
			System.out.println("Sry - server pole n�htav :(((");
			return;
		}
		try {
			System.out.println("socket = " + socket);

			netOut = new ObjectOutputStream(socket.getOutputStream());
			netOut.flush();
			netIn = new ObjectInputStream(socket.getInputStream());

			// Klaviatuurisisend:
			BufferedReader stdin = new BufferedReader(new InputStreamReader(
					System.in));

			// Saabunud s�numite kuulaja:
			SocketListener l = new SocketListener(socket, netIn, inQueue);
			l.start();

			netOut.reset();
			netOut.writeObject(new Message(myName, project.MessageType.TEXT)); // saadame
																				// oma
																				// nime
																				// serverisse

			String msg;

			do { // JabberClient eluts�kli p�hiosa **********************
				System.out
						.println("Sisesta s�num ja vajuta ENTER; l�petamiseks toksi END: ");
				System.out.println("Saabunud s�numite lugemiseks vajuta ENTER");

				msg = stdin.readLine(); // blocked...
				// System.out.println( "Kuulaja olek = " + k.isAlive() ); //
				// debugging...

				if (msg.length() > 0) {
					netOut.reset();
					if (msg.equals("UPDATE")) {
						Message m = new Message("", project.MessageType.UPDATE);
						m.setFilesInCurrentDirectory(watcher
								.getFilesInCurrentDirectory());
						netOut.writeObject(m);
					}
					if (msg.equalsIgnoreCase("END")) {
						netOut.writeObject(new Message("END",
								project.MessageType.END));
					}

					else{
						netOut.writeObject(new Message(msg, project.MessageType.TEXT)); // saadame oma s�numi
					}								// serverisse
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
			} while (!msg.equals("END")); // END l�petab kliendi t��

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.out.println("closing...");
			socket.close();
		}
	}

}
