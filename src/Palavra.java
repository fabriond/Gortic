import java.util.HashMap;
import java.util.Map;

public class Palavra {
	
	static{
		Map<String, String> palavras = new HashMap<String, String>();
		palavras.put("Japão", "Ásia"); //Sushi
		palavras.put("China", "Ásia"); //Jackie Chan
		palavras.put("Coréia do Norte", "Ásia"); //Kim Jong-Un
		palavras.put("Russia", "Europa/Ásia"); 
		palavras.put("França", "Europa"); //Napoleão
		palavras.put("Alemanha", "Europa"); //7x1
		palavras.put("Inglaterra", "Europa"); //Harry Potter
		palavras.put("Canadá", "Américas"); //Folha de Maple
		palavras.put("Brasil", "Américas"); //Crise
		palavras.put("Austrália", "Oceania"); //Canguru
		palavras.put("Nova Zelândia", "Oceania"); //Kiwi
		palavras.put("Egito", "África"); //Camelo		
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
