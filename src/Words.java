import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

public class Words {
	private static Map<String, String> wordList = new HashMap<String, String>();
	static{
		wordList.put("Japao", "Ásia"); //Sushi
		wordList.put("China", "Ásia"); //Jackie Chan
		wordList.put("Coreia do Norte", "Ásia"); //Kim Jong-Un
		wordList.put("Russia", "Europa/Ásia"); 
		wordList.put("Franca", "Europa"); //Napoleão
		wordList.put("Alemanha", "Europa"); //7x1
		wordList.put("Inglaterra", "Europa"); //Harry Potter
		wordList.put("Canada", "Américas"); //Folha de Maple
		wordList.put("Brasil", "Américas"); //Crise
		wordList.put("Australia", "Oceania"); //Canguru
		wordList.put("Nova Zelandia", "Oceania"); //Kiwi
		wordList.put("Egito", "África"); //Camelo		
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
