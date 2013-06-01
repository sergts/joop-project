package project.messages;

import java.util.concurrent.ConcurrentHashMap;

import project.ClientSession;
import project.client.Client;
import project.utils.FileInfo;

public class UpdFilesMsg extends Message {
	


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UpdFilesMsg(ConcurrentHashMap<String, FileInfo> files) {
		super(files);
	}
	public UpdFilesMsg(){
		super();
	}

	@Override
	public void action(Client cli) {
		//cli.getOut().addMessage(new UpdFilesMsg( cli.getWatcher().getFilesFormatted(cli.getWatcher().getMap())  ));
	}

	@Override
	public void action(ClientSession sess) {
		sess.files = getFiles();
	}
	
	
}
