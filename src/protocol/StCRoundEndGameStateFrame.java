package protocol;

import java.util.HashMap;
import java.util.Map;

import client.Client;
import client.Player;

public class StCRoundEndGameStateFrame extends StCFrame{
	Byte[] ids;
	Byte[] scores;
	public StCRoundEndGameStateFrame(byte[] pload) {
		super(pload);
		
		//Parse the payload
		ids=new Byte[pload.length/2];
		scores= new Byte[pload.length/2];
		for(int i=0;i<pload.length;i+=2)
		{
			ids[i/2]=pload[i];
			scores[i/2]=pload[i+1];
		}
	}

	@Override
	public int getFrameType() {
		return 18;
	}
	
	
	@Override
	public void execute(Client client) {
		client.getFrame().printMessage(client.getServer(), "Next round in 10 seconds.");
		Map<Byte,Player> idTPlayerMap=client.getPlayers();
			
		for(int i=0;i<ids.length;++i)//set the scores of each player to the previously parsed value
				idTPlayerMap.get(ids[i]).score=scores[i];
		
		client.getFrame().repaint();
	}

}
