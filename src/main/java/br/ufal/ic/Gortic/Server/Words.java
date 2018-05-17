package br.ufal.ic.Gortic.Server;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

public class Words {
	private static Map<String, String> wordList = new HashMap<String, String>();
	static{
		wordList.put("Japao", "Asia"); //Sushi
		wordList.put("China", "Asia"); //Jackie Chan
		wordList.put("Coreia do Norte", "Asia"); //Kim Jong-Un
		wordList.put("Russia", "Europa/Asia"); 
		wordList.put("Franca", "Europa"); //Napole�o
		wordList.put("Alemanha", "Europa"); //7x1
		wordList.put("Inglaterra", "Europa"); //Harry Potter
		wordList.put("Canada", "Americas"); //Folha de Maple
		wordList.put("Brasil", "Americas"); //Crise
		wordList.put("Australia", "Oceania"); //Canguru
		wordList.put("Nova Zelandia", "Oceania"); //Kiwi
		wordList.put("Egito", "Africa"); //Camelo		
		//Armario
		//Celular
		//Socket
		//TCP //Nao garante entrega
		//UDP
		//Alvino - Melhor monitor
	}
	
	@SuppressWarnings("unchecked")
	public static Entry<String, String> getWord(){
		Object[] words = wordList.entrySet().toArray();
		return (Entry<String, String>) words[new Random().nextInt(words.length)];
	}
}
