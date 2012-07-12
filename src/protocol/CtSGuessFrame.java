package protocol;

import java.io.IOException;

public class CtSGuessFrame extends CtSFrame {
	private String guess;
	
	public CtSGuessFrame(String guess) {
		super(guess.getBytes());
		this.guess=guess;
	}

	public CtSGuessFrame(byte[] pload) {
		super(pload);
		this.guess = new String(pload);
	}

	

	public String getGuess() {
		return guess;
	}
	
	@Override
	public int getFrameType() {
		return 5;
	}

}
