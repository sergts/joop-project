package project.messages;

import java.util.concurrent.ConcurrentHashMap;

import project.client.Client;
import project.server.ClientSession;
import project.utils.FileInfo;

@SuppressWarnings("serial")
public class UpdFilesMsg extends Message {
	


	

	public UpdFilesMsg(ConcurrentHashMap<String, FileInfo> files) {
		super(files);
	}
	public UpdFilesMsg(){
		super();
	}

	@Override
	public void action(Client cli) {
		
	}

	@Override
	public void action(ClientSession sess) {
		sess.files = getFiles();
	}
	
	
}
