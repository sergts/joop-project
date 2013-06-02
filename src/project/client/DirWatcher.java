package project.client;



import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.io.*;

import project.messages.UpdFilesMsg;
import project.utils.FileInfo;





public class DirWatcher extends Thread {
	private String path;
	private HashMap<File, Long> dir = new HashMap<File, Long>();
	private ConcurrentHashMap<String, FileInfo> fileNames;
	private File directory;
	private long lastmod;
	private Client client;
	private boolean run = true;
	

	

	public DirWatcher(String path, Client client) {
		
		this.path = path;
		System.out.println("Sharing " + path);
		client.getLogger().add("Sharing " + path);
		directory = new File(path);
		fileNames =  getFilesFormatted(directory.listFiles());
		client.getOut().addMessage(new UpdFilesMsg(fileNames));
		lastmod = directory.lastModified();
		this.client = client;

		
	
		start();
	}

	public HashMap<File, Long> getMap() {
		return dir;
	}

	public void run() {
		long modified;
		while (run) {
			modified = directory.lastModified();
			if(modified > lastmod + 3000){
				fileNames =  getFilesFormatted(directory.listFiles());
				lastmod = modified;
				client.getOut().addMessage(new UpdFilesMsg(fileNames));
				
			}
			try{
				Thread.sleep(2000);
			}catch(Exception e){}
			
			
		}
		
	}
	
	public ConcurrentHashMap<String, FileInfo> getFiles(){
		return fileNames;
	}
	
	public void stopThread(){
		this.run = false;
	}
	
	
	

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