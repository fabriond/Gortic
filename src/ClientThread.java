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
	private final String username;
	private int score = 0;
	
	public ClientThread(Socket connectionSocket, BroadcastServer server) throws IOException {
		this.connectionSocket = connectionSocket;
		this.server = server;
		this.inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
		this.outToClient = new DataOutputStream(connectionSocket.getOutputStream());
		this.username = inFromClient.readLine();		
	}
	
	public void onMessage(String message) {
		try {
			outToClient.writeBytes(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String getUsername() {
		return username;
	}
	
	public int getScore() {
		return score;
	}
	
	public void addScore() {
		score++;
	}
	
	public String getActualGuess(String guess) {
		if(guess.contains("LETTER#"))
			return guess.substring(guess.indexOf("#")+1);
		else return guess;
	}
	
	public void letterGuess(String guess) {
		guess = guess.replaceAll("[^a-zA-Z]", "");
		int correctCount = 0;
		for(char letterGuess : guess.toCharArray()) { 
			if(server.broadcast(letterGuess, this)) {
				correctCount++;
			}
		}
		if(correctCount == 0)
			try {
				outToClient.writeBytes(server.getCurrentWordInfo());
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	
	@Override
	public void run() {
		try {
			outToClient.writeBytes(server.getCurrentWordInfo());
			
			String clientGuess = "";
			
			while(!clientGuess.equals("EXIT")) {
				//LETTER#letterGuess
				//wordGuess
				clientGuess = inFromClient.readLine();
				while(clientGuess.matches("LETTER#(.)*")) {
					letterGuess(getActualGuess(clientGuess));
					clientGuess = inFromClient.readLine(); 
				}
				while(!server.broadcast(clientGuess, this)) {
					if(clientGuess.equals("EXIT")) {
						outToClient.writeBytes("\n");
						break;
					}
					outToClient.writeBytes("Wrong guess, please try again!\n");
					clientGuess = inFromClient.readLine();
					while(clientGuess.matches("LETTER#(.)*")) {
						letterGuess(getActualGuess(clientGuess));
						clientGuess = inFromClient.readLine(); 
					}
				}
			}
			connectionSocket.close();
			server.removeMessageListener(this);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
