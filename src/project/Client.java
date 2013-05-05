package project;

import java.net.*;
import java.io.*;
class Client extends Thread {
	private Socket sock;
	private BufferedReader netIn;
	private PrintWriter netOut;
	
	public Client(Socket sock) throws IOException {
		this.sock = sock;
		netIn = new BufferedReader(
				new InputStreamReader(
						sock.getInputStream()));
		netOut = new PrintWriter(
				new BufferedWriter(
						new OutputStreamWriter(
								sock.getOutputStream())), true);
		// Kui voogude loomine eba�nnestub, peab v�ljakutsuv meetod 
		// sokli sulgema. Kui l�im l�ks k�ima, vastutab l�im selle eest
		
		start();
	}
	
	@Override
	public void run() {
		try {
			System.out.println("Klient " + getName() + ": �hendus loodi, hurraa!");

			// tervitame �henduse loonud klienti - saadame v�ljundvoogu teate:
			netOut.println("Tere p�evast, sisenesite meie moodsasse serverisse");
			netOut.println("�tle midagi. V�ljumiseks sisesta t�hjal real QUIT");

			// Kliendiseansi t��ts�kkel
			while(true) {
				String fromClient = netIn.readLine();		// Mis on kliendil meile �elda?
				if (fromClient != null) {
					System.out.println("Klient " + getName() + " saatis: " + fromClient);

					// juhtblokk
					if ("QUIT".equals(fromClient)) {
						System.out.println("Klient " + getName() + " l�petas t��");
						break;
					} else {
						// Papagoi:
						String response = "Sina �tlesid: '" + fromClient 
								+ "', kuid mina ei saa sellest aru! �tle veel midagi. ";
						netOut.println(response);
					}
				}

			} // t��ts�kli l�pp

		} catch (IOException e) {
			try {
				System.out.println("Avarii kliendi " + getName() + " seansis: " + e.getMessage() );
				sock.close();
			} catch (IOException ee) {
				throw new RuntimeException(ee);
			}
		}
	}
}