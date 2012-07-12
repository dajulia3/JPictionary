package Game;

import Server.PictionaryServer;

public class DuringRoundTimer extends Thread{

	PictionaryServer ps;
	int secs;
	int roundNumber;
	
	public DuringRoundTimer(final PictionaryServer ps, int secs, int roundNumber){
		this.secs=secs;
		this.roundNumber=roundNumber;
		this.ps=ps;
		
	}
	/**
	 * in a separate thread, runs a timer that sleeps and then tells the server
	 * that the round is over
	 */
	public void run() {
		try {
			Thread.sleep(secs*1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}		
		if(ps.game.roundNumber==roundNumber)
			ps.roundEndsTimeUp();
	}
	
	
}
