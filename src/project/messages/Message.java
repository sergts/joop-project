package project.messages;

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;


import project.utils.FileInfo;




public abstract class Message implements Serializable, Actable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String contents;
	private String to;
	private ConcurrentHashMap<String, FileInfo> files;
	
	
	public ConcurrentHashMap<String, FileInfo> getFiles() {
		return files;
	}
	public void setFiles(ConcurrentHashMap<String, FileInfo> files) {
		this.files = files;
	}
	
	public Message(){
		contents = null;
		to = null;
	}
	
	public Message(String m, String to){
		contents = m;
		this.to = to;
		
	}
	public Message(String m){
		contents = m;
		this.to = null;
		
	}
	public Message(ConcurrentHashMap<String, FileInfo> files){
		this.files = files;
		contents = null;
		this.to = null;
	}
	
	public String getTo(){
		return to;
	}
	public String getContents(){
		return contents;
	}
	
	
	
	
}
