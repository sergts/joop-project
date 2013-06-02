package project.messages;

import java.lang.reflect.Constructor;

import project.client.Client;
import project.server.ClientSession;


/**
 * This class implements teh logic of the message used to 
 * set the name of the client
 */
@SuppressWarnings("serial")
public class InitName extends Message {

   private final  static String USED_NAME_STATE = "2";
   private final  static String OK_NAME_STATE = "3";
   

	

	/**
	 * {@link Constructor}
	 * @param myName -  name to be set
	 */
	public InitName(String myName) {
		super(myName);
	}

	@Override
	public void action(Client cli) {
		cli.setState(Integer.parseInt(getContents()));	
	}

	@Override
	public void action(ClientSession sess) {
		
		
		if(sess.activeSessions.checkInSession(getContents(), sess)){
			sess.sendMessage(new InitName(OK_NAME_STATE));
			sess.name = getContents();
		}else{
			sess.sendMessage(new InitName(USED_NAME_STATE));
		}
		
		
	
	}

}
