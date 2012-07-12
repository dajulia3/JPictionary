package protocol;

import client.Client;

public class StCChatFrame extends StCFrame{
	
	byte[] message;
	byte playerID;

	public StCChatFrame(byte[] pload) {
		super(pload);
		playerID = pload[0];
		byte[] tempMessage = new byte[pload.length - 1];
		for (int i = 0; i < tempMessage.length; ++i) {
			tempMessage[i] = pload[i + 1];
		}
		message = tempMessage;
	}
	
	/**
	 * Makes a frame given the message as a byte array and the player
	 * @param message
	 * @param player
	 */
	public StCChatFrame(byte[] message, byte playerID){
		super(byteArrayConstruct(message, playerID));
		this.message = message;
		this.playerID = playerID;
	}
	
	public byte[] getMessage() { return message;}
	public byte getPID() { return playerID;}
	
	// since the super must be first, this method makes the proper payload and returns it
	private static byte[] byteArrayConstruct(byte[] message, byte playerID) {
		byte[] ans = new byte[1 + message.length];
		ans[0] = playerID;
		for (int i = 1; i < ans.length; ++i) {
			ans[i] = message[i-1]; 
		}
		return ans;
	}

	@Override
	public int getFrameType() {
		return 4;
	}

	@Override
	public void execute(Client client) {
		client.getFrame().chatReceived(client.getPlayers().get(getPID()), new String(getMessage()));
	}

}
