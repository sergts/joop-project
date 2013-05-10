package project;

public class Message {
	String contents;
	String to;	
	Message(String m, String to){
		contents = m;
		this.to = to;
	}
	Message(String m){
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
