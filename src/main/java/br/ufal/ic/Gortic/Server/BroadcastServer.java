package br.ufal.ic.Gortic.Server;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;

import br.ufal.ic.Gortic.Client.ClientThread;

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
	
	public void broadcast(String clientGuess, ClientThread guesser) {
		if(clientGuess.length() == 1) {
			processLetterGuess(clientGuess.charAt(0), guesser);
		}
		else if(clientGuess.toUpperCase().equals(word.getKey().toUpperCase())) {
			guesser.addScore(checkHiddenWord('_')*2);
			String message = "Word \""+word.getKey()+"\" Guessed Correctly by "+guesser.getUsername()+"!"
						   + "##Scoreboard: #"+getScoreboard()+"#"+setNewWord();
			
			listeners.forEach(l -> l.onMessage(message));
		}
		else guesser.onMessage("Wrong guess, please try again!\n");
	}
	
	public void processLetterGuess(char letterGuess, ClientThread guesser) {
		letterGuess = Character.toUpperCase(letterGuess);
		String ch = Character.toString(letterGuess);
		if(word.getKey().toUpperCase().contains(ch)) {
			if(!hiddenWord.contains(ch)) {
				hiddenWord = checkForChar(letterGuess);
				guesser.addScore(checkHiddenWord(letterGuess));
				
				String message = "Letter '"+letterGuess+"' Guessed Correctly by "+guesser.getUsername()+"!"
							   + "##Scoreboard: #"+getScoreboard()+"#"+getCurrentWordInfo();
				listeners.forEach(l -> l.onMessage(message));
				
				if(!hiddenWord.contains("_")) {
					String message2 = "Whole Word Revealed#Setting New Word!#"+setNewWord();
					listeners.forEach(l -> l.onMessage(message2));
				}
			
			} 
			//case in which the letter was already guessed
			else guesser.onMessage("Letter '"+letterGuess+"' Already Guessed Correctly!\n");
		}
		else {
			guesser.onMessage("Letter '"+letterGuess+"' Guessed Incorrectly!\n");
			if(wrongGuesses.add(letterGuess))
				listeners.forEach(l -> {if(!l.equals(guesser)) l.onMessage(getCurrentWordInfo());});
		}
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
	
	private int checkHiddenWord(char ch) {
		return (int)hiddenWord.chars().filter(num -> num == ch).count();
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
