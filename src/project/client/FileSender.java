package project.client;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class FileSender extends Thread {
	String fileName;
	int port;
	public FileSender(String file, int port) {
		this.fileName = file;
		this.port = port;
		start();
	}
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            File file = new File(fileName);
             
            System.out.println("Server: waiting for a client to connect.");
             
            Socket connectedSocket = serverSocket.accept();
            System.out.println("Server: a client has connected.");
            OutputStream out = connectedSocket.getOutputStream();
            InputStream in = new BufferedInputStream(new FileInputStream(file) );
             
         
            int bytes = StreamUtil.streamCopy(in, out);
             
            in.close();
            out.flush();
            out.close();
             
            System.out.println("Server: finished streaming file "+ file + ", " + bytes + " bytes sent.");
             
            connectedSocket.close();
             
            serverSocket.close();
            System.out.println("Server: finished.");
		} catch (Exception e) {
			
			e.printStackTrace();
		}
    }

    
}