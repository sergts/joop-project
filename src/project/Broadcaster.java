package project;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

class Broadcaster extends Thread {
	private ActiveSessions activeSessions;
	private OutboundMessages outQueue;

	Broadcaster(ActiveSessions aa, OutboundMessages o) {
		activeSessions = aa;
		outQueue = o;
		start();
	}

	public void run() {
		while (true) {  
			Message msg = outQueue.getMessage(); 				// blocked
			synchronized (activeSessions) { 				// ActiveSessions lukku!
				Iterator<ClientSession> active = activeSessions.iterator();
				
				while (active.hasNext()) {
					ClientSession cli = active.next();
					
					if (!cli.isAlive()) {
						active.remove(); 		// ;-)
						
					} else if(msg.contents.equals("WHO") && msg.to.equals(cli.getName())){
						String who = "Who: ";
						for(ClientSession sess : activeSessions.getSessions()) who += sess.getName() + " ";
						cli.sendMessage(who);
					} else if(msg.contents.split(" ")[0].equals("DOWNLOAD") && msg.to.equals(cli.getName())){
						
						
						String str = msg.contents;
						
						for(ClientSession sess : activeSessions.getSessions()){
							if(sess.getName().equals(str.split(" ")[3])){
								for(File f : sess.watcher.getMap().keySet()){
									if(f.getName().equals(str.split(" ")[1])){
										try {
											sess.sendFile(f.getCanonicalPath(), 8877);
											cli.receiveFile(sess.getIP(), f.getName(), 8877);
										} catch (IOException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										
									}
								}
							}
						}
						
						
					}
					else if (msg.contents.equals("FILES") && msg.to.equals(cli.getName())) { 
						String files = "";
						for(ClientSession sess : activeSessions.getSessions()){
							cli.sendMessage(sess.getName() + " : ");
							for(File f : sess.watcher.getMap().keySet()){
								cli.sendMessage( "   " + f.getName() );
							}
						}
						//cli.sendMessage(files);
						
					}
					else if (msg.to == null) { // üldlevi e. broadcast-sõnum
						cli.sendMessage(msg.contents);
					} else if ( msg.to.equals(cli.getName()) ) {
					cli.sendMessage(msg.contents); // ainult konkreetsele adressaadile
					}
					
					
				}
			}
		}
		
	}
}