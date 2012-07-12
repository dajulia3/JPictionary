package protocol;

import java.io.IOException;

public class CtSChatFrame extends CtSFrame {
	private String message;

	public CtSChatFrame(String message) {
		super(message.getBytes());
		this.message = message;
	}
	
	public CtSChatFrame(byte[] b) {
		super(b);
		this.message = new String(b);
		this.payload = b;
	}

	
	@Override
	public int getFrameType() {
		return 3;
	}

	public String getMessage() {
		return message;
	}

}
