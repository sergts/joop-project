package project.messages;

import java.lang.reflect.Constructor;

import project.client.Client;
import project.server.ClientSession;


/**
 * This class implements the logic of the message initiating
 * download connection for file transfrer on the downloading side
 *
 */
@SuppressWarnings("serial")
public class OpenDownloadConnMsg extends Message{

	private static int IP_INDEX = 0;
	private static int FILE_INDEX = 1;
	private static int PORT_INDEX = 2;
	private static String DELIMITER = "<";



	/**
	 * {@link Constructor}
	 * @param m - message contents
	 */
	public OpenDownloadConnMsg(String m){
		super(m);
	}
	
	
	/**
	 * {@link Constructor}
	 * @param m - message contents
	 * @param m - contents 
	 * 
	 * @param to - whom is the message addressed to
	 */
	public OpenDownloadConnMsg(String m, String to){
		super(m, to);
	}
	
	@Override
	public void action(Client cli) {
		
		System.out.println("download message received");
		System.out.println(this.getContents());
		String ip = this.getContents().split(DELIMITER)[IP_INDEX];
		String file = this.getContents().split(DELIMITER)[FILE_INDEX];
		int port = Integer.parseInt(this.getContents().split(DELIMITER)[PORT_INDEX]);
		cli.downloadConn(file, ip,  port);
		
	}

	@Override
	public void action(ClientSession sess) {
		
		
		ClientSession session = sess.getActiveSessions().get(this.getTo());
		if(session!=null){
			session.sendMessage(this);
		}
		
		
		
	}

	
}
