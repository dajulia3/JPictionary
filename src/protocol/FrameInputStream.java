package protocol;


import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

public class FrameInputStream {

	private DataInputStream input;
	private boolean isServer;

	public FrameInputStream(DataInputStream is, boolean isServer){
		this.isServer = isServer;
		input = is;
	}


	public Frame readFrame() {
		try {
			if (input.read() == '*') { // only if this is the start
				// of a flap frame
				byte frametype = (byte)input.read();	
				short length = input.readShort();
				byte[] payload = new byte[length];
				//FIXME problem here?
				input.readFully(payload);
				if(!isServer) {//client is receiving messages from server
					switch (frametype) { // for the frametype, we'll return a 
					//different object for each case
					case 2:
						return new StCDrawPointFrame(payload);
					case 4:
						return new StCChatFrame(payload);
					case 6:
						return new StCGuessWrongFrame();
					case 8:
						return new StCPlayerJoinedFrame(payload);
					case 9:
						return new StCServerFullFrame();
					case 10:
						return new StCCanvasSizeFrame(payload);
					case 12:
						return new StCPlayerLeftFrame(payload);
					case 13:
						return new StCRoundBeginFrame();
					case 14:
						return new StCDrawersAreFrame(payload);
					case 15:
						return new StCWordIsFrame(payload);
					case 16:
						return new StCRoundEndGuessCorrectFrame(payload);
					case 17:
						return new StCRoundEndTimeOutFrame();
					case 18:
						return new StCRoundEndGameStateFrame(payload);
					}
				}
				else{// server receiving messages from client

					switch(frametype) {

					case 1: //draw point
						return new CtSDrawPointFrame(payload);
					case 3: //chat message
						return new CtSChatFrame(payload);
					case 5: // guess
						return new CtSGuessFrame(payload);
					case 7: // join game
						return new CtSJoinGameFrame(payload);
					case 11: // leave game
						return new CtSLeaveGameFrame();
					}
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
