package project.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;


import project.messages.*;
import project.utils.FileInfo;
import project.utils.Logger;
import project.utils.OutboundMessages;


public class Client extends Thread {

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
	private boolean initDir;
	private ConcurrentHashMap<String, FileInfo> filesOnServer;
	private CopyOnWriteArrayList<String> usersOnServer;
	private Logger logger;
	private boolean run;
	private Set<Integer> busyPorts;  
	


	public Client(String server, int port){
		this.port = port;
		this.serverAddr = server;
		start();
	}
	public Client(){
		port = 8888;
		serverAddr = "localhost";
		setDirectory("");
		start();
	}



	public void run(){
		
		setBusyPorts(new HashSet<Integer>());
		setLogger(new Logger());
		setInQueue(new LinkedList<String>()); 
		setOut(new OutboundMessages());
		run = true;
		initDir();

		try {
			InetAddress servAddr = InetAddress.getByName(serverAddr);
			setSocket(new Socket(servAddr, port)); 
		} catch (IOException e) {
			logger.add("Server not visible");
			return;
		}
		try {
			logger.add("Connected to server " + getSocket());
			netOut = new ObjectOutputStream(getSocket().getOutputStream());
			netOut.flush();
			setNetIn(new ObjectInputStream(getSocket().getInputStream()));
			new ClientMessageSender(getOut(), netOut);
			new SocketListener(this);
			
			out.addMessage(new InitIp(InetAddress.getLocalHost().getHostAddress()));
			initNameGUI();
			
			watcher = new DirWatcher(getDirectory(), this);
			
			while(run) { 
				Thread.sleep(1000);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			logger.add("closing from client");
			try {
				socket.close();
			} catch (IOException e) {}
		}
	}

	public DirWatcher getWatcher(){
		return watcher;

	}

	public void downloadConn(String ip, String fileName, int port){
		busyPorts.add(port);
		new FileReceiver(fileName, ip, port, directory, this);
	}
	public void uploadConn(String fileName, int port){
		new FileSender(fileName, port, this);
	}
	
	public void uploadConn(String fileName, String downloadInfo){
		int port = getFreePort();
		new FileSender(fileName, port, this);
		
		out.addMessage(new OpenDownloadConnMsg( 
				downloadInfo.substring(downloadInfo.indexOf("<") + 1) + "<"+port, 
				downloadInfo.substring(0, downloadInfo.indexOf("<")) ));
		
		System.out.println("open download msg sent " + downloadInfo + "<"+port);
		
	}
	
	
	public int getFreePort(){
		synchronized(busyPorts){
			int port = 8000;
			int maxPort = 8887;
			while(port <= maxPort){
				if(port == maxPort) port = 8000;
				if(!getBusyPorts().contains(port)){
					getBusyPorts().add(port);
					break;
				}
				port++;
			}
			return port;
		}	
	}
	
	public void removePort(int port){
		synchronized(busyPorts){
			busyPorts.remove(port);
		}
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



	public synchronized OutboundMessages getOut() {
		return out;
	}
	
	public void setFilesOnServer(ConcurrentHashMap<String, FileInfo> files){
		filesOnServer = new ConcurrentHashMap<String, FileInfo>(files);
	}



	public void setOut(OutboundMessages out) {
		this.out = out;
	}

	public void resetDir(String dir){
		if(validateDirectory(dir)){
			
			watcher.stopThread();
			watcher = new DirWatcher(dir, this);
		
		}
	}
	
	private boolean validateDirectory(String directory){	
			File check = new File(directory);
			if (check.isDirectory()){
				logger.add("Correct directory");
				check = null;
				return true;
			}
			check = null;
		
		
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
					setDirectory(getDirectory());
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
		boolean flag = true;
		while(true){
			if(state == 0){
				try {
					setName("");
					Thread.currentThread();
					Thread.sleep(100);
				} catch (InterruptedException e) {}
				
			}
			else if(state == 1){
				flag = true;
				try {
					Thread.currentThread();
					Thread.sleep(100);
				} catch (InterruptedException e) {}
			}
			else if(state == 2){ //name in use
				setName("");
				if(flag) logger.add("Name already in use, enter new one");
				flag = false;
				try {
					Thread.currentThread();
					Thread.sleep(100);
				} catch (InterruptedException e) {}
				
			}
			else if(state == 3){ //server answer sets state to 2 or 3, if 3 name is ok
				System.out.println("Ok, your name is: " + getName());
				logger.add("Ok, your name is: " + getName());
				getOut().addMessage(new FilesQuery());
				getOut().addMessage(new WhoMessage());
				break;
			}
		}
	}
	
	
	
	public synchronized void setState(int s){
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
	public void stopRunning(){
		run = false;
	}
	
	public String getGUILabel(){
		return serverAddr +":" + Integer.toString(port);
	}
	public Set<Integer> getBusyPorts() {
		return busyPorts;
	}
	public void setBusyPorts(Set<Integer> busyPorts) {
		this.busyPorts = busyPorts;
	}
	
	
	

	
}
