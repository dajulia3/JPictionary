package Game;

import Server.PictionaryServer;

public class BetweenRoundTimer extends Thread{

	PictionaryServer ps;
	int secs;
	
	public BetweenRoundTimer(final PictionaryServer ps, int secs){
		this.secs=secs;
		this.ps=ps;
	}
	
	/**
	 * in a separate thread, waits for a few seconds and then tells the 
	 * server that time is up
	 */
	public void run() {
		try {
			Thread.sleep(secs*1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}		
		ps.startRound();
	}
	
	
}
