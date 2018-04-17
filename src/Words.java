import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

public class Words {
	private static Map<String, String> wordList = new HashMap<String, String>();
	static{
		wordList.put("Japao", "�sia"); //Sushi
		wordList.put("China", "�sia"); //Jackie Chan
		wordList.put("Coreia do Norte", "�sia"); //Kim Jong-Un
		wordList.put("Russia", "Europa/�sia"); 
		wordList.put("Franca", "Europa"); //Napole�o
		wordList.put("Alemanha", "Europa"); //7x1
		wordList.put("Inglaterra", "Europa"); //Harry Potter
		wordList.put("Canada", "Am�ricas"); //Folha de Maple
		wordList.put("Brasil", "Am�ricas"); //Crise
		wordList.put("Australia", "Oceania"); //Canguru
		wordList.put("Nova Zelandia", "Oceania"); //Kiwi
		wordList.put("Egito", "�frica"); //Camelo		
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
