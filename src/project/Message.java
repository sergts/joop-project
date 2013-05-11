package project;

import java.io.Serializable;

public class Message implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String contents;
	String to;	
	Message(String m, String to){
		contents = m;
		this.to = to;
	}
	public Message(String m){
		contents = m;
		this.to = null;
	}
	public String getTo(){
		return to;
	}
	public String getContents(){
		return contents;
	}
}
