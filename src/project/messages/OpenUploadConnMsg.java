package project.messages;

import project.client.Client;
import project.server.ClientSession;

/**
 *This class implements the logic of the message initiating
 *file transfer on the sending side
 *
 */
@SuppressWarnings("serial")
public class OpenUploadConnMsg extends Message {

	
	
 



	public OpenUploadConnMsg(String m) {
	
		  super(m);
	}
	
	/**
	 * @param m - message contents
	 * @param to - receiver - filename - ip of thr owner
	 */
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
