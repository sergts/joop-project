package project.messages;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import project.client.Client;
import project.server.ClientSession;

/**
 * This class implements the logic of the message for
 * exchanging information about the users on the server
 *
 */
@SuppressWarnings("serial")
public class WhoQuery extends Message {

	
	


	/**
	 * {@link Constructor}
	 * used for querying the server
	 */
	public WhoQuery() {
		super();
	}
	
	
	
	/**
	 * {@link Constructor}
	 * used as a response by server
	 * @param m - who's on the server
	 * 
	 */
	public WhoQuery(String m) {
		super(m);
	}



	@Override
	public void action(Client client) {
		
		List<String> users = Arrays.asList(getContents().split(" "));
		client.setUsersOnServer(users);
		
	}

	@Override
	public void action(ClientSession sess) {
		
		String namesOfActiveSessions = "";
		Iterator<String> names = sess.getActiveSessions().iteratorNames();
		while(names.hasNext()){
				namesOfActiveSessions += (names.next() + " ");
		}
		sess.sendMessage(new WhoQuery(namesOfActiveSessions));

		
		
	}

	
}
