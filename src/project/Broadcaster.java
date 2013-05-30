package project;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import project.messages.Message;

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
			Message msg = outQueue.getMessage(); 				
			synchronized (activeSessions) { 				
				Iterator<ClientSession> active = activeSessions.iterator();
				
				while (active.hasNext()) {
					ClientSession cli = active.next();
					
					if (!cli.isAlive()) {
						active.remove(); 		
						
					} 
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					/*
					else if(msg.contents.equals("WHO") && msg.to.equals(cli.getName())){
						String who = "Who: ";
						for(ClientSession sess : activeSessions.getSessions()) who += sess.getName() + " " + sess.ip + " ";
						cli.sendMessage(new Message(who, MessageType.QUERY));
					} 
					*/
					/*
					 * if message is DOWNLOAD <filename> FROM <username>, then scan all active users to find the one
					 * you want to download from, check if he has this file, and open connections between two users.
					 */
					/*
					else if(msg.contents.split(" ")[0].equals("DOWNLOAD") && msg.to.equals(cli.getName())){
						
						
						String str = msg.contents;
						
						for(ClientSession sess : activeSessions.getSessions()){
							if(sess.getName().equals(str.split(" ")[3])){
								for(String f : sess.files.keySet()){
									
									if(f.equals(str.split(" ")[1])){
										
										String send = sess.files.get(f).getPath() + " " + "8877"; //file name and port as string
										
										
										sess.sendMessage(new Message(send, MessageType.OPEN_UPLOAD_CONNECTION));
										
										String receive = f + " " + sess.ip + " " + "8877";
										
										cli.sendMessage(new Message(receive, MessageType.OPEN_DOWNLOAD_CONNECTION));
										
									}
								}
							}
						}
						
						
					}
					*/
					/*
					else if (msg.contents.equals("FILES") && msg.to.equals(cli.getName())) { 
						String files = "";
						
						for(ClientSession sess : activeSessions.getSessions()){
							files += sess.getName() + " : \n";
							for(String f : sess.files.keySet()){
								files +=  "   " + f + "\n";
							}
						}
						cli.sendMessage(new Message(files, MessageType.QUERY));
						
					}
					else if (msg.to == null) { // üldlevi e. broadcast-sõnum
						cli.sendMessage(new Message(msg.contents, MessageType.QUERY));
					} else if ( msg.to.equals(cli.getName()) ) {
						cli.sendMessage(new Message(msg.contents, MessageType.QUERY) ); // ainult konkreetsele adressaadile
					}
					*/
					
					
				}
			}
		}
		
	}
}