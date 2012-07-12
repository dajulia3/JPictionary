package protocol;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

import client.Client;

public abstract class StCFrame extends Frame{
	
	final protected DataInputStream dis;
	private byte[] pload;
	
	
	public StCFrame(byte[] pload) {
		dis = new DataInputStream(new ByteArrayInputStream(pload));
		this.pload = pload;
	}
		
 	public abstract int getFrameType();
 	public byte[] getPayload()
 	{	
 		return pload;
 	}
 	
 	public String toString() {
 		return ("StC frame type: " + this.getFrameType() + " " + new String(pload)); 
 	}
 	
 	public abstract void execute(Client client);
}
