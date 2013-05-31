package project.messages;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import project.ClientSession;
import project.FileInfo;
import project.client.Client;

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
		
		String files = "";
		if(getFiles() != null){
			for(String file : getFiles().keySet()){
				files += file + "\n";
			}
			
			cli.getInQueue().add(files);
			
			cli.setFilesOnServer(getFiles());
		}else cli.getInQueue().add("null");
		
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
				
				//files += client.getName() + " : \n";
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
		
		
		
		
		/*String files = "";
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
		sess.sendMessage(new TextMsg(files));*/
		
	}
	
}
