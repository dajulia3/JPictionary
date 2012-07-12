package protocol;

import client.Client;

public class StCCanvasSizeFrame extends StCFrame{
	
	short x, y;

	public StCCanvasSizeFrame(byte[] pload) {
		super(pload);
		this.x = (short) (pload[0]*256 + pload[1]);
		this.y = (short) (pload[3]*256 + pload[4]);
	}
	
	public StCCanvasSizeFrame(short x, short y) {
		super(byteArrayConstructor(x,y));
		this.x = x;
		this.y = y;
	}
	
	public short getX() { return x;}
	public short getY() { return y;}
	

	public static byte[] byteArrayConstructor(short x, short y) {
		byte[] ans = new byte[4];
		ans[0] = (byte) (x/256);
		ans[1] = (byte) (x%256);
		ans[2] = (byte) (y/256);
		ans[3] = (byte) (y%256);
		return ans;
	}

	
	@Override
	public int getFrameType() {
		return 10;
	}

	@Override
	public void execute(Client client) {
		client.getFrame().setDrawSize(getX(), getY());
	}
	
}
