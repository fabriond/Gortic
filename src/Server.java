import java.io.IOException;
import java.net.ServerSocket;

public class Server {
	
	static BroadcastServer bs = new BroadcastServer();
	static ServerSocket welcomeSocket;
	static final int PORT = 1997;
	
	public static void main(String[] args) throws IOException {
		System.out.println("SERVER STARTED");
		welcomeSocket = new ServerSocket(PORT);
		
		while(true) {
			ClientThread ct = new ClientThread(welcomeSocket.accept(), bs);
			System.out.println("NEW CLIENT");
			bs.addMessageListener(ct);
			ct.start();
		}
	}
	
}


