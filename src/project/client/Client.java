package project.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Constructor;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;


import project.messages.*;
import project.utils.FileInfo;
import project.utils.Logger;
import project.utils.OutboundMessages;


/**
 * This class implements the logic of a client side of the
 * client-server interaction
 * @author Roman
 *
 */
public class Client extends Thread {

	private DirWatcher watcher;
	private ClientMessageSender sender;
	private SocketListener listener;
	private String directory;
	private int port;
	private String serverAddr;
	private OutboundMessages out;
	private Socket socket;
	private ObjectInputStream netIn;
	private ObjectOutputStream netOut;
	private int state;
	private boolean initDir;
	private ConcurrentHashMap<String, FileInfo> filesOnServer;
	private CopyOnWriteArrayList<String> usersOnServer;
	private Logger logger;
	private boolean run;
	private Set<Integer> busyPorts;  
	private final static int START_INDEX = 0; 
	private final static int OK_DIR_STATE = 0;
	private final static int WRONG_DIR_STATE = 1;
	private final static int NAMEINIT_START_STATE = 0;
    private final static int NAME_ACK_WAIT_STATE = 1;
    private final static int NAME_USED_STATE = 2;
    private final static int OK_NAME_STATE = 3;

	/**
	 * {@link Constructor}
	 * @param server - server address
	 * @param port - port used for connection
	 */
	public Client(String server, int port, Logger logger){
		this.port = port;
		this.serverAddr = server;
		this.logger = logger;
		logger.add("New client process started");
		start();
	}
	/**
	 * default {@link Constructor}
	 */
	public Client(){
		port = 8888;
		serverAddr = "localhost";
		setDirectory("");
		start();
	}



	
	public void run(){
		
		setBusyPorts(new HashSet<Integer>());                  
		setOut(new OutboundMessages());     //outcoming messag
		run = true;
		initDir();

		try {
			logger.add("Trying to connect to " + serverAddr + ":" + port);
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
			sender = new ClientMessageSender(getOut(), netOut);
			listener = new SocketListener(this);
			
			out.addMessage(new InitializeIpMsg(InetAddress.getLocalHost().getHostAddress())); // sends ip
			initNameGUI();  
			
			watcher = new DirWatcher(getDirectory(), this); 
			logger.add("Files will be downloaded into " + getDirectory() + "\\Downloads");
			
			while(run) { 
				Thread.sleep(1000);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			logger.add("Closing client process");
			try {
				socket.close();
			} catch (IOException e) {}
		}
	}

	/**
	 * @return a direcotry watcher specified for this client
	 */
	public DirWatcher getWatcher(){
		return watcher;

	}

	/**
	 * establish connection for downloading a file
	 * @param ip - ip to download from
	 * @param fileName - filename
	 * @param port - port for download connection
	 * 
	 */
	public void downloadConn(String ip, String fileName, int port){
		busyPorts.add(port);
		new DownloadConn(fileName, ip, port, directory, this);
	}
	
	
	/**
	 * establishes connection for uploading a file, 
	 * sends message to the client who wants to download a file
	 * to initiate connection
	 * 
	 * @param fileName - name of file to be downloaded
	 * @param downloadInfo - whom to send, filename, ip
	 */
	public void uploadConn(String fileName, String downloadInfo){
		int port = getFreePort();
		new UploadConn(fileName, port, this);
		
		out.addMessage(new OpenDownloadConnMsg( 
				downloadInfo.substring(downloadInfo.indexOf("<") + 1) + "<"+port, 
				downloadInfo.substring(START_INDEX, downloadInfo.indexOf("<")) ));
		
		
	}
	
	
	/**
	 * @return - free port for establishing connection
	 */
	public int getFreePort(){
		synchronized(busyPorts){
			int port = 8000;
			int maxPort = 8887;
			while(port <= maxPort){
				if(port == maxPort) port = 8000;
				if(!getBusyPorts().contains(port) && available(port)){
					getBusyPorts().add(port);
					break;
				}
				port++;
			}
			return port;
		}	
	}
	
	/**
	 * Checks to see if a specific port is available.
	 */
	public static boolean available(int port) {
	    ServerSocket ss = null;
	    try {
	        ss = new ServerSocket(port);
	        ss.setReuseAddress(true);
	        return true;
	    } catch (IOException e) {
	    } finally {
	        if (ss != null) {
	            try {
	                ss.close();
	            } catch (IOException e) {}
	        }
	    }
	    return false;
	}
	
	/**
	 * @param port - port which is freed
	 */
	public void removePort(int port){
		synchronized(busyPorts){
			busyPorts.remove(port);
		}
	}
	


	/**
	 * @return - socket of client-server connection
	 */
	public Socket getSocket() {
		return socket;
	}



	/**
	 * @param socket - set socket for for client-server connection
	 */
	public void setSocket(Socket socket) {
		this.socket = socket;
	}



	/**
	 * @return inputstream of client's objects
	 */
	public ObjectInputStream getNetIn() {
		return netIn;
	}



	/**
	 * sets inputstream of client objects
	 * @param netIn inputstream
	 */
	public void setNetIn(ObjectInputStream netIn) {
		this.netIn = netIn;
	}



	



	/**
	 * @return - outcoming message
	 */
	public synchronized OutboundMessages getOut() {
		return out;
	}
	
	/**
	 * set client's files on server
	 * @param files - files to be set in server
	 */
	public void setFilesOnServer(ConcurrentHashMap<String, FileInfo> files){
		filesOnServer = new ConcurrentHashMap<String, FileInfo>(files);
	}



	/**
	 * sets the outcoming message list
	 * @param out - outcoming messages
	 */
	public void setOut(OutboundMessages out) {
		this.out = out;
	}

	/**
	 * changes direcotry of the dirwatcher
	 * @param dir - new directory to be set 
	 */
	public void resetDir(String dir){
		if(validateDirectory(dir)){
			
			watcher.stopThread();
			watcher = new DirWatcher(dir, this);
			logger.add("Files will be downloaded into " + dir + "\\Downloads");
		
		}
	}
	
	/**
	 * checks if specified path is a directory
	 * @param directory - direcoty to be checked
	 * @return {@link Boolean}
	 */
	private boolean validateDirectory(String directory){	
			File check = new File(directory);
			if (check.isDirectory()){
				logger.add("Correct directory");
				check = null;
				return true;
			}
			check = null;
		
		
		logger.add("Wrong directory");
		return false;
		
	}
	
	
	
	/**
	 * initializes the directory to be shared before connection
	 */
	public void initDir(){
		setDirectory("");
		setInitDir(false);
		state = WRONG_DIR_STATE;
		logger.add("Enter directory to share");
		
		while(true){
			
			if(state == OK_DIR_STATE){
				
				if(validateDirectory(getDirectory())){
					setDirectory(getDirectory());
					setInitDir(true);
					state = OK_DIR_STATE;
					break;
				}else state = WRONG_DIR_STATE;
			}else{
				try {
					Thread.currentThread();
					Thread.sleep(100);
				} catch (InterruptedException e) {}
			}

		}
		
	}
	
	/**
	 * check  if the client name is already in use on the server 
	 * and sets the client name
	 */
	public void initNameGUI(){
		state = 0;
		logger.add("Enter name (use alphanumeric symbols only!)");
		boolean flag = true;
		while(true){
			if(state == NAMEINIT_START_STATE){
				try {
					setName("");
					Thread.currentThread();
					Thread.sleep(100);
				} catch (InterruptedException e) {}
				
			}
			else if(state == NAME_ACK_WAIT_STATE){
				flag = true;
				try {
					Thread.currentThread();
					Thread.sleep(100);
				} catch (InterruptedException e) {}
			}
			else if(state == NAME_USED_STATE){ //name in use
				setName("");
				if(flag) logger.add("Name already in use, enter new one");
				flag = false;
				try {
					Thread.currentThread();
					Thread.sleep(100);
				} catch (InterruptedException e) {}
				
			}
			else if(state == OK_NAME_STATE){ //server answer sets state to 2 or 3, if 3 name is ok
				logger.add("Ok, your name is: " + getName());
				getOut().addMessage(new FilesQuery());
				getOut().addMessage(new WhoQuery());
				break;
			}
		}
	}
	
	
	
	/**
	 * sets state variable on initializing name
	 * @param s -  state
	 */
	public synchronized void setState(int s){
		state = s;
	}
	/**
	 * @return state
	 */
	public int getClientState(){
		return state;
	}
	/**
	 * @return map of file list on server
	 */
	public ConcurrentHashMap<String, FileInfo> getFilesOnServer() {
		return filesOnServer;
	}
	/**
	 * sets clients on server
	 * @param u - client list
	 */
	public void setUsersOnServer(Collection<String> u) {
		CopyOnWriteArrayList<String> users = new CopyOnWriteArrayList<String>(u);
		usersOnServer = users;
		
	}
	/**
	 * @return - users on server
	 */
	public CopyOnWriteArrayList<String> getUsersOnServer() {
		return usersOnServer;
	}
	/**
	 * if shared directory is initialized
	 * @return {@link Boolean}
	 */
	public boolean isInitDir() {
		return initDir;
	}
	/**
	 * sets if the shared directory is initialized
	 * @param initDir - {@link Boolean}
	 */
	public void setInitDir(boolean initDir) {
		this.initDir = initDir;
	}
	/**
	 * @return - shared directory
	 */
	public String getDirectory() {
		return directory;
	}
	/**
	 * sets the directory to be shared
	 * @param directory 
	 */
	public void setDirectory(String directory) {
		this.directory = directory;
	}
	/**
	 * @return client logger
	 */
	public Logger getLogger() {
		return logger;
	}
	/**
	 * sets client logger
	 * @param logger
	 */
	public void setLogger(Logger logger) {
		this.logger = logger;
	}
	/**
	 * stops run() method and all other service threads
	 */
	public void stopRunning(){
		if(watcher!=null)watcher.stopThread();
		if(listener!=null)listener.stopThread();
		if(sender!=null)sender.stopThread();
		run = false;
	}
	
	/**
	 * @return - label of the GUI window
	 */
	public String getGUILabel(){
		return serverAddr +":" + Integer.toString(port);
	}
	/**
	 * @return ports which are alredy in use
	 */
	public Set<Integer> getBusyPorts() {
		return busyPorts;
	}
	/**
	 * sets ports, which are already in use
	 * @param busyPorts 
	 */
	public void setBusyPorts(Set<Integer> busyPorts) {
		this.busyPorts = busyPorts;
	}
	
	
	

	
}
