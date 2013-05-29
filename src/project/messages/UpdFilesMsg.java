package project.messages;

import java.util.HashMap;

import project.ClientSession;
import project.FileInfo;
import project.client.Client;
import project.client.FileUtils;

public class UpdFilesMsg extends Message {
	


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UpdFilesMsg(HashMap<String, FileInfo> files) {
		super(files);
	}

	@Override
	public void action(Client cli) {
		cli.getOut().addMessage(new UpdFilesMsg(FileUtils.getFilesFormatted(cli.getWatcher().getMap())));
	}

	@Override
	public void action(ClientSession sess) {
		sess.files = getFiles();
	}
	
	
}
