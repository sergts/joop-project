package project.messages;

import project.ClientSession;
import project.client.Client;

public class OpenUploadConnMsg extends Message {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
  public OpenUploadConnMsg(String m) {
	// TODO Auto-generated constructor stub
	  super(m);
}
	
	@Override
	public void action(Client cli) {
		// TODO Auto-generated method stub
		
		String file = this.getContents().split(" ")[0];
		int port = Integer.parseInt(this.getContents().split(" ")[1]);
		System.out.println(this.getContents());
		cli.uploadConn(file, port);
		
	}

	@Override
	public void action(ClientSession sess) {
		// TODO Auto-generated method stub
		
	}

	
}
