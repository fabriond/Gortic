package br.ufal.ic.Gortic.Client;

import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;

public class ClientMain {
	private static boolean clientUp = true;
	static final String HOST = "localhost";
	static final int PORT = 1997;
	
	public static void main(String[] args) throws IOException {
		Socket clientSocket;
		try {
			clientSocket = new Socket(HOST, PORT);
		}catch(ConnectException ce) {
			System.err.println("Server not started, closing program!");
			return;
		}
		System.out.print("Username: ");
		Scanner scan = new Scanner(System.in);
		Client client = new Client(clientSocket);
		String username = scan.nextLine();
		client.send(username);
		
		Thread output = new Thread(() -> {
			int firstSize = 0;
			int lastSize = 0;
			try {
				try {
					while(clientUp) {
						String[] contentFromServer = client.receive();
						if(contentFromServer.length > 0) {
							firstSize = contentFromServer[0].length();
							
							synchronized(System.out) {
								if(!contentFromServer[0].isEmpty()) {
									for(int i = 0; i < Integer.max(firstSize, lastSize); i++)
										System.out.print("-");
									System.out.println();
								
									for(int i = 0; i < contentFromServer.length; i++) {
										System.out.println(contentFromServer[i]);
									}
								}
								System.out.println();
								lastSize = contentFromServer[contentFromServer.length-1].length();
								System.out.notify();
							}
						}
					}
				}catch(SocketException se) {
					System.err.println("Server stopped, closing program!");
					clientUp = false;
				}finally {
					scan.close();
					clientSocket.close();
					System.out.println("Gortic Closed");
				}
			} catch(IOException e) {
				e.printStackTrace();
			}
        });
        
        Thread input = new Thread(() -> {
			try {
				while(clientUp) {
					String currentGuess = scan.nextLine();
					client.send(currentGuess);
					if(currentGuess.equals("EXIT")) clientUp = false;
					else synchronized(System.out) {
	    				System.out.wait();
	    				for(int i = 5; i > 0; i--) {
	    					System.out.println("Cooldown: "+i+" seconds");
	    					Thread.sleep(1000);
	    				}
	    				System.out.println("You can continue guessing now!\n");
					}
				}
			} catch (IOException e) {
				return; // server disconnected and user tried to type anything
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
        });
	    output.start();
	    input.start();
	}
}
