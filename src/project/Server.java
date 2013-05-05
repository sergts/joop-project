package project;

import java.net.*;
import java.io.*;


public class Server {

	public static void main(String[] args) {
		int port = 8888;

		try {
			ServerSocket serv = new ServerSocket(port);
			
			// Serveri igavene t��ts�kkel
			while (true) {
				System.out.println("Server: kuulan pordil: " + port);
				Socket clisock = serv.accept();			// accept() j��b ootama, kuniks luuakse �hendus
				
				try {
					new Client(clisock);			// loome kliendiseansi l�ime ning uuesti tagasi porti kuulama
				} catch (IOException e) {
					clisock.close();					// Kui �hendust ei loodud, sulgeme sokli
				}
					
				System.out.println("Server: alati valmis uusi kliente teenindama!");
				
			} // t��ts�kli l�pp

		} catch (IOException e) {
			System.out.println("IO viga :" + e.getMessage());
			e.printStackTrace();
		}

	}
}