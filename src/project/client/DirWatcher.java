package project.client;



import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.io.*;
import java.lang.reflect.Constructor;

import project.client.utils.DirectoryNotFoundException;
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
	private ConcurrentHashMap<String, FileInfo> filesMap;
	private File directory;
	private long lastmod;
	private Client client;
	private boolean run = true;
	private static int MODIFIED_TIME_DIFFERENCE = 1000; //ms
	private static int THREAD_SLEEP_TIME = 2000; //ms
	

	

	/**
	 * {@link Constructor}
	 * @param path
	 * @param client
	 */
	public DirWatcher(String path, Client client) {
		
		this.path = path;
		client.getLogger().add("Sharing " + path);
		directory = new File(path);
		filesMap =  getFilesFormatted(directory.listFiles());
		client.getOut().addMessage(new UpdFilesMsg(filesMap));
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
		try{
			while (run) {
				
				if(!directory.exists()) throw new DirectoryNotFoundException();
				modified = directory.lastModified();
				if(modified > lastmod + MODIFIED_TIME_DIFFERENCE){
					filesMap =  getFilesFormatted(directory.listFiles());
					lastmod = modified;
					client.getOut().addMessage(new UpdFilesMsg(filesMap));
						
				}
				try{
					Thread.sleep(THREAD_SLEEP_TIME);
				}catch(Exception e){}
			}
		}catch(DirectoryNotFoundException e){
				client.getLogger().add("Directory " + path + " not found");
				client.getOut().addMessage(new UpdFilesMsg(new ConcurrentHashMap<String, FileInfo>()));
		}
			
			
			
		
		
	}
	
	/**
	 * @return - files with file information map
	 */
	public ConcurrentHashMap<String, FileInfo> getFiles(){
		return filesMap;
	}
	
	/**
	 * Stops this thread's main while cycle
	 */
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