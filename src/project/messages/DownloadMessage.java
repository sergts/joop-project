package project.messages;

import java.io.IOException;
import java.net.Socket;
import java.util.Iterator;


import project.ClientSession;
import project.client.Client;

public class DownloadMessage extends Message {

	
	

	private static final int MAX_PORT_NUMBER = 8887;
	private static final int DEFAULT_PORT = 8000;
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

		System.out.println(" staring download message at client session");

		Iterator<ClientSession> activeSessions = sess.getActiveSessions().iterator();
		System.out.println(" sessions iterator accessed");
		
		String filename = this.contents;
		while (activeSessions.hasNext()) {
			ClientSession session = activeSessions.next();
			if (session.getName().equals(this.to)) {

				
				
				if(session.files.containsKey(filename)){
					
					session.sendMessage(new OpenUploadConnMsg(session.files.get(filename).getPath(), 
							sess.getName() + "<" + filename + "<" + session.ip ));
					System.out.println("upload message sent");
				}
/*
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
						
						String send = session.files.get(filename).getPath() + "<" + port;

						session.sendMessage(new OpenUploadConnMsg(send));
						System.out.println("upload mesage sent");
						String receive = filename + "<" + session.ip + "<" + port;

						sess.sendMessage(new OpenDownloadConnMsg(receive));
						System.out.println("downlaod message sent");


					}
				}*/

			}
		}

	}
	
	/*
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
	*/
	

}