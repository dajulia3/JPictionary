package protocol;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import client.ClientFrame;

public class CtSDrawPointFrame extends CtSFrame {
	private int x,y;
	public CtSDrawPointFrame(int x, int y) {
		super(byteArrayConstructor(x, y));
		this.x=x;
		this.y=y;
	}
	public CtSDrawPointFrame(byte[] pload){
		super(pload);
		this.payload=pload;
		
		ByteArrayInputStream bin = new ByteArrayInputStream(pload);
		DataInputStream din = new DataInputStream(bin);
		
		try {
			this.x = din.readShort();
			this.y = din.readShort();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	public static byte[] byteArrayConstructor (int x, int y) {
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		DataOutputStream dout = new DataOutputStream(bout);
		
		try {
			dout.writeShort(x);
			dout.writeShort(y);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		
		System.out.println("CTSDRAWPOINTFRAME: (" + x + ", " + y + ")");
		return bout.toByteArray();
	}

	

	public int getFrameType(){
		return 1;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
}

