package client;

import java.io.IOException;

import Server.PictionaryServer;

public class Test {
	public static void main(String[] args) throws Exception {
		new Thread(new Runnable() {
			@Override
			public void run() {
				PictionaryServer.main(new String[0]);
			}
		}).start();
		Thread.sleep(500);
		new Thread(new Client("localhost", "Alpha")).start();
		Thread.sleep(500);
		new Thread(new Client("localhost", "Beta")).start();
		
		
	}
}
