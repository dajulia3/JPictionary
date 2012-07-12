package protocol;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

public abstract class CtSFrame extends Frame {

	protected byte[] payload;
	
	public CtSFrame(byte[] payload)
	{
		this.payload = payload;
	}
	
	/**
	 * universal method to get the payload of a cts frame
	 */
	public byte[] getPayload(){
		return this.payload;
	}
	
}
