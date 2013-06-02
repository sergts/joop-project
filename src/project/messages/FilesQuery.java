package project.messages;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

import project.client.Client;
import project.server.ClientSession;
import project.utils.FileInfo;


/**
 * This message implements the logic of the message used 
 * to gain information about files of clients on the
 *
 */
@SuppressWarnings("serial")
public class FilesQuery extends Message {

	


	public FilesQuery(String search){
		super(search);
	}
	
	public FilesQuery() {
		super("");
	}

	@Override
	public void action(Client cli) {
		if(getFiles() != null){
			cli.setFilesOnServer(getFiles());
		}
		
	}

	@Override
	public void action(ClientSession sess) {
		
		String filter = getContents();
		
		ConcurrentHashMap<String, FileInfo> filesMap = new ConcurrentHashMap<String, FileInfo>();
		Iterator<ClientSession> clients = sess.activeSessions.iteratorSessions();
		String key;
		while (clients.hasNext()) {
			ClientSession client = clients.next();
			if(client.isAlive()){
				for(String f : client.files.keySet()){
					if(f.toLowerCase().indexOf(filter.toLowerCase()) != -1){
						key = f + " : " + client.getName();
						filesMap.put(key, client.files.get(f));
					}
					
				}
				
			}
			
		}
		
		this.setFiles(filesMap);
		sess.sendMessage(this);

		
	}
	
}
