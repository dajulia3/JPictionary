package protocol;

import client.Client;

public class StCRoundEndTimeOutFrame extends StCFrame{

	public StCRoundEndTimeOutFrame() {
		super(new byte[0]);
	}

	@Override
	public int getFrameType() {
		return 17;
	}

	@Override
	public void execute(Client client) {
		client.endRound();
		client.getFrame().printMessage(client.getServer(), "Ran out of time!");
	}

}
