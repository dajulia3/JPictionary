package protocol;

import java.util.Set;

import client.Client;
import client.ClientFrame;
import client.Player;

public class StCDrawersAreFrame extends StCFrame{
	
	byte playerID;

	public StCDrawersAreFrame(byte playerID) {
		super(new byte[] {playerID});
		this.playerID = playerID;
	}
	
	public StCDrawersAreFrame(byte[] pload) {
		super(pload);
		this.playerID = pload[0];
	}

	@Override
	public int getFrameType() {
		return 14;
	}

	@Override
	public void execute(Client client) {
		ClientFrame frame = client.getFrame();
		Set<Player> drawers = client.getDrawers();
		
		for ( Player player : drawers ) {
			player.drawer = false;
		}
		
		drawers.clear();
		for ( byte pid : getPayload() ) {
			Player drawer = client.getPlayers().get(pid);
			drawer.drawer = true;
			drawers.add(drawer);
		}
		
		StringBuilder sb = new StringBuilder("Drawers are: ");
		
		for ( Player player : drawers ) {
			sb.append(player.name).append(" ");
		}
		
		frame.printMessage(client.getServer(), sb.toString());
		
		if ( drawers.contains(client.getThisPlayer()) ) {
			frame.printMessage(client.getServer(), "You are a drawer!");
		}
		
		frame.repaint();
	}
	
	

}
