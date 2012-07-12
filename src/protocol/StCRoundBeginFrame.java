package protocol;

import client.Client;

public class StCRoundBeginFrame extends StCFrame{

	public StCRoundBeginFrame() {
		super(new byte[0]);
	}

	@Override
	public int getFrameType() {
		return 13;
	}

	@Override
	public void execute(Client client) {
		client.startRound();
	}
	

}
