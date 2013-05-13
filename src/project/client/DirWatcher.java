package project.client;

import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.io.*;

import project.FileInfo;

public class DirWatcher extends Thread {
	private String path;
	private File filesArray[];
	private HashMap<File, Long> dir = new HashMap<File, Long>();

	public DirWatcher(String path) {
		this(path, "");
	}

	public DirWatcher(String path, String filter) {
		this.path = path;

		filesArray = new File(path).listFiles();

		// transfer to the hashmap be used a reference and keep the
		// lastModfied value
		for (int i = 0; i < filesArray.length; i++) {
			dir.put(filesArray[i], new Long(filesArray[i].lastModified()));
		}
		start();
	}

	public HashMap<File, Long> getMap() {
		return dir;
	}

	public void run() {

		while (true) {
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

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	
	public HashMap<String, FileInfo> getFilesInCurrentDirectory() {

		HashMap<String, FileInfo> filesInDirectoryMap = new HashMap<String, FileInfo>();

		for (File file : dir.keySet()) {
			long size = dir.get(file);
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

	private static String byteArray2Hex(byte[] hash) {
		Formatter formatter = new Formatter();
		for (byte b : hash) {
			formatter.format("%02x", b);
		}
		return formatter.toString();
	}

}