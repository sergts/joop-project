package project.client;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class FileReceiver extends Thread {
	String fileName;
	String ip;
	int port;
	String directory;
	public FileReceiver(String fileName, String ip, int port, String directory) {
		this.fileName = fileName;
		this.ip = ip; 
		this.port = port;
		this.directory = directory;
		start();
	}
    public void run() {
        try {
        	System.out.println(ip + " " + port);
        	Thread.sleep(500);
            Socket clientSocket = new Socket(ip, port);
            File outputFile = new File(directory+"\\"+fileName);
            System.out.println("Client: connected to server.");
            InputStream in = clientSocket.getInputStream();
            OutputStream out = new BufferedOutputStream(new FileOutputStream(outputFile) );
             
            StreamUtil stream = new StreamUtil();
            int bytes = stream.streamCopy(in, out);
            
            //outputFile.renameTo(new File(fileName));
             
            System.out.println("Client: received " + outputFile + ", " + bytes + " bytes read.");
             
            
            in.close();
            out.flush();
            out.close();
               
            clientSocket.close();
            System.out.println("Client: finished");
		} catch (Exception e) {
			
			e.printStackTrace();
		}
    }
}
