package project.messages;

import java.io.IOException;
import java.net.Socket;
import java.util.Iterator;


import project.ClientSession;
import project.client.Client;

public class DownloadMessage extends Message {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private static final int MAX_PORT_NUMBER = 65535;
	private static final int DEFAULT_PORT = 8889;
	private int port;

	public DownloadMessage(String m, String to) {
		super(m, to);
        port = DEFAULT_PORT;
	}

	@Override
	public void action(Client cli) {
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
							if(available(port, sess.ip, session.ip)){
								break;
							}
							else{
								port = changePortValue(port);
							}
						}
						
						String send = session.files.get(filename).getPath() + " " + port;

						session.sendMessage(new OpenUploadConnMsg(send));

						String receive = filename + " " + session.ip + " " + port;

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

	private static boolean available(int port, String ip, String ip2) {
	    System.out.println("--------------Testing port " + port);
	    Socket s = null;
	    Socket s2 = null;
	    try {
	        s = new Socket(ip, port);
	        s2 = new Socket(ip2, port);
	        System.out.println("--------------Port " + port + " is not available");
	        return false;
	    } catch (IOException e) {
	        System.out.println("--------------Port " + port + " is available");
	        return true;
	    } finally {
	        if( s != null){
	            try {
	                s.close();
	                
	            } catch (IOException e) {}
	            
	        }
	        if( s2 != null){
	            try {
	                s2.close();
	            } catch (IOException e) {}
	            
	        }
	    }
	}
	
	

}