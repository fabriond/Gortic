import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

public class BroadcastServer {
	
	private List<ClientThread> listeners = new ArrayList<ClientThread>();
	private Entry<String, String> word;
	
	public BroadcastServer() {
		setNewWord();
	}
	
	public boolean broadcast(String clientGuess, String username) {
		if(clientGuess.equals(word.getKey())) {
			String message = "Word \""+word.getKey()+"\" Guessed Correctly by "+username+"!"+"#"+setNewWord();
			
			listeners.forEach(l -> l.onMessage(message));
			return true;
		}
		return false; //wrong answer
	}
	
	private String setNewWord() {
		this.word = Words.getWord();
		System.out.println("New Word: "+word.getKey());
		return "New Word has "+word.getKey().length()+" letters"+"#"
			 + "New Word's clue: "+word.getValue()+"\n";
	}
	
	public String getCurrentWordInfo() {
		return "Current Word has "+word.getKey().length()+" letters"+"#"
			 + "Current Word's clue: "+word.getValue()+"\n";
	}

	public void addMessageListener(ClientThread listener) {
		System.out.println(listener.getUsername()+" just joined the game!");
		listeners.add(listener);
	}
	public void removeMessageListener(ClientThread listener) {
		listeners.remove(listener);
	}
}
