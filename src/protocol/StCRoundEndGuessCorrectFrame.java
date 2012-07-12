package protocol;

import client.Client;
import client.ClientFrame;
import client.Player;

public class StCRoundEndGuessCorrectFrame extends StCFrame{
	
	byte playerID;
	String currentWord;

	public StCRoundEndGuessCorrectFrame(byte[] pload) {
		super(pload);
		playerID = pload[0];
		byte[] currentWordArray = new byte[pload.length - 1];
		for (int i = 0; i < currentWordArray.length; ++i) {
			currentWordArray[i] = pload[i + 1];
		}
		this.currentWord = new String(currentWordArray);
	}

	public StCRoundEndGuessCorrectFrame(byte playerID, String currentWord) {
		super(byteArrayConstruct(playerID, currentWord));
		this.playerID = playerID;
		this.currentWord = currentWord;
	}
	
	public String getWord() { return currentWord;}
	public byte getPID() { return playerID;}
	
	public static byte[] byteArrayConstruct(byte playerID, String currentWord) {
		byte[] currentWordArray = currentWord.getBytes();
		byte[] ans = new byte[1 + currentWordArray.length];
		ans[0] = playerID;
		for (int i = 1; i <= currentWordArray.length; ++i) {
			ans[i] = currentWordArray[i-1];
		}
		return ans;
	}

	@Override
	public int getFrameType() {
		return 16;
	}

	@Override
	public void execute(Client client) {
		ClientFrame frame = client.getFrame();
		Player server = client.getServer();
		
		client.endRound();
		frame.printMessage(server, client.getPlayers().get(getPID()) + " guessed correctly.");
		frame.printMessage(server, "Word was: " + getWord());
	}

}
