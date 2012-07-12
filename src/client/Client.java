package client;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JOptionPane;

import protocol.CtSChatFrame;
import protocol.CtSDrawPointFrame;
import protocol.CtSGuessFrame;
import protocol.CtSJoinGameFrame;
import protocol.CtSLeaveGameFrame;
import protocol.FrameInputStream;
import protocol.FrameOutputStream;
import protocol.StCFrame;
import protocol.StCPlayerJoinedFrame;

/**
 * 
 * @author daj3
 */
public class Client implements Runnable {
	private ClientFrame frame;
	private FrameInputStream fis;
	private FrameOutputStream fos;
	private Socket s;

	private Set<Player> drawers = new HashSet<Player>();
	private Map<Byte, Player> players = new HashMap<Byte, Player>();

	private Player server;

	String tName = null;
	Player tPlayer = null;

	volatile boolean roundOn = false;
	private Timer timer = new Timer("Timer timer");

	public static void main(String args[]) {
		try {
			new Client().run();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Client(String hostname, String playername) throws IOException {
		frame = new ClientFrame(this);
		frame.setVisible(true);

		server = new Player(-1, "Server", false);

		tName = playername;
		
		s = new Socket(hostname, 3500);

		fos = new FrameOutputStream(s.getOutputStream());
		fis = new FrameInputStream(new DataInputStream(s.getInputStream()),
				false);

		sendJoinRequest(tName);
		
        frame.setTitle("Pictionary: " + tName);
	}
	
	public Client() throws IOException {
		frame = new ClientFrame(this);
		frame.setVisible(true);

		server = new Player(-1, "Server", false);

		String host = JOptionPane.showInputDialog(frame,
				"Please enter server name.");
		
		if ( host.equals("") ) {
			host = "localhost";
		}
		
		tName = JOptionPane.showInputDialog(frame, "Please enter player name.");

		s = new Socket(host, 3500);

		fos = new FrameOutputStream(s.getOutputStream());
		fis = new FrameInputStream(new DataInputStream(s.getInputStream()),
				false);

		sendJoinRequest(tName);
		
        frame.setTitle("Pictionary: " + tName);
	}

	public ClientFrame getFrame() {
		return frame;
	}

	public Set<Player> getDrawers() {
		return drawers;
	}

	public Map<Byte, Player> getPlayers() {
		return players;
	}

	public Player getThisPlayer() {
		return tPlayer;
	}

	public String getThisName() {
		return tName;
	}

	public Player getServer() {
		return server;
	}

	public void startRound() {
		frame.printMessage(server, "Round begins!");
		frame.resetDrawing();
		roundOn = true;
		timer.scheduleAtFixedRate(new TimerTask() {
			int time = 59;
			
			@Override
			public void run() {
				if ( roundOn && time != -1 ) {
					frame.updateTime(time--);
				}
			}
		}, 0, 1000);
	}

	public void endRound() {
		roundOn = false;
		frame.printMessage(server, "Round over.");
		frame.roundEnd();
	}

	public void run() {
//        Player alpha = new Player(44, "Alpha", false);
//        Player beta = new Player(13, "Beta", true);
//
//        frame.addPlayer(alpha);
//        frame.addPlayer(beta);
//		
//        frame.addPlayer(new Player(56, "Gamma", true));
//        frame.addPlayer(new Player(99, "Delta", false));
//        frame.addPlayer(new Player(22, "Epsilon", false));
//
//        frame.chatReceived(alpha, "Hello there!");
//        try {
//            Thread.sleep(3000);
//        } catch (InterruptedException ex) {
//        	ex.printStackTrace();
//        }
//
//        frame.kickVoteRecieved(beta, alpha);
//        frame.chatReceived(alpha, "Goodbye!");
		
        frame.addWindowListener(new WindowAdapter() {
        	@Override
        	public void windowClosing(WindowEvent e) {
        		leaveGame();
        	}
        });

		server = new Player(-1, "Server", false);

		while (true) {
			StCFrame stcFrame = (StCFrame) fis.readFrame();
			System.out.println(stcFrame);

			try{
			stcFrame.execute(this);
			}
			catch(Throwable t)
			{
				System.out.println("Crikey, that last frame was a doozy!");
			}
			if ( tPlayer == null && stcFrame instanceof StCPlayerJoinedFrame ) {
				StCPlayerJoinedFrame dFrame = (StCPlayerJoinedFrame) stcFrame;
				
				if ( dFrame.getName().equals(tName) ) {
					tPlayer = players.get((byte)dFrame.getPID());
				}
			}
			
//			if ( stcFrame instanceof StCDrawPointFrame ) {
//				StCDrawPointFrame dFrame = (StCDrawPointFrame) stcFrame;
//				frame.pointDrawn(dFrame.getX(), dFrame.getY());
//			} else if ( stcFrame instanceof StCDrawersAreFrame ) {
//				StCDrawersAreFrame dFrame = (StCDrawersAreFrame) stcFrame;
//				
//				System.out.println(Arrays.deepToString(players.entrySet().toArray()));
//				
//				for ( Player player : drawers ) {
//					player.drawer = false;
//				}
//				
//				drawers.clear();
//				for ( byte pid : dFrame.getPayload() ) {
//					Player drawer = players.get(pid);
//					drawer.drawer = true;
//					drawers.add(drawer);
//				}
//				
//				StringBuilder sb = new StringBuilder("Drawers are: ");
//				
//				for ( Player player : drawers ) {
//					sb.append(player.name).append(" ");
//				}
//				
//				frame.printMessage(server, sb.toString());
//				
//				if ( drawers.contains(tPlayer) ) {
//					frame.printMessage(server, "You are a drawer!");
//				}
//				
//				frame.repaint();
//			} else if ( stcFrame instanceof StCPlayerJoinedFrame ) {
//				StCPlayerJoinedFrame dFrame = (StCPlayerJoinedFrame) stcFrame;
//				
//				Player player = new Player(dFrame.getPID(), dFrame.getName(), false);
//				if ( !players.containsKey((byte) player.playerId) ) {
//					players.put(dFrame.getPID(), player);
//					frame.addPlayer(player);
//				}
//				
//				System.out.println(player.playerId + " =? " + ((tPlayer == null)? -1 : tPlayer.playerId) );
//				
//				if ( tPlayer == null && player.name.equals(tName) ) {
//					tPlayer = player;
//				}
//			} else if ( stcFrame instanceof StCChatFrame ) {
//				StCChatFrame dFrame = (StCChatFrame) stcFrame;
//				frame.chatReceived(players.get(dFrame.getPID()), new String(dFrame.getMessage()));
//			} else if ( stcFrame instanceof StCPlayerLeftFrame ) {
//				StCPlayerLeftFrame dFrame = (StCPlayerLeftFrame) stcFrame;
//				frame.removePlayer(players.get(dFrame.getPID()));
//			} else if ( stcFrame instanceof StCGuessWrongFrame ) {
//				frame.printMessage(server, "Wrong guess");
//			} else if ( stcFrame instanceof StCCanvasSizeFrame ) {
//				StCCanvasSizeFrame dFrame = (StCCanvasSizeFrame) stcFrame;
//				frame.setDrawSize(dFrame.getX(), dFrame.getY());
//			} else if ( stcFrame instanceof StCRoundBeginFrame ) {
//				frame.printMessage(server, "Round begins!");
//				frame.resetDrawing();
//				roundOn = true;
//			} else if ( stcFrame instanceof StCRoundEndTimeOutFrame ) {
//				StCRoundEndTimeOutFrame dFrame = (StCRoundEndTimeOutFrame) stcFrame;
//				frame.printMessage(server, "Round over. Ran out of time!");
//				// TODO: What was the word?
//				roundOn = false;
//			} else if ( stcFrame instanceof StCRoundEndGuessCorrectFrame ) {
//				StCRoundEndGuessCorrectFrame dFrame = (StCRoundEndGuessCorrectFrame) stcFrame;
//				frame.printMessage(server, "Round over. " + players.get(dFrame.getPID()) + "guessed correctly.");
//				frame.printMessage(server, "Word was: " + dFrame.getWord());
//				roundOn = false;
//			} else if ( stcFrame instanceof StCRoundEndGameStateFrame ) {
//				StCRoundEndGameStateFrame dFrame = (StCRoundEndGameStateFrame) stcFrame;
//				// TODO: Get state information and update accordingly.
//			} else if ( stcFrame instanceof StCWordIsFrame ) {
//				StCWordIsFrame dFrame = (StCWordIsFrame) stcFrame;
//				frame.printMessage(server, "Word is: " + new String(dFrame.getPayload()));
//			} else if ( stcFrame instanceof StCServerFullFrame ) {
//				frame.printMessage(server, "Server is full!");
//			}
			
//			try {
////				Thread.sleep(50);
//			} catch (InterruptedException e) {
//				// FIXME Auto-generated catch block
//				e.printStackTrace();
//			}
		}
		
		
	}

	public void leaveGame() {
		try {
			fos.writeFrame(new CtSLeaveGameFrame());
		} catch (IOException e) {
			// FIXME Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void sendJoinRequest(String playerName) {
		try {
			fos.writeFrame(new CtSJoinGameFrame(playerName));
		} catch (IOException e) {
			// FIXME Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void writePoint(int x, int y) {
		try {
			if (roundOn && drawers.contains(tPlayer)) {
				fos.writeFrame(new CtSDrawPointFrame(x, y));
			}
		} catch (IOException e) {
			// FIXME Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void sendChatMessage(String message) {
		try {
			fos.writeFrame(new CtSChatFrame(message));
		} catch (IOException e) {
			// FIXME Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void sendGuess(String guess) {
		try {
			if (tPlayer.drawer) {
				frame.printMessage(server, "You can't guess. You're the drawer!");
			} else {
				frame.printMessage(server, "You guessed: " + guess);
				fos.writeFrame(new CtSGuessFrame(guess));
			}
		} catch (IOException e) {
			// FIXME Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void sendKickRequest(Player kickee) {
		// TODO: Implement the underlying protocol for this
	}

}
