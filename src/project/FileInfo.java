package project;

import java.io.Serializable;

public class FileInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	long size;
	String checkSum;
	String absolutePath;
	
	public FileInfo(long size, String checkSum, String absolutePath ) {
		
		this.size=size;
		this.checkSum=checkSum;
		this.absolutePath=absolutePath;
	}

	public long getSize() {
		return size;
	}

	public String getCheckSum() {
		return checkSum;
	}

	public String getPath() {
		return absolutePath;
	}

	
	
	
	
	
	
}
