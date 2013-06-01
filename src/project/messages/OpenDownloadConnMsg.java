package project.messages;

import project.ClientSession;
import project.client.Client;

public class OpenDownloadConnMsg extends Message{

	

	

	public OpenDownloadConnMsg(String m){
		super(m);
		//System.out.println("msg created");
	}
	
	@Override
	public void action(Client cli) {
		
		System.out.println("download message received");
		System.out.println(this.getContents());
		String ip = this.getContents().split("<")[0];
		String file = this.getContents().split("<")[1];
		int port = Integer.parseInt(this.getContents().split("<")[2]);
		//System.out.println(this.getContents());
		cli.downloadConn(file, ip,  port);
		
	}

	@Override
	public void action(ClientSession sess) {
		
		
	}

	
}
