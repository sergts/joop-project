package project;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;




public class Message implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	String contents;
	String to;
	MessageType messageType;
	HashMap<String, FileInfo> filesInCurrentDirectory;
	
	
	public HashMap<String, FileInfo> getFilesInCurrentDirectory() {
		return filesInCurrentDirectory;
	}
	public void setFilesInCurrentDirectory(HashMap<String, FileInfo> filesInCurrentDirectory) {
		this.filesInCurrentDirectory = filesInCurrentDirectory;
	}
	
	public Message(String m, String to, MessageType messageType){
		contents = m;
		this.to = to;
		this.messageType = messageType;
	}
	
	
	public Message(String m, MessageType messageType){
		contents = m;
		this.to = null;
		this.messageType = messageType;
	}
	
	public Message(HashMap<String, FileInfo> files, MessageType messageType){
		filesInCurrentDirectory = files;
		contents = null;
		this.to = null;
		this.messageType = messageType;
	}
	public Message(MessageType messageType){
		this.messageType = messageType;
	}
	public String getTo(){
		return to;
	}
	public String getContents(){
		return contents;
	}
	public MessageType getMessageType() {
		return messageType;
	}
	
	
}
