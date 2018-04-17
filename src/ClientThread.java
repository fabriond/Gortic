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
	private User user;
	
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
	
	public String getUsername() {
		return user.getName();
	}
	
	@Override
	public void run() {
		try {
			user = new User(inFromClient.readLine());
			outToClient.writeBytes(server.getCurrentWordInfo());
			
			String clientGuess = "";
			
			while(!clientGuess.equals("EXIT")) {
				clientGuess = inFromClient.readLine();
				while(!server.broadcast(clientGuess, getUsername())) {
					if(clientGuess.equals("EXIT")) {
						outToClient.writeBytes("\n");
						break;
					}
					outToClient.writeBytes("Wrong guess, please try again!\n");
					clientGuess = inFromClient.readLine();
				}
				user.addScore();
			}
			
			connectionSocket.close();
			server.removeMessageListener(this);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
