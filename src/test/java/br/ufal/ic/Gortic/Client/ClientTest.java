package br.ufal.ic.Gortic.Client;

import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import org.junit.jupiter.api.Test;

class ClientTest {

	File temp;
	
	private void createTemp() throws IOException {
		if(temp == null) {
			temp = File.createTempFile("temp", Long.toString(System.nanoTime()));
			temp.deleteOnExit();
		}
	}
	
	@Test
	void sendTest() throws IOException {
		
		createTemp();
		
		BufferedReader reader = new BufferedReader(new FileReader(temp));
		DataOutputStream writer = new DataOutputStream(new FileOutputStream(temp));
		
		Client client = new Client(writer, reader);
		client.send("send_test");
		assertEquals("send_test", reader.readLine());
		
	}
	
	@Test
	void receiveTest() throws IOException {

		createTemp();

		BufferedReader reader = new BufferedReader(new FileReader(temp));
		DataOutputStream writer = new DataOutputStream(new FileOutputStream(temp));
		
		Client client = new Client(writer, reader);
		client.send("receive_test");
		assertEquals("receive_test", client.receive()[0]);
		
	}

}
