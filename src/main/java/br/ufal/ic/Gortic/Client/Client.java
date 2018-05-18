package br.ufal.ic.Gortic.Client;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {
	static final String HOST = "localhost";
	static final int PORT = 1997;
	static final boolean DEBUG = false;
	static boolean clientUp = true;
	static boolean wait = true;
	
	public static void main(String[] args) throws UnknownHostException, IOException {
		System.out.print("Username: ");
		Scanner scan = new Scanner(System.in);
		String name = scan.nextLine();
		Socket clientSocket;
		try {
			clientSocket = new Socket(HOST, PORT);
		}catch(ConnectException ce) {
			System.err.println("Server not started, closing program!");
			return;
		}
        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        
		outToServer.writeBytes(name+"\n");
        Thread output = new Thread() {
        	public void run() {
				try {
					int firstSize = 0;
					int lastSize = 0;
					try {
						while(clientUp) {
							String contentAux = inFromServer.readLine();
							String[] contentFromServer = contentAux.split("#");
							firstSize = contentFromServer[0].length();
							
							synchronized(System.out) {
								if(!contentAux.isEmpty()) {
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
					}catch(SocketException se) {
						System.err.println("Server stopped, closing program!");
						clientUp = false;
					}finally {
						if(DEBUG) System.out.println("Output Thread Closed");
						scan.close();
						clientSocket.close();
						System.out.println("Gortic Closed");
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
            	
        	}
        
        };
        
        Thread input = new Thread() {
        	public void run() {
        		try {
        			while(clientUp) {
        				String currentGuess = inFromUser.readLine();
        				outToServer.writeBytes(currentGuess+"\n");
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
        			if(DEBUG)System.out.println("\nInput Thread Closed");
				} catch (IOException e) {
					return; // server disconnected and user tried to type anything
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
        	}
        };
	    output.start();
	    input.start();
	}

}
