package project.client;



import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.io.*;
import java.lang.reflect.Constructor;

import project.messages.UpdFilesMsg;
import project.utils.FileInfo;





/**
 * This class implements the logic of an object responsible
 * for observing the shared directory
 *
 */
public class DirWatcher extends Thread {
	private String path;
	private HashMap<File, Long> dir = new HashMap<File, Long>();
	private ConcurrentHashMap<String, FileInfo> fileNames;
	private File directory;
	private long lastmod;
	private Client client;
	private boolean run = true;
	private static int MODIFIED_TIME_DIFFERENCE = 3000;
	

	

	/**
	 * {@link Constructor}
	 * @param path
	 * @param client
	 */
	public DirWatcher(String path, Client client) {
		
		this.path = path;
		client.getLogger().add("Sharing " + path);
		directory = new File(path);
		fileNames =  getFilesFormatted(directory.listFiles());
		client.getOut().addMessage(new UpdFilesMsg(fileNames));
		lastmod = directory.lastModified();
		this.client = client;

		
	
		start();
	}

	/**
	 * @return - directory of files w/ size  map
	 */
	public HashMap<File, Long> getMap() {
		return dir;
	}

	public void run() {
		long modified;
		while (run) {
			modified = directory.lastModified();
			if(modified > lastmod + MODIFIED_TIME_DIFFERENCE){
				fileNames =  getFilesFormatted(directory.listFiles());
				lastmod = modified;
				client.getOut().addMessage(new UpdFilesMsg(fileNames));
				
			}
			try{
				Thread.sleep(2000);
			}catch(Exception e){}
			
			
		}
		
	}
	
	/**
	 * @return - files with file information map
	 */
	public ConcurrentHashMap<String, FileInfo> getFiles(){
		return fileNames;
	}
	
	public void stopThread(){
		this.run = false;
	}
	
	
	

	/**
	 * converts an array of files into a map of files with fileinfo
	 * @param files - file array in dir
	 * @return - map of files with fileinfo
	 */
	public ConcurrentHashMap<String, FileInfo> getFilesFormatted(File[] files) {

		ConcurrentHashMap<String, FileInfo> filesInDirectoryMap = new ConcurrentHashMap<String, FileInfo>();

		for (File file : files) {
			if(file.isFile()){
				long size = file.length();
				String fullpath = this.path + "//" + file.getName();
				FileInfo fileInfo = new FileInfo(size, fullpath);
				filesInDirectoryMap.put(file.getName(), fileInfo);
			}
		}

		return filesInDirectoryMap;

	}

	


}