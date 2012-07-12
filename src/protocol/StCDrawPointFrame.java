package protocol;

import java.io.IOException;

import client.Client;

public class StCDrawPointFrame extends StCFrame {
	
	int x, y;
	
	public StCDrawPointFrame(byte[] pload) throws IOException {
		super(pload);
		this.x = dis.readShort();
		this.y = dis.readShort();
	}

	public StCDrawPointFrame(int x, int y, int playerNumber) {
		super(byteArrayConstruct(x, y, playerNumber));
		this.x = x;
		this.y = y;
	}
	
	public int getX() { return x;}
	public int getY() { return y;}
	
	private static byte[] byteArrayConstruct(int x, int y, int playerNumber) {
		byte[] ans = new byte[5];
		ans[0] = (byte) (x>>8);
		ans[1] = (byte) x;
		ans[2] = (byte) (y>>8);
		ans[3] = (byte) y;
		ans[4] = (byte) playerNumber;
		return ans;
	}

	@Override
	public int getFrameType() {
		return 2;
	}

	@Override
	public void execute(Client client) {
		client.getFrame().pointDrawn(getX(), getY());
		client.getFrame().repaint();
	}
	
}
