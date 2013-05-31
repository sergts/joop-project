package project.messages;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.util.Iterator;


import project.ActiveSessions;
import project.ClientSession;
import project.client.Client;

public class DownloadMessage extends Message {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private static final int MAX_PORT_NUMBER = 65535;
	private static final int DEFAULT_PORT = 8877;
	private int port;

	public DownloadMessage(String m, String to) {
		super(m, to);
        port = DEFAULT_PORT;
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
                       
						while(true){
							if(available(port)){
								break;
							}
							else{
								port = changePortValue(port);
							}
						}
						
						String send = session.files.get(filename).getPath() + " " + port;

						session.sendMessage(new OpenUploadConnMsg(send));

						String receive = filename + " " + sess.ip + " " + port;

						sess.sendMessage(new OpenDownloadConnMsg(receive));



					}
				}

			}
		}

	}
	
	
	private int changePortValue(int currentPort) {
		
		if(currentPort==MAX_PORT_NUMBER){
			return DEFAULT_PORT;
		}
		 return ++currentPort;
		
	}

	private boolean available(int port) {
	    if ( port > MAX_PORT_NUMBER) {
	        throw new IllegalArgumentException("Invalid start port: " + port);
	    }

	    ServerSocket ss = null;
	    DatagramSocket ds = null;
	    try {
	        ss = new ServerSocket(port);
	        ss.setReuseAddress(true);
	        ds = new DatagramSocket(port);
	        ds.setReuseAddress(true);
	        return true;
	    } catch (IOException e) {
	    } finally {
	        if (ds != null) {
	            ds.close();
	        }

	        if (ss != null) {
	            try {
	                ss.close();
	            } catch (IOException e) {
	                /* should not be thrown */
	            }
	        }
	    }

	    return false;
	}
	
	
	

}