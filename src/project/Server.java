package project;

import java.net.*;
import java.io.*;


public class Server {

	public static void main(String[] args) {
		int port = 8888;

		try {
			ServerSocket serv = new ServerSocket(port);
			
			// Serveri igavene töötsükkel
			while (true) {
				System.out.println("Server: kuulan pordil: " + port);
				Socket clisock = serv.accept();			// accept() jääb ootama, kuniks luuakse ühendus
				
				try {
					new Client(clisock);			// loome kliendiseansi lõime ning uuesti tagasi porti kuulama
				} catch (IOException e) {
					clisock.close();					// Kui ühendust ei loodud, sulgeme sokli
				}
					
				System.out.println("Server: alati valmis uusi kliente teenindama!");
				
			} // töötsükli lõpp

		} catch (IOException e) {
			System.out.println("IO viga :" + e.getMessage());
			e.printStackTrace();
		}

	}
}