package project;

public class FileUpdateRoutine extends Thread {
	
	private ClientSession s;
	FileUpdateRoutine(ClientSession s){
		this.s= s;
		start();
	}
	public void run(){
		while(true){
			s.sendMessage(new Message(MessageType.UPDATE));
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {}
		}
	}
	
}
