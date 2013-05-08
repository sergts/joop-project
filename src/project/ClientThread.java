package project;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ClientThread extends Thread {
	String fileName;
	public ClientThread(String fileName) {
		this.fileName = fileName;
		start();
	}
    public void run() {
        try {
            Socket clientSocket = new Socket("82.147.183.139", 7777);
            File outputFile = new File(fileName);
            System.out.println("Client: connected to server.");
            InputStream in = clientSocket.getInputStream();
            OutputStream out = new BufferedOutputStream(new FileOutputStream(outputFile) );
             
            int bytes = StreamUtil.streamCopy(in, out);
             
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