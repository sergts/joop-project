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
		// Kui voogude loomine ebaõnnestub, peab väljakutsuv meetod 
		// sokli sulgema. Kui lõim läks käima, vastutab lõim selle eest
		
		start();
	}
	
	@Override
	public void run() {
		try {
			System.out.println("Klient " + getName() + ": ühendus loodi, hurraa!");

			// tervitame ühenduse loonud klienti - saadame väljundvoogu teate:
			netOut.println("Tere päevast, sisenesite meie moodsasse serverisse");
			netOut.println("ütle midagi. Väljumiseks sisesta tühjal real QUIT");

			// Kliendiseansi töötsükkel
			while(true) {
				String fromClient = netIn.readLine();		// Mis on kliendil meile öelda?
				if (fromClient != null) {
					System.out.println("Klient " + getName() + " saatis: " + fromClient);

					// juhtblokk
					if ("QUIT".equals(fromClient)) {
						System.out.println("Klient " + getName() + " lõpetas töö");
						break;
					} else {
						// Papagoi:
						String response = "Sina ütlesid: '" + fromClient 
								+ "', kuid mina ei saa sellest aru! Ütle veel midagi. ";
						netOut.println(response);
					}
				}

			} // töötsükli lõpp

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