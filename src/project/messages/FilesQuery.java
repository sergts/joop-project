package project.messages;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

import project.ClientSession;
import project.client.Client;
import project.utils.FileInfo;

public class FilesQuery extends Message {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

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
		Iterator<ClientSession> clients = sess.activeSessions.iterator();
		String key;
		while (clients.hasNext()) {
			ClientSession client = clients.next();
			if(client.isAlive()){
				for(String f : client.files.keySet()){
					if(f.indexOf(filter) != -1){
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
