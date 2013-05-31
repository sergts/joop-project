package project.messages;

import java.util.Iterator;


import project.ActiveSessions;
import project.ClientSession;
import project.client.Client;

public class DownloadMessage extends Message {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DownloadMessage(String m, String to) {
		super(m, to);
		
	}

	@Override
	public void action(Client cli) {
		// TODO Auto-generated method stub

	}

	@Override
	public void action(ClientSession sess) {

		
		Iterator<ClientSession> activeSessions = sess.getActiveSessions().iterator();
		while (activeSessions.hasNext()) {
			ClientSession session = activeSessions.next();
			if (session.getName().equals(this.to)) {
				
				
				for (String filename : session.files.keySet()) {
					
					if (filename.equalsIgnoreCase(this.contents)) {
                       
						String send = session.files.get(filename).getPath() + " " + "8877";
						
						session.sendMessage(new OpenUploadConnMsg(send));
						
						String receive = filename + " " + sess.ip + " " + "8877";
						
						sess.sendMessage(new OpenDownloadConnMsg(receive));
						
						
						
					}
				}

			}
		}

	}

}
