package project.messages;

import java.io.Serializable;
import java.util.HashMap;
import project.FileInfo;
import project.ClientSession;
import project.client.Client;




public abstract class Message implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	String contents;
	String to;
	HashMap<String, FileInfo> files;
	
	
	public HashMap<String, FileInfo> getFiles() {
		return files;
	}
	public void setFiles(HashMap<String, FileInfo> files) {
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
	public Message(HashMap<String, FileInfo> files){
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
