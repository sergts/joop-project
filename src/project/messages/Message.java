package project.messages;

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;

import project.ClientSession;
import project.client.Client;
import project.utils.FileInfo;




public abstract class Message implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	String contents;
	String to;
	ConcurrentHashMap<String, FileInfo> files;
	
	
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
	
	public abstract void action(Client cli);
	public abstract void action(ClientSession sess);
	
	
}
