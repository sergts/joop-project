package project.messages;

import project.ClientSession;
import project.client.Client;

public class OpenUploadConnMsg extends Message {

	
	
 

	public OpenUploadConnMsg(String m) {
	
		  super(m);
	}
	
	@Override
	public void action(Client cli) {
	
		System.out.println("upload message received");
		System.out.println(this.getContents());
		String file = this.getContents().split("<")[0];
		int port = Integer.parseInt(this.getContents().split("<")[1]);
		//System.out.println(this.getContents());
		cli.uploadConn(file, port);
		
	}

	@Override
	public void action(ClientSession sess) {
		
	}

	
}
