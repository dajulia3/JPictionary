package protocol;

import java.util.Map;

import client.Client;
import client.Player;

public class StCPlayerJoinedFrame extends StCFrame {
	
	byte playerID;
	String name;

	public StCPlayerJoinedFrame(byte[] pload) {
		super(pload);
		playerID = pload[0];
		byte[] nameArray = new byte[pload.length - 1];
		for (int i = 0; i < nameArray.length; ++i) {
			nameArray[i] = pload[i + 1];
		}
		name = new String(nameArray);
	
	}

	public StCPlayerJoinedFrame(byte playerID, String name) {
		super(byteArrayConstruct(playerID, name));
		this.playerID = playerID;
		this.name = name;
	}
	
	public byte getPID() { return playerID;}
	public String getName() { return name;}
	
	public static byte[] byteArrayConstruct(byte playerID, String name) {
		byte[] nameArray = name.getBytes();
		byte[] ans = new byte[nameArray.length + 1];
		ans[0] = playerID;
		for (int i = 1; i <= nameArray.length; ++i) {
			ans[i] = nameArray[i-1];
		}
		return ans;
	}

	@Override
	public int getFrameType() {
		return 8;
	}

	@Override
	public void execute(Client client) {
		Map<Byte, Player> players = client.getPlayers();
		
		Player player = new Player(getPID(), getName(), false);
		if ( !players.containsKey((byte) player.playerId) ) {
			players.put(getPID(), player);
			client.getFrame().addPlayer(player);
		}
	}

}
