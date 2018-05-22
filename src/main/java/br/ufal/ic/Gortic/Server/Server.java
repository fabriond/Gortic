package br.ufal.ic.Gortic.Server;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketException;

import br.ufal.ic.Gortic.Client.ClientThread;

public class Server {
	
	static BroadcastServer bs;
	static ServerSocket welcomeSocket;
	static final int PORT = 1997;
	
	public static void main(String[] args) throws IOException {
		System.out.println("SERVER STARTED");
		bs = new BroadcastServer();
		welcomeSocket = new ServerSocket(PORT);
		
		while(true) {
			try {
				ClientThread ct = new ClientThread(welcomeSocket.accept(), bs);
				System.out.println("New Client");
				bs.addMessageListener(ct);
				ct.start();
			} catch(SocketException e) {
				System.err.println("Client Failed to Connect");
			}
		}
	}
	
}


