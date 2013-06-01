package project.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;


import project.*;
import project.messages.*;
import project.utils.FileInfo;
import project.utils.Logger;
import project.utils.OutboundMessages;


public class Client extends Thread {

	private static final int DOWNLOAD_QUERY_LENGTH = 4;
	private static final int DOWNLOAD_INDEX = 0;
	private static final int FILENAME_INDEX = 1;
	private static final int FROM_INDEX = 2;
	private static final int USERNAME_INDEX = 3;

	private DirWatcher watcher;
	private String directory;
	private int port;
	private String serverAddr;
	private OutboundMessages out;
	private Socket socket;
	private LinkedList<String> inQueue;
	private ObjectInputStream netIn;
	private ObjectOutputStream netOut;
	private int state;
	private String name;
	private boolean initDir;
	private ConcurrentHashMap<String, FileInfo> filesOnServer;
	private CopyOnWriteArrayList<String> usersOnServer;
	private Logger logger;
	


	public Client(int port, String server, String dir){
		this.port = port;
		this.serverAddr = server;
		setDirectory(dir);
		start();
	}
	public Client(){
		port = 8888;
		serverAddr = "localhost";
		setDirectory("H:\\Projects\\test2");
		start();
	}



	public void run(){

		setLogger(new Logger());
		setInQueue(new LinkedList<String>()); 
		setOut(new OutboundMessages());
		initDir();

		try {
			InetAddress servAddr = InetAddress.getByName(serverAddr);
			setSocket(new Socket(servAddr, port)); 
		} catch (IOException e) {
			logger.add("Server not visible");
			System.out.println("Server not visible");
			return;
		}
		try {
			System.out.println("socket = " + getSocket());
			logger.add("socket = " + getSocket());
			netOut = new ObjectOutputStream(getSocket().getOutputStream());
			netOut.flush();
			setNetIn(new ObjectInputStream(getSocket().getInputStream()));
			new ClientMessageSender(getOut(), netOut);
			//BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
			new SocketListener(this);
			out.addMessage(new InitIp(InetAddress.getLocalHost().getHostAddress()));
			initNameGUI();
			watcher = new DirWatcher(getDirectory(), this);
			//String msg;
			do { 
				//msg = stdin.readLine(); 
				//parseMessage(msg);
				if (!getInQueue().isEmpty()) {
					synchronized (getInQueue()) {
						Iterator<String> incoming = getInQueue().iterator();
						while (incoming.hasNext()) {
							String next = incoming.next();
							System.out.println(next);
							logger.add(next);
							incoming.remove();
						}
					}
				}
			} while (true);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			logger.add("closing...");
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
		new FileReceiver(fileName, ip, port, directory);
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
	
	public void setFilesOnServer(ConcurrentHashMap<String, FileInfo> files){
		filesOnServer = new ConcurrentHashMap<String, FileInfo>(files);
	}



	public void setOut(OutboundMessages out) {
		this.out = out;
	}
	
	public void parseMessage(String msg){
		if (msg.length() > 0) {
			
			if(msg.equalsIgnoreCase("myfiles")){
				System.out.println(watcher.getFiles());
			}
			else if(msg.equalsIgnoreCase("files")){
				
				out.addMessage(new FilesQuery());
				
			}
			else if(validateDirectory(msg)){
				
					watcher.stopThread();
					watcher = new DirWatcher(msg.split(" ")[1], this);
				
			}
			else if(msg.equalsIgnoreCase("who")){
				out.addMessage(new WhoMessage());
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
		
		
	}
	
	private boolean validateDirectory(String directory){
		
		if(directory.split(" ")[0].equalsIgnoreCase("share")){
			File check = new File(directory.split(" ")[1]);
			if (check.isDirectory()){
				System.out.println("Correct directory");
				logger.add("Correct directory");
				check = null;
				return true;
			}
			check = null;
		}
		logger.add("Wrong directory");
		System.out.println("Wrong directory");
		return false;
		
	}
	
	
	
	public void initDir(){
		setDirectory("");
		setInitDir(false);
		state = 1;
		logger.add("Enter directory to share");
		
		while(true){
			
			if(state == 0){
				
				if(validateDirectory(getDirectory())){
					setDirectory(getDirectory().split(" ")[1]);
					setInitDir(true);
					state = 0;
					break;
				}else state = 1;
			}else{
				try {
					Thread.currentThread();
					Thread.sleep(100);
				} catch (InterruptedException e) {}
			}

		}
		
	}
	
	public void initNameGUI(){
		state = 0;
		logger.add("Enter name");
		while(true){
			if(state == 0){
				try {
					setName("");
					Thread.currentThread();
					Thread.sleep(100);
				} catch (InterruptedException e) {}
				
			}
			else if(state == 1){
				try {
					Thread.currentThread();
					Thread.sleep(100);
				} catch (InterruptedException e) {}
			}
			else if(state == 2){ //name in use
				setName("");
				logger.add("Name already in use, enter new one");
				try {
					Thread.currentThread();
					Thread.sleep(100);
				} catch (InterruptedException e) {}
				
			}
			else if(state == 3){ //server answer sets state to 2 or 3, if 3 name is ok
				System.out.println("Ok, your name is: " + getName());
				logger.add("Ok, your name is: " + getName());
				
				break;
			}
		}
	}
	
	
	public void initName(){
		name = "";
		state = 0;
		while(true){
			if(state == 0){
				state = 1;
				
				while(name.length() == 0){
					System.out.println("Enter your name: ");
					name = new Scanner(System.in).nextLine();
				}
				out.addMessage(new InitName(name));
				
			}
			else if(state == 1){
				try {
					Thread.currentThread();
					Thread.sleep(100);
				} catch (InterruptedException e) {}
			}
			else if(state == 2){
				state = 1;
				name = "";
				System.out.println("Name already in use");
				logger.add("Name already in use");
				while(name.length() == 0){
					System.out.println("Enter your name: ");
					name = new Scanner(System.in).nextLine();
				}
				out.addMessage(new InitName(name));
				
			}
			else if(state == 3){ //server answer sets state to 2 or 3, if 3 name is ok
				System.out.println("Ok, your name is: " + name);
				setName(name);
				break;
			}
		}
	}
	
	public void setState(int s){
		state = s;
	}
	public int getClientState(){
		return state;
	}
	public ConcurrentHashMap<String, FileInfo> getFilesOnServer() {
		return filesOnServer;
	}
	public void setUsersOnServer(Collection<String> u) {
		CopyOnWriteArrayList<String> users = new CopyOnWriteArrayList<String>(u);
		usersOnServer = users;
		
	}
	public CopyOnWriteArrayList<String> getUsersOnServer() {
		return usersOnServer;
	}
	public boolean isInitDir() {
		return initDir;
	}
	public void setInitDir(boolean initDir) {
		this.initDir = initDir;
	}
	public String getDirectory() {
		return directory;
	}
	public void setDirectory(String directory) {
		this.directory = directory;
	}
	public Logger getLogger() {
		return logger;
	}
	public void setLogger(Logger logger) {
		this.logger = logger;
	}
	
	public String getGUILabel(){
		return serverAddr +":" + Integer.toString(port);
	}
	
	
	

	
}
