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
import project.messages.*;


public class Client extends Thread {
	
	private DirWatcher watcher;
	private String directory = "H:\\Projects\\test2";
	private int port = 8888;
	private OutboundMessages out;
	private Socket socket;
	private LinkedList<String> inQueue;
	private ObjectInputStream netIn;
	private ObjectOutputStream netOut;
	
	
	public Client(int port, String dir){
		this.port = port;
		directory = dir;
		start();
	}
	
	
	
	public  void run() {
		
		watcher = new DirWatcher(directory);
		setInQueue(new LinkedList<String>()); 
		setOut(new OutboundMessages());
		
		
		System.out.println("Enter your name: ");
		String myName = new Scanner(System.in).nextLine();
		
		InetAddress servAddr = InetAddress.getByName("localhost");// choose
																		// ip
																		// for
																		// server
																		// to
																		// connect
																		// to

		try {
			setSocket(new Socket(servAddr, port)); // JabberServer.PORT
		} catch (IOException e) {
			System.out.println("Server not visible");
			return;
		}
		try {
			System.out.println("socket = " + getSocket());

			netOut = new ObjectOutputStream(getSocket().getOutputStream());
			netOut.flush();
			setNetIn(new ObjectInputStream(getSocket().getInputStream()));
			
			new ClientMessageSender(getOut(), netOut);
			
			BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));

			SocketListener l = new SocketListener(this);
			
			
			//out.addMessage(new Message(IpChecker.getIp(), MessageType.QUERY));
			getOut().addMessage(new Message(InetAddress.getLocalHost().getHostAddress(), MessageType.QUERY));
			
			getOut().addMessage(new Message(myName, MessageType.QUERY));

			String msg;

			do { 
				msg = stdin.readLine(); 
				
				if (msg.length() > 0) {
					
					if(msg.equalsIgnoreCase("myfiles")){
						System.out.println(FileUtils.getFilesFormatted(watcher.getMap()));
					}
					else
						getOut().addMessage(new Message(msg, MessageType.QUERY));
					
				}

				if (!getInQueue().isEmpty()) { // kas on midagi saabunud?
					synchronized (getInQueue()) { // lukku !!!
						Iterator<String> incoming = getInQueue().iterator();
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
			getSocket().close();
		}
	}
	
	public DirWatcher getWatcher(){
		return watcher;
		
	}
	
	public void downloadConn(String ip, String fileName, int port){
		new FileReceiver(fileName, ip, port);
	}
	public void uploadConn(String fileName, int port){
		new FileSender(fileName, port);
	}



	public Socket getSocket() {
		return socket;
	}



	public void setSocket(Socket socket) {
		this.socket = socket;
	}



	public ObjectInputStream getNetIn() {
		return netIn;
	}



	public void setNetIn(ObjectInputStream netIn) {
		this.netIn = netIn;
	}



	public LinkedList<String> getInQueue() {
		return inQueue;
	}



	public void setInQueue(LinkedList<String> inQueue) {
		this.inQueue = inQueue;
	}



	public OutboundMessages getOut() {
		return out;
	}



	public void setOut(OutboundMessages out) {
		this.out = out;
	}

}
