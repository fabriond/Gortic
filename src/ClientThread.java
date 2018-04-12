import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientThread extends Thread{

	private Socket connectionSocket;
	
	public ClientThread(Socket connectionSocket) {
		this.connectionSocket = connectionSocket;
	}
	
	@Override
	public void run() {
		String clientTry;
		BufferedReader inFromClient;
		DataOutputStream outToClient;
		try{
			inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
			outToClient = new DataOutputStream(connectionSocket.getOutputStream());
			clientTry = inFromClient.readLine();
			
		}catch(Exception e) {
			System.out.println("oloko bixo, essa fera aí meu");
		}
	}

}
