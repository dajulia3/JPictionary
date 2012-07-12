package protocol;

import client.Client;

public class StCPlayerLeftFrame extends StCFrame{
	
	byte playerID;

	public StCPlayerLeftFrame(byte[] pload) {
		super(pload);
		this.playerID = pload[0];
	}

	public StCPlayerLeftFrame(byte playerID) {
		super(new byte[] {playerID});
		this.playerID = playerID;
	}
	
	public byte getPID() { return playerID;}

	@Override
	public int getFrameType() {
		return 12;
	}

	@Override
	public void execute(Client client) {
		client.getFrame().removePlayer(client.getPlayers().get(getPID()));
	}
	
}
