package br.ufal.ic.Gortic;
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
		this(connectionSocket, new BufferedReader(new InputStreamReader(connectionSocket.getInputStream())),
				new DataOutputStream(connectionSocket.getOutputStream()), server);		
	}

	public ClientThread(Socket connectionSocket, BufferedReader br, DataOutputStream dos, BroadcastServer server) throws IOException {
		this.connectionSocket = connectionSocket;
		this.server = server;
		this.inFromClient = br;
		this.outToClient = dos;
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
	
	public void addScore(int number) {
		score += number;
	}
	
	@Override
	public void run() {
		try {
			outToClient.writeBytes(server.getCurrentWordInfo());
			
			String clientGuess = "";
			
			while(!clientGuess.equals("EXIT")) {
				clientGuess = inFromClient.readLine();
				while(!server.broadcast(clientGuess, this)) {
					if(clientGuess.equals("EXIT")) {
						outToClient.writeBytes("\n");
						break;
					}
					if(clientGuess.length() > 1) outToClient.writeBytes("Wrong guess, please try again!\n");
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
