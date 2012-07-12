package protocol;

import java.io.IOException;

public class CtSJoinGameFrame extends CtSFrame {
	private String playerName;
	
	
	public CtSJoinGameFrame(String playerName) {
		super(playerName.getBytes());
		this.playerName=playerName;
	}

	public CtSJoinGameFrame(byte[] payload) {
		super(payload);
		this.playerName = new String(payload);
	}


	@Override
	public int getFrameType() {
		return 7;
	}


	public String getName() {
		return playerName;
	}

}
