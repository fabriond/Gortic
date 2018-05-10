import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Timer;
import java.util.TimerTask;

public class Client {
	static final String HOST = "localhost";
	static final int PORT = 1997;
	static boolean clientUp = true;
	
	@SuppressWarnings("resource")
	public static void main(String[] args) throws UnknownHostException, IOException {
		Socket clientSocket = new Socket(HOST, PORT);
		
        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        
        System.out.print("Username: ");
		outToServer.writeBytes(inFromUser.readLine()+"\n");
		System.out.printf("\r");
        Thread output = new Thread() {
        	public void run() {
				try {
					int firstSize = 0;
					int lastSize = 0;
					while(clientUp) {
						String[] contentFromServer = inFromServer.readLine().split("#");
						firstSize = contentFromServer[0].length();
						if(!contentFromServer[0].isEmpty()){
							for(int i = 0; i < Integer.max(firstSize, lastSize); i++)
								System.out.print("-");
							System.out.println();
						}
		            	for(int i = 0; i < contentFromServer.length; i++) {
		            		System.out.println(contentFromServer[i]);
		            	}
		            	lastSize = contentFromServer[contentFromServer.length-1].length();
					}
					System.out.println("Output Thread Closed");
				} catch (IOException e) {
					e.printStackTrace();
				}
            	
        	}
        
        };
        
        Thread input = new Thread() {
        	public void run() {
        		try {
        			while(clientUp) {
        				for(int i = 5; i > 0; i--) {
        					System.out.println("Cooldown: "+i+" seconds");
        					Thread.sleep(1000);
        				}
        				String currentGuess = inFromUser.readLine();
        				outToServer.writeBytes(currentGuess+"\n");
        				if(currentGuess.equals("EXIT")) clientUp = false;
        			}
        			System.out.println("\nInput Thread Closed");
				} catch (IOException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
        	}
        };
	    output.start();
	    input.start();
	}

}
