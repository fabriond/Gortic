import java.util.HashMap;
import java.util.Map;

public class Palavra {
	
	static{
		Map<String, String> palavras = new HashMap<String, String>();
		palavras.put("Jap�o", "�sia"); //Sushi
		palavras.put("China", "�sia"); //Jackie Chan
		palavras.put("Cor�ia do Norte", "�sia"); //Kim Jong-Un
		palavras.put("Russia", "Europa/�sia"); 
		palavras.put("Fran�a", "Europa"); //Napole�o
		palavras.put("Alemanha", "Europa"); //7x1
		palavras.put("Inglaterra", "Europa"); //Harry Potter
		palavras.put("Canad�", "Am�ricas"); //Folha de Maple
		palavras.put("Brasil", "Am�ricas"); //Crise
		palavras.put("Austr�lia", "Oceania"); //Canguru
		palavras.put("Nova Zel�ndia", "Oceania"); //Kiwi
		palavras.put("Egito", "�frica"); //Camelo		
		//Armario
		//Celular
		//Socket
		//TCP //Nao garante entrega
		//UDP
		//Alvino - Melhor monitor
		
	}
	/*
	private final String palavra;
	private final String dica;
	
	private Palavra(String palavra, String dica){
		this.palavra = palavra;
		this.dica = dica;
		//int pick = new Random().nextInt(Letter.values().length);
	    //return Letter.values()[pick];
	}
	
	public String getDica() {
		return this.dica;
	}
	*/
}
