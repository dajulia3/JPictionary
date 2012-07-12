package protocol;

import client.Client;

public class StCGuessWrongFrame extends StCFrame{

	public StCGuessWrongFrame() {
		super(new byte[0]);
	}

	@Override
	public int getFrameType() {
		return 6;
	}

	@Override
	public void execute(Client client) {
		client.getFrame().printMessage(client.getServer(), "Wrong guess");
	}

}
