import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

public class BroadcastServer {
	
	private List<ClientThread> listeners = new ArrayList<ClientThread>();
	private Entry<String, String> word;
	private String hiddenWord;
	private Set<Character> wrongGuesses;
	private final int scoreBoardSize = 5;
	
	public BroadcastServer() {
		setNewWord();
	}
	
	public boolean broadcast(char letterGuess, ClientThread guesser) {
		char auxGuess = letterGuess;
		letterGuess = Character.toUpperCase(letterGuess);
		System.out.println(letterGuess);
		String ch = Character.toString(letterGuess);
		if(word.getKey().toUpperCase().contains(ch)) {
			if(!hiddenWord.contains(ch)) {
				hiddenWord = checkForChar(letterGuess);
				String message = "Letter '"+letterGuess+"' Guessed Correctly by "+guesser.getUsername()+"!"
							   + "##"+getCurrentWordInfo();
				listeners.forEach(l -> l.onMessage(message));
				
				if(!hiddenWord.contains("_")) {
					String message2 = "Whole Word Revealed#Setting New Word!#"+setNewWord();
					listeners.forEach(l -> l.onMessage(message2));
				}
				
				return true;
			} 
			//case in which the letter was already guessed
			else {
				guesser.onMessage("Letter '"+auxGuess+"' Already Guessed Correctly!\n");
				return false;
			}
		}
		wrongGuesses.add(letterGuess);
		guesser.onMessage("Letter '"+auxGuess+"' Guessed Incorrectly!\n");
		listeners.forEach(l -> {if(!l.equals(guesser)) l.onMessage(getWrongGuesses());});
		return false;
	}
	
	public boolean broadcast(String clientGuess, ClientThread guesser) {
		if(clientGuess.length() == 1) {
			broadcast(clientGuess.charAt(0), guesser);
			return false;
		}
		if(clientGuess.toLowerCase().equals(word.getKey().toLowerCase())) {
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
		wrongGuesses = new HashSet<Character>();
		StringBuilder underlines = new StringBuilder();
		for(int i = 0; i < word.getKey().length(); i++) 
			if(word.getKey().charAt(i) != ' ') underlines.append("_ ");
			else underlines.append("  ");
		hiddenWord = underlines.toString();
		System.out.println("New Word: "+word.getKey());
		return "New Word: "+hiddenWord+"#"
			 + "New Word's clue: "+word.getValue()+"#"+getWrongGuesses();
	}
	
	public String getCurrentWordInfo() {
		return "Current Word: "+hiddenWord+"#"
			 + "Current Word's clue: "+word.getValue()+"#"+getWrongGuesses();
	}
	
	private String getWrongGuesses() {
		if(wrongGuesses.size() == 0) return "Incorrect Letters Guessed: None yet\n" ;
		StringBuilder aux = new StringBuilder();
		String comma = "";
		for(Character guess : wrongGuesses) {
			aux.append(comma+guess);
			comma = ", ";
		}
		
		return "Incorrect Letters Guessed: "+aux.toString()+"\n";
	}
	
	private String checkForChar(char letterGuess) {
		StringBuilder updatedHidden = new StringBuilder(hiddenWord);
		for(int i = 0; i < word.getKey().length(); i++) {
			if(word.getKey().toUpperCase().charAt(i) == letterGuess){
				updatedHidden.setCharAt(i*2, letterGuess);
			}
		}
		return updatedHidden.toString();
	}
	
	private String getScoreboard() {
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
