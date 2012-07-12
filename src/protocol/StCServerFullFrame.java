package protocol;

import client.Client;

public class StCServerFullFrame extends StCFrame{

	public StCServerFullFrame() {
		super(new byte[0]);
	}

	@Override
	public int getFrameType() {
		return 9;
	}

	@Override
	public void execute(Client client) {
		client.getFrame().printMessage(client.getServer(), "Server is full!");
	}

}
