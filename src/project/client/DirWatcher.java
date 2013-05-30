package project.client;


import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.io.*;

import project.FileInfo;
import project.messages.UpdFilesMsg;



public class DirWatcher extends Thread {
	private String path;
	private File filesArray[];
	private HashMap<File, Long> dir = new HashMap<File, Long>();
	private ConcurrentHashMap<String, FileInfo> fileNames;
	private File directory;
	private long lastmod;
	private Client client;

	

	public DirWatcher(String path, Client client) {
		this.path = path;
		directory = new File(path);
		fileNames =  getFilesFormatted(directory.listFiles());
		client.getOut().addMessage(new UpdFilesMsg(fileNames));
		lastmod = directory.lastModified();
		this.client = client;
		//filesArray = new File(path).listFiles();
		
		

		// transfer to the hashmap be used a reference and keep the
		// lastModfied value
		/*
		for (int i = 0; i < filesArray.length; i++) {
			dir.put(filesArray[i], new Long(filesArray[i].lastModified()));
		}
		*/
		start();
	}

	public HashMap<File, Long> getMap() {
		return dir;
	}

	public void run() {
		long modified;
		while (true) {
			
			modified = directory.lastModified();
			if(modified > lastmod){
				fileNames =  getFilesFormatted(directory.listFiles());
				lastmod = modified;
				client.getOut().addMessage(new UpdFilesMsg(fileNames));
				
			}
			
			/*
			HashSet<File> checkedFiles = new HashSet<File>();
			filesArray = new File(path).listFiles();

			// scan the files and check for modification/addition
			for (int i = 0; i < filesArray.length; i++) {
				Long current = (Long) dir.get(filesArray[i]);
				checkedFiles.add(filesArray[i]);
				if (current == null) {
					// new file
					dir.put(filesArray[i],
							new Long(filesArray[i].lastModified()));

				} else if (current.longValue() != filesArray[i].lastModified()) {
					// modified file
					dir.put(filesArray[i],
							new Long(filesArray[i].lastModified()));

				}
			}

			// now check for deleted files
			Set ref = ((HashMap)dir.clone()).keySet();
			ref.removeAll((Set<File>) checkedFiles);
			Iterator<File> it = ref.iterator();
			while (it.hasNext()) {
				File deletedFile = (File) it.next();
				dir.remove(deletedFile);

			}
*/
			
			
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public ConcurrentHashMap<String, FileInfo> getFiles(){
		return fileNames;
	}
	
	

	public ConcurrentHashMap<String, FileInfo> getFilesFormatted(File[] files) {

		ConcurrentHashMap<String, FileInfo> filesInDirectoryMap = new ConcurrentHashMap<String, FileInfo>();

		for (File file : files) {
			if(file.isFile() && (!file.getName().substring(file.getName().length() - 4).equals(".tmp"))){
				long size = file.length();
				String path = file.getAbsolutePath();
				String checkSum;
				try {
					checkSum = getFileCheckSum(file);
					FileInfo fileInfo = new FileInfo(size, checkSum, path);
					filesInDirectoryMap.put(file.getName(), fileInfo);
				} catch (NoSuchAlgorithmException e) {
	
					e.printStackTrace();
				} catch (FileNotFoundException e) {
	
					e.printStackTrace();
				}
			}
		}

		return filesInDirectoryMap;

	}

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