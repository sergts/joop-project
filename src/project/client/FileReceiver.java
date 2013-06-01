package project.client;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class FileReceiver extends Thread {
	private String fileName;
	private String ip;
	private int port;
	private String directory;
	private Client client;
	
	
	public FileReceiver(String fileName, String ip, int port, String directory, Client client) {
		this.fileName = fileName;
		this.ip = ip; 
		this.port = port;
		this.directory = directory;
		this.client = client;
		start();
	}
    public void run() {
        try {
        	System.out.println(ip + " " + port);
            Socket clientSocket = new Socket(ip, port);
            
            File tmpDir = new File(directory+"/tmp");
			if (!tmpDir.exists()) {
				tmpDir.mkdir();
			}
			
			tmpDir.mkdir();
			File outputFile = new File(directory+"/tmp", fileName);
			
            System.out.println("Client: connected to server.");
            client.getLogger().add("Connected to uploader: " + ip + ":" + port);
            
            InputStream in = clientSocket.getInputStream();
            OutputStream out = new BufferedOutputStream(new FileOutputStream(outputFile) );
             
            StreamUtil stream = new StreamUtil();
            int bytes;
            try {
            	bytes = stream.streamCopy(in, out);
				String newPath = outputFile.getAbsolutePath().replaceAll("tmp\\\\","");
				
				File newOutputFile = new File(newPath);
				org.apache.commons.io.FileUtils.copyFile(outputFile, newOutputFile);
				System.out.println("Client: received " + outputFile + ", " + bytes + " bytes read.");
	            client.getLogger().add("Received " + outputFile + ", " + bytes + " bytes read.");
            }catch(Exception e){
            	System.out.println("Error during file " + fileName + " receiving.");
	            client.getLogger().add("Error during file " + fileName + " receiving.");
            	
            }finally{
				
				in.close();
				out.flush();
				out.close();
				
				clientSocket.close();
				outputFile.delete();
			}
		} catch (Exception e) {
			
			client.getLogger().add("Error during file " + fileName + " receiving process.");
		}
    }
}
