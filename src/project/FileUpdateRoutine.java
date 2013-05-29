package project;

import project.messages.*;

public class FileUpdateRoutine extends Thread {
	
	private ClientSession s;
	FileUpdateRoutine(ClientSession s){
		this.s= s;
		start();
	}
	public void run(){
		while(true){
			s.sendMessage(new UpdFilesMsg());
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {}
		}
	}
	
}
