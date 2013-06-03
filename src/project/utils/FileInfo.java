package project.utils;

import java.io.Serializable;

public class FileInfo implements Serializable {

	/**
	 * This class implement the logic of an object used to
	 * store information about a file
	 */
	private static final long serialVersionUID = 1L;
	long size;
	String checkSum;
	String absolutePath;
	String user;
	
	/**
	 * @param size - file size
	 * @param absolutePath - file absolute path
	 */
	public FileInfo(long size, String absolutePath ) {
		
		this.size=size;
		this.absolutePath=absolutePath;
		
	}
	
	/**
	 * @param size - file size
	 * @param checkSum - file checksum
	 * @param absolutePath - file absolute path
	 */
	public FileInfo(long size, String checkSum, String absolutePath ) {
		
		this.size=size;
		this.checkSum=checkSum;
		this.absolutePath=absolutePath;
		
	}
	
	
	/**
	 * @return file owner
	 */
	public String getUser(){
		return user;
	}
	

	/**
	 * @return file size
	 */
	public long getSize() {
		return size;
	}

	/**
	 * @return file checksum
	 */
	public String getCheckSum() {
		return checkSum;
	}

	/**
	 * @return file path
	 */
	public String getPath() {
		return absolutePath;
	}
	
	public boolean equals(FileInfo another){
		if(this.size == another.size && this.absolutePath.equals(another.absolutePath))
			return true;
		return false;
	}

	
	
	
	
	
	
}
