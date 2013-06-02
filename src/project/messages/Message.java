package project.messages;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.concurrent.ConcurrentHashMap;


import project.utils.FileInfo;




/**
 * This class implements the logic of the file transfer
 * protocol basic object used for communication between client 
 * and client session on server
 *
 */
public abstract class Message implements Serializable, Actable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String contents;
	private String to;
	private ConcurrentHashMap<String, FileInfo> files;
	
	
	/**
	 * @return - filenames and file info
	 */
	public ConcurrentHashMap<String, FileInfo> getFiles() {
		return files;
	}
	/**
	 * set filenames and fileinfo to be transferred
	 * @param files - filenames and fileinfo to be transferred
	 */
	public void setFiles(ConcurrentHashMap<String, FileInfo> files) {
		this.files = files;
	}
	
	/**
	 * {@link Constructor}
	 */
	public Message(){
		contents = null;
		to = null;
	}
	
	/**
	 * @param m - message
	 * @param to - receiver of the message
	 */
	public Message(String m, String to){
		contents = m;
		this.to = to;
		
	}
	public Message(String m){
		contents = m;
		this.to = null;
		
	}
	/**
	 * {@link Constructor}
	 * @param files  - filenames and fileinfo to be transferred with msg
	 */
	public Message(ConcurrentHashMap<String, FileInfo> files){
		this.files = files;
		contents = null;
		this.to = null;
	}
	
	/**
	 * @return - receiver of the message
	 */
	public String getTo(){
		return to;
	}
	/**
	 * @return - message contents (text)
	 */
	public String getContents(){
		return contents;
	}
	
	
	
	
}
