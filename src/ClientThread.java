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
			
			String clientGuess = "";
			
			while(!clientGuess.equals("EXIT")) {
				clientGuess = inFromClient.readLine();
				while(!server.broadcast(clientGuess, username)) {
					if(clientGuess.equals("EXIT")) {
						outToClient.writeBytes("\n");
						break;
					}
					outToClient.writeBytes("Wrong guess, please try again!\n");
					clientGuess = inFromClient.readLine();
				}
			}
			
			connectionSocket.close();
			server.removeMessageListener(this);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
