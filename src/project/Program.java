package project;

public class Program {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		String fileFullName = "H:\\Downloads\\ATmega microcontroller.pdf"; //set this to a full file location with extension
		new ServerThread(fileFullName);
		String fileShortName = "file.pdf"; //this will be the name of file on the receiver's end
		String ip = "82.147.183.139"; //sever ip address
		new ClientThread(ip, fileShortName);

	}

}
