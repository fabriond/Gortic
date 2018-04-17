import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientThread extends Thread{
	
	private Socket connectionSocket;
	private BroadcastServer server;
	private BufferedReader inFromClient;
	private DataOutputStream outToClient;
	private String username;
	
	public ClientThread(Socket connectionSocket, BroadcastServer server) throws IOException {
		this.connectionSocket = connectionSocket;
		this.server = server;
		this.inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
		this.outToClient = new DataOutputStream(connectionSocket.getOutputStream());
		
	}
	
	public void onMessage(String message) {
		try {
			outToClient.writeBytes(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		try {
			username = inFromClient.readLine();
			outToClient.writeBytes(server.getCurrentWordInfo());
			
			exitThread:
			while(true) {
				String clientGuess = inFromClient.readLine();
				while(!server.broadcast(clientGuess, username)) {
					if(clientGuess.equals("EXIT")) {
						outToClient.writeBytes("EXIT");
						connectionSocket.close();
						server.removeMessageListener(this);
						break exitThread;
					}
					outToClient.writeBytes("Wrong guess, please try again!\n");
					clientGuess = inFromClient.readLine();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
