import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	private static int port = 1997;
	private static ServerSocket welcomeSocket;
	private static String currentWord;
	public static void main(String[] args) throws IOException {
		welcomeSocket = new ServerSocket(port);
		
		currentWord = 
		
		while(true) {
			Socket connectionSocket = welcomeSocket.accept();
			ClientThread ct = new ClientThread(connectionSocket);
			ct.start();
		}
	}
}
