import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

public class BroadcastServer {
	
	private List<ClientThread> listeners = new ArrayList<ClientThread>();
	private Entry<String, String> word;
	private final int scoreBoardSize = 5;
	
	public BroadcastServer() {
		setNewWord();
	}
	
	public boolean broadcast(String clientGuess, ClientThread guesser) {
		if(clientGuess.equals(word.getKey())) {
			guesser.addScore();
			String message = "Word \""+word.getKey()+"\" Guessed Correctly by "+guesser.getUsername()+"!"
						   + "##Scoreboard: #"+getScoreboard()+"#"+setNewWord();
			
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
	
	public String getScoreboard() {
		listeners.sort((l1, l2) -> new Integer(l2.getScore()).compareTo(l1.getScore()));
		String result = "";
		String format = "%03d | %s#";
		for(int i = 0; i < Math.min(listeners.size(), scoreBoardSize); i++) {
			result += String.format(format, listeners.get(i).getScore(), listeners.get(i).getUsername());
		}
		return result;
	}
	
	public void addMessageListener(ClientThread listener) {
		System.out.println(listener.getUsername()+" just joined the game!");
		listeners.add(listener);
	}
	public void removeMessageListener(ClientThread listener) {
		listeners.remove(listener);
	}
}
