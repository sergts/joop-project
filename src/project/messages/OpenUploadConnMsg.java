package project.messages;

import project.client.Client;
import project.server.ClientSession;

@SuppressWarnings("serial")
public class OpenUploadConnMsg extends Message {

	
	
 



	public OpenUploadConnMsg(String m) {
	
		  super(m);
	}
	
	public OpenUploadConnMsg(String m, String to) {
		
		  super(m, to);
	}
	
	@Override
	public void action(Client cli) {
	
		System.out.println("upload message received");
		System.out.println(this.getContents());
		 
		cli.uploadConn(this.getContents(), getTo());
		
		
	}

	@Override
	public void action(ClientSession sess) {
		
	}

	
}
