package project.messages;


import project.client.Client;
import project.server.ClientSession;


/**
 * This message implements the logic of message sent
 * by a client intending to download a file from another client
 *
 */
@SuppressWarnings("serial")
public class DownloadMessage extends Message {

	
	


	/**
	 * @param m -  filename of the file to download
	 * @param to - owner of the file
	 */
	public DownloadMessage(String m, String to) {
		super(m, to);
       
	}

	@Override
	public void action(Client cli) {
	}

	@Override
	public void action(ClientSession sess) {

		
		
		ClientSession session = sess.activeSessions.get(this.getTo());
		String filename = this.getContents();
		if(session != null){
			if(session.files.containsKey(filename)){
				
				session.sendMessage(new OpenUploadConnMsg(session.files.get(filename).getPath(), 
						sess.getName() + "<" + filename + "<" + session.ip ));
				System.out.println("upload message sent");
			}
		}
		
		

	}
	

	

}