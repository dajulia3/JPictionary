package protocol;

import client.Client;

public class StCWordIsFrame extends StCFrame{
	
	String currentWord;

	public StCWordIsFrame(byte[] pload) {
		super(pload);
		currentWord = new String(pload);
	}
	
	public StCWordIsFrame(String currentWord) {
		super(currentWord.getBytes());
		this.currentWord = currentWord;
	}

	@Override
	public int getFrameType() {
		return 15;
	}
	
	public String getWord() {
		return this.currentWord;
	}

	@Override
	public void execute(Client client) {
		client.getFrame().printMessage(client.getServer(), "Word is: " + new String(getPayload()));
	}
	
	

}
