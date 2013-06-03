package project.client;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.net.ServerSocket;
import java.net.Socket;

import project.client.utils.ByteConverter;
import project.client.utils.StreamUtil;
import project.messages.LogMessage;
import project.messages.OpenDownloadConnMsg;
import project.messages.PersonalMessage;

/**
 * This class implements the logic of the sending side 
 * during the file transfer
 *
 */
public class UploadConn extends Thread{
	private String fileName;
	private int port;
	private Client client;
	private String downloadInfo;
	private static final String DELIMITER = "<";
	
	/**
	 * {@link Constructor}
	 * @param file -  file to be sent
	 * @param port -  port used for transfer connection
	 * @param client - client who sends the file
	 */
	public UploadConn(String file, int port, Client client, String downloadInfo) {
		this.fileName = file;
		this.port = port;
		this.client = client;
		this.downloadInfo = downloadInfo;
		start();
	}
    public void run() {
        try {
        	File file = new File(fileName);
        	if(!file.exists()) throw new FileNotFoundException();
        	
        	client.getOut().addMessage(new OpenDownloadConnMsg( 
    				downloadInfo.substring(downloadInfo.indexOf(DELIMITER) + 1) + DELIMITER+port, 
    				downloadInfo.substring(0, downloadInfo.indexOf(DELIMITER)) ));
        	
        	ServerSocket serverSocket = new ServerSocket(port);
        	serverSocket.setSoTimeout(10000);
            client.getLogger().add("Waiting to upload file " + file + " on port " + port);
            Socket connectedSocket = serverSocket.accept(); 
            OutputStream out = connectedSocket.getOutputStream();
            client.getLogger().add("Sending file " + file + " on port " + port);
            InputStream in = new BufferedInputStream(new FileInputStream(file) );
            StreamUtil stream = new StreamUtil();
            int bytes = stream.streamCopy(in, out);
            in.close();
            out.flush();
            out.close();
            client.getLogger().add("Finished uploading file "+ file + ", " + ByteConverter.formatSize(bytes) + " sent.");
            client.getOut().addMessage(new LogMessage(client.getName()+" finished uploading file "+ file + ", " + ByteConverter.formatSize(bytes) + " sent."));
            connectedSocket.close();
            serverSocket.close();
            
		} catch(FileNotFoundException e){
			client.getLogger().add("File for uploading " + fileName + " not found");
			
			client.getOut().addMessage(new PersonalMessage("File "+ fileName + " of user " + client.getName() + " was not found.",
					downloadInfo.substring(0, downloadInfo.indexOf(DELIMITER))));
		}
        catch (Exception e) {
			client.getLogger().add("Error during file " + fileName + " uploading process");
			client.getOut().addMessage(new LogMessage(client.getName()+" has failed to upload " +fileName));
		}finally{
			client.removePort(port);
		}
        
    }

    
}