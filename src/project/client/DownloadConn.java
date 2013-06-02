package project.client;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.net.Socket;


import project.client.utils.ByteConverter;
import project.client.utils.StreamUtil;
import project.messages.LogMessage;

/**
 * Implements the logic of the class responsible for
 * file transfer on the receiving side
 * @author Roman
 *
 */
public class DownloadConn extends Thread {
	private String fileName;
	private String ip;
	private int port;
	private String directory;
	private Client client;
	private static final int START_INDEX = 0;

	
	
	/**
	 * {@link Constructor}
	 * @param fileName - name of file to download
	 * @param ip - ip of a client to download from
	 * @param port - port used for transer
	 * @param directory - directory to store the file (shared dir)
	 * @param client - client downloading a file
	 */
	public DownloadConn(String fileName, String ip, int port, String directory, Client client) {
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
            // check if directory for download files exist
			File tmpDir = new File(directory+"//Downloads");
			if (!tmpDir.exists()) { // if not, create
				tmpDir.mkdir(); 
			}
			tmpDir.mkdir();
			
			File checkOutputFile = new File(directory+"//Downloads", fileName); 
			 // check if file with the specified name already exist in this dir
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
            
            
            InputStream in = clientSocket.getInputStream(); // read from
            OutputStream out = new BufferedOutputStream(new FileOutputStream(outputFile) ); // write
             
            StreamUtil stream = new StreamUtil();
            int bytes;
            try {
            	bytes = stream.streamCopy(in, out);
				
				
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
    
    
}

