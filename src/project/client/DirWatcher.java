package project.client;


import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.io.*;

import project.messages.UpdFilesMsg;
import project.utils.FileInfo;




public class DirWatcher extends Thread {
	private String path;
	private File filesArray[];
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
			if(modified > lastmod && run){
				fileNames =  getFilesFormatted(directory.listFiles());
				lastmod = modified;
				client.getOut().addMessage(new UpdFilesMsg(fileNames));
				
			}
		}
		try{
			Thread.sleep(2000);
		}catch(Exception e){}
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
			if(file.isFile() && (!file.getName().substring(file.getName().length() - 4).equals(".tmp"))){
				long size = file.length();
				String path = file.getAbsolutePath();
				FileInfo fileInfo = new FileInfo(size, path);
				filesInDirectoryMap.put(file.getName(), fileInfo);
			}
		}

		return filesInDirectoryMap;

	}

	@SuppressWarnings("unused")
	private String getFileCheckSum(File file) throws NoSuchAlgorithmException,
			FileNotFoundException {

		MessageDigest md5 = MessageDigest.getInstance("MD5");
		FileInputStream fis = new FileInputStream(file);
		BufferedInputStream bis = new BufferedInputStream(fis);
		DigestInputStream dis = new DigestInputStream(bis, md5);

		// read the file and update the hash calculation
		try {
			while (dis.read() != -1)
				;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// get the hash value as byte array
		byte[] hash = md5.digest();

		return byteArray2Hex(hash);

	}

	
	private String byteArray2Hex(byte[] hash) {
		
		Formatter formatter = new Formatter();
		for (byte b : hash) {
			formatter.format("%02x", b);
		}
		return formatter.toString();
	}

	


}