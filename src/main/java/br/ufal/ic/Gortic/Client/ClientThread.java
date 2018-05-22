package br.ufal.ic.Gortic.Client;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;

import br.ufal.ic.Gortic.Server.BroadcastServer;

public class ClientThread extends Thread{
	
	private Socket connectionSocket;
	private BroadcastServer server;
	private BufferedReader inFromClient;
	private DataOutputStream outToClient;
	private String username;
	private int score = 0;
	
	public ClientThread(Socket connectionSocket, BroadcastServer server) throws IOException {
		this(connectionSocket, new BufferedReader(new InputStreamReader(connectionSocket.getInputStream())),
				new DataOutputStream(connectionSocket.getOutputStream()), server);
		this.username = inFromClient.readLine();
	}
	
	public ClientThread(Socket connectionSocket, BufferedReader br, DataOutputStream dos, BroadcastServer server) throws IOException {
		this.connectionSocket = connectionSocket;
		this.server = server;
		this.inFromClient = br;
		this.outToClient = dos;
	}
	
	public void onMessage(String message) {
		try {
			if(!message.endsWith("\n")) message += "\n";
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
			try {
				clientGuess = inFromClient.readLine();
				while(!clientGuess.equals("EXIT")) {
					server.broadcast(clientGuess, this);
					clientGuess = inFromClient.readLine();
				}
				outToClient.writeBytes("\n");
			} catch(SocketException se) {
				System.err.println("Client "+username+" closed the program unexpectedly, disconnecting him"); 
			}finally {
				connectionSocket.close();
				server.removeMessageListener(this);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
