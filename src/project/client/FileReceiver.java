package project.client;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.channels.FileChannel;

import project.messages.LogMessage;
import project.utils.ByteConverter;

public class FileReceiver extends Thread {
	private String fileName;
	private String ip;
	private int port;
	private String directory;
	private Client client;
	private static final int START_INDEX = 0;

	
	
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
            Socket clientSocket = new Socket(ip, port);
            File outputFile;
			File tmpDir = new File(directory+"//Downloads");
			if (!tmpDir.exists()) {
				tmpDir.mkdir();
			}
			tmpDir.mkdir();
			
			File checkOutputFile = new File(directory+"//Downloads", fileName);
			if(!checkOutputFile.exists()){
			outputFile = new File(directory+"//Downloads", fileName);
			}
			else{
				int indexOfExtension = fileName.lastIndexOf('.');
				String plainName = fileName.substring(START_INDEX, indexOfExtension);
				String extension = fileName.substring(indexOfExtension );
				
				int i = 1;
				while(true){
					checkOutputFile = new File(directory+"//Downloads", plainName +"("+i+")"+extension);
					if(!checkOutputFile.exists()){
						outputFile = new File(directory+"//Downloads", plainName +"("+i+")"+extension);
						break;
					}
					else{
						i++;
					}
				}
			}
            client.getLogger().add("Connected to uploader: " + ip + ":" + port);
            
            InputStream in = clientSocket.getInputStream();
            OutputStream out = new BufferedOutputStream(new FileOutputStream(outputFile) );
             
            StreamUtil stream = new StreamUtil();
            int bytes;
            try {
            	bytes = stream.streamCopy(in, out);
				//String newPath = outputFile.getAbsolutePath().replaceAll("tmp\\\\","");
				
				
				
				//File newOutputFile = new File(directory, fileName);
				//copyFile(outputFile, newOutputFile, client);
				
	            client.getLogger().add("Downloaded " + outputFile + ", " + ByteConverter.formatSize(bytes) + " read.");
	            client.getOut().addMessage(new LogMessage(client.getName() + " downloaded " + outputFile + ", " + ByteConverter.formatSize(bytes) + " read.") );
            }catch(Exception e){
	            client.getLogger().add("Error during file " + fileName + " downloaing.");
	            client.getOut().addMessage(new LogMessage(client.getName()+
	            		" has failed to receive " + outputFile ) );
            	
            }finally{
				
				in.close();
				out.flush();
				out.close();
				
				clientSocket.close();
				//outputFile.delete();
				
			}
		} catch (Exception e) {
			
			client.getLogger().add("Error during file " + fileName + " downloading process.");
		}finally{
			client.removePort(port);
		}
    }
    
    public static void copyFile(File sourceFile, File destFile, Client client) throws IOException {
    	
        if(!destFile.exists()) {
            destFile.createNewFile();
        }

        FileChannel source = null;
        FileChannel destination = null;

        try {
            source = new FileInputStream(sourceFile).getChannel();
            destination = new FileOutputStream(destFile).getChannel();
            destination.transferFrom(source, 0, source.size());
        }
        finally {
            if(source != null) {
                source.close();
            }
            if(destination != null) {
                destination.close();
            }
            
        }
    }
}

