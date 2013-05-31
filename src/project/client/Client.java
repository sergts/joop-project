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
	
	private static final int DOWNLOAD_QUERY_LENGTH = 4;
	private static final int DOWNLOAD_INDEX = 0;
	private static final int FILENAME_INDEX = 1;
	private static final int FROM_INDEX = 2;
	private static final int USERNAME_INDEX = 3;
	
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
	public Client(){
		start();
	}
	
	
	
	public void run(){
		
		//watcher = new DirWatcher(directory, this);
		setInQueue(new LinkedList<String>()); 
		setOut(new OutboundMessages());
		
		
		System.out.println("Enter your name: ");
		String myName = new Scanner(System.in).nextLine();
		

		try {
			InetAddress servAddr = InetAddress.getByName("localhost");
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
			out.addMessage(new InitIp(InetAddress.getLocalHost().getHostAddress()));
			
			out.addMessage(new InitName(myName));
			
			watcher = new DirWatcher(directory, this);
			
			String msg;

			do { 
				msg = stdin.readLine(); 
				
				if (msg.length() > 0) {
					
					if(msg.equalsIgnoreCase("myfiles")){
						System.out.println(watcher.getFiles());
					}
					else if(msg.equalsIgnoreCase("files")){
						out.addMessage(new FilesQuery());
					}
					else if(msg.split(" ")[0].equalsIgnoreCase("share")){
						File check = new File(msg.split(" ")[1]);
						if (check.isDirectory()){
							watcher.stopThread();
							watcher = new DirWatcher(msg.split(" ")[1], this);
						}
					}
					else if (msg.split(" ").length == DOWNLOAD_QUERY_LENGTH
							&& msg.split(" ")[DOWNLOAD_INDEX].equalsIgnoreCase("download")
							&& msg.split(" ")[FROM_INDEX].equalsIgnoreCase("from")) {
						
						out.addMessage(new DownloadMessage(
								                   msg.split(" ")[FILENAME_INDEX],
								                   msg.split(" ")[USERNAME_INDEX]));

					}
					
					else{
						out.addMessage(new TextMsg(msg));
					}
					
					
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
			try {
				socket.close();
			} catch (IOException e) {}
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
