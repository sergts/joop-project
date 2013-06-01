package project.client;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class FileSender extends Thread{
	private String fileName;
	private int port;
	private Client client;
	
	public FileSender(String file, int port, Client client) {
		this.fileName = file;
		this.port = port;
		this.client = client;
		start();
	}
    public void run() {
        try {
        	ServerSocket serverSocket = new ServerSocket(port);
        	serverSocket.setSoTimeout(5000);
            File file = new File(fileName);
            System.out.println("Server: waiting for a client to connect.");
            client.getLogger().add("Waiting to send file " + file + " on port " + port);
            Socket connectedSocket = serverSocket.accept();
            System.out.println("Server: a client has connected.");
            
            OutputStream out = connectedSocket.getOutputStream();
            client.getLogger().add("Sending file " + file + " on port " + port);
            InputStream in = new BufferedInputStream(new FileInputStream(file) );
            StreamUtil stream = new StreamUtil();
            int bytes = stream.streamCopy(in, out);
            in.close();
            out.flush();
            out.close();
            System.out.println("Finished streaming file "+ file + ", " + bytes + " bytes sent.");
            client.getLogger().add("Finished streaming file "+ file + ", " + bytes + " bytes sent.");
            connectedSocket.close();
            serverSocket.close();
            
		} catch (Exception e) {
			client.getLogger().add("Error during file " + fileName + " sendnig process");
		}
        
    }

    
}