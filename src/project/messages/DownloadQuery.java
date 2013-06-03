package project.messages;


import project.client.Client;
import project.server.ClientSession;


/**
 * This message implements the logic of message sent
 * by a client intending to download a file from another client
 *
 */
@SuppressWarnings("serial")
public class DownloadQuery extends Message {

	
	private static String DELIMITER = "<";


	/**
	 * @param m -  filename of the file to download
	 * @param to - owner of the file
	 */
	public DownloadQuery(String m, String to) {
		super(m, to);
       
	}

	@Override
	public void action(Client cli) {
	}

	
	/**
	 * Checks if wanted file and user exist, and sends them a message to start uploading file.
	 */
	@Override
	public void action(ClientSession sess) {
		
		ClientSession session = sess.getActiveSessions().get(this.getTo());
		String filename = this.getContents();
		if(session != null){
			if(session.getFiles().containsKey(filename)){
				
				session.sendMessage(new OpenUploadConnMsg(
						session.getFiles().get(filename).getPath(), 
						sess.getName() + DELIMITER + filename + DELIMITER + session.getIp() ));
				
			}
		}
		
		
		
		

	}
	

	

}