package project.messages;

import java.util.Iterator;

import project.ClientSession;
import project.client.Client;

public class FilesQuery extends Message {

	@Override
	public void action(Client cli) {
		cli.getInQueue().add(getContents());
		
	}

	@Override
	public void action(ClientSession sess) {
		String files = "";
		Iterator<ClientSession> clients = sess.activeSessions.iterator();
		
		while (clients.hasNext()) {
			ClientSession client = clients.next();
			if(client.isAlive()){
				files += client.getName() + " : \n";
				for(String f : client.files.keySet()){
					files +=  "   " + f + "\n";
				}
			}
			
		}
		
		sess.sendMessage(new TextMsg(files));
		
	}
	
}
