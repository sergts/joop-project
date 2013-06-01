package project.messages;

import java.util.Iterator;

import project.ClientSession;
import project.client.Client;

public class OpenDownloadConnMsg extends Message{

	

	

	public OpenDownloadConnMsg(String m){
		super(m);
	}
	
	public OpenDownloadConnMsg(String m, String to){
		super(m, to);
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
		if(to!=null){
			Iterator<ClientSession> activeSessions = sess.getActiveSessions().iterator();
			System.out.println(" opendwncon sessions iterator accessed");
			while (activeSessions.hasNext()) {
				ClientSession session = activeSessions.next();
				if (session.getName().equals(this.to)) {
					session.sendMessage(this);
					
				}
			}
			
			
			
			
			
		}
		
	}

	
}
