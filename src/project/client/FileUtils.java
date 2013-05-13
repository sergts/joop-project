package project.client;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.HashMap;

import project.FileInfo;

public class FileUtils {
	
	public static HashMap<String, FileInfo> getFilesFormatted(HashMap<File, Long> dir) {

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

	private static String getFileCheckSum(File file) throws NoSuchAlgorithmException,
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
