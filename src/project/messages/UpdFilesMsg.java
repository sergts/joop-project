package project.messages;

import java.util.concurrent.ConcurrentHashMap;

import project.client.Client;
import project.server.ClientSession;
import project.utils.FileInfo;

/**
 *This class implements the logic of the message used to
 *update information about files of clients by dirwatcher
 *
 */
@SuppressWarnings("serial")
public class UpdFilesMsg extends Message {
	


	

	/**
	 * @param files - files the client has (filename, fileinfo)
	 */
	public UpdFilesMsg(ConcurrentHashMap<String, FileInfo> files) {
		super(files);
	}
	

	@Override
	public void action(Client cli) {
		
	}

	@Override
	public void action(ClientSession sess) {
		sess.files = getFiles();
	}
	
	
}
