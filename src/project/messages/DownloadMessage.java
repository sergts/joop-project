package project.messages;


import project.client.Client;
import project.server.ClientSession;


@SuppressWarnings("serial")
public class DownloadMessage extends Message {

	
	


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
		
		/*
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


			}
		}*/

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