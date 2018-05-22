package br.ufal.ic.Gortic.Server;

import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.List;
import java.util.Map.Entry;

import org.junit.jupiter.api.Test;

import br.ufal.ic.Gortic.Client.ClientThread;

class BroadcastServerTest {

	@SuppressWarnings("unchecked")
	@Test
	void addListenerTest() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, IOException {
		BroadcastServer bs = new BroadcastServer();
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		DataOutputStream dos = new DataOutputStream(System.out);
		
		
		ClientThread ct = new ClientThread(new Socket(), br, dos, bs);
		bs.addMessageListener(ct);
		
		Field f = bs.getClass().getDeclaredField("listeners");
		f.setAccessible(true);
		List<ClientThread> listeners = (List<ClientThread>) f.get(bs);
		
		assertTrue(listeners.contains(ct));
	}
	
	@SuppressWarnings("unchecked")
	@Test
	void removeListenerTest() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, IOException {
		BroadcastServer bs = new BroadcastServer();
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		DataOutputStream dos = new DataOutputStream(System.out);
		
		
		ClientThread ct = new ClientThread(new Socket(), br, dos, bs);
		Field f = bs.getClass().getDeclaredField("listeners");
		f.setAccessible(true);
		List<ClientThread> listeners = (List<ClientThread>) f.get(bs);
		listeners.add(ct);
		
		bs.removeMessageListener(ct);
		assertFalse(listeners.contains(ct));
	}
	
	@Test
	void scoreBoardTest() throws IOException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException  {
		BroadcastServer bs = new BroadcastServer();
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		DataOutputStream dos = new DataOutputStream(System.out);
		
		
		ClientThread ct = new ClientThread(new Socket(), br, dos, bs);
		ct.addScore(1);
		ClientThread ct2 = new ClientThread(new Socket(), br, dos, bs);
		ct2.addScore(2);
		
		Method m = bs.getClass().getDeclaredMethod("getScoreboard");
		m.setAccessible(true);
		
		String scoreBoard1 = (String) m.invoke(bs);
		
		bs.addMessageListener(ct);
		bs.addMessageListener(ct2);
		
		String scoreBoard2 = (String) m.invoke(bs);
		String oracle2 = "002 | null#001 | null#"; 
		
		assertAll(
			() -> assertTrue(scoreBoard1.isEmpty()),
			() -> assertEquals(oracle2, scoreBoard2)
		);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	void checkHiddenWordTest() throws NoSuchMethodException, SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, IOException {
		BroadcastServer bs = new BroadcastServer();
		Method m1 = bs.getClass().getDeclaredMethod("processLetterGuess", char.class, ClientThread.class);
		Method m2 = bs.getClass().getDeclaredMethod("checkHiddenWord", char.class);
		m1.setAccessible(true);
		m2.setAccessible(true);
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		DataOutputStream dos = new DataOutputStream(System.out);
		
		ClientThread ct = new ClientThread(new Socket(), br, dos, bs);
		
		Field f = bs.getClass().getDeclaredField("word");
		f.setAccessible(true);
		String word = ((Entry<String, String>) f.get(bs)).getKey();
		m1.invoke(bs, word.charAt(0), ct);
		int result = (int) m2.invoke(bs, word.charAt(0));
		
		word = word.toLowerCase();
		word = word.replaceAll("[^"+word.charAt(0)+"]", "");
		assertEquals(word.length(), result);
	}

}
