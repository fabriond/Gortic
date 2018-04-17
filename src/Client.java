import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
	static final String HOST = "localhost";
	static final int PORT = 1997;
	
	@SuppressWarnings("resource")
	public static void main(String[] args) throws UnknownHostException, IOException {
		Socket clientSocket = new Socket(HOST, PORT);
		
        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        
        System.out.print("Username: ");
		outToServer.writeBytes(inFromUser.readLine()+"\n");
		
        Thread output = new Thread() {
        	public void run() {
				try {
					while(true) {
						String[] contentFromServer = inFromServer.readLine().split("#");
						if(contentFromServer[0].equals("EXIT")) break;
						System.out.println("#############################################");
		            	for(int i = 0; i < contentFromServer.length; i++) {
		            		System.out.println(contentFromServer[i]);
		            	}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
            	
        	}
        };
        
        Thread input = new Thread() {
        	public void run() {
        		try {
        			while(true) {
        				String currentGuess = inFromUser.readLine();
        				outToServer.writeBytes(currentGuess+"\n");
        				if(currentGuess.equals("EXIT")) break;
        			}
				} catch (IOException e) {
					e.printStackTrace();
				}
        	}
        };
	    output.start();
	    input.start();
	}

}
