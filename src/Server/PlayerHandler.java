package Server;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

import Game.PictionaryGame;

import protocol.*;
public class PlayerHandler extends Thread {

	private Socket socket; //corresponds to a particular player
	private PictionaryServer server;
	private FrameInputStream fromPlayer;
	private FrameOutputStream toPlayer;
	private static byte IDgetter=0;

	//player info
	private byte playerID;
	public boolean canDraw;
	private boolean hasJoined;
	public String playername="not made yet"; 
	public int score;
	private boolean alive; // when this is no longer the case, kill the threads

	public PlayerHandler(Socket s, PictionaryServer serve) { // the server will drive this construction	
		score=0;
		socket = s;
		server = serve;
		canDraw = false;
		alive = true;
		hasJoined=false;

		try {
			fromPlayer = new FrameInputStream(new DataInputStream(s.getInputStream()), true);
			toPlayer = new FrameOutputStream(s.getOutputStream());
			playerID = IDgetter;
			IDgetter=(byte) (IDgetter+1);

		} catch (IOException e) {
			System.out.println("PLAYER HANDLER"+ playerID + ": " + "error connecting to client");
			e.printStackTrace();
		}
	}
	/**
	 * write a frame to the client
	 * @param f
	 */
	public void sendFrame(StCFrame f) {
		try {
			toPlayer.writeFrame(f);
		} catch (Exception e) {
			System.out.println("PLAYER HANDLER"+ playerID + ": say goodbye to: " + this.playername);
			this.kill();
		}
	}
	/**
	 * returns the player's ID byte
	 */
	public byte getPID() {
		return (byte) this.playerID;
	}


	/**
	 * kill a player; their thread ends
	 */
	public void kill() {
		alive = false;
		server.playerLeaves(this);
	}
	/**
	 * continually reads from the client and passes the changes on to the server
	 */
	public void run() {

		while (alive) {
			// accept input from player until the game ends
			// we need to talk about who gets to control what

			CtSFrame frame = (CtSFrame) fromPlayer.readFrame();
			if (frame == null) break;
			switch(frame.getFrameType()){ //based on frame type, we do different methods

			case 1: if(hasJoined){//receive coordinates of a drawn point
				CtSDrawPointFrame frame1 =(CtSDrawPointFrame) frame;
				server.playerDraws(frame1.getX(),frame1.getY(), playerID);
				break;
			}

			case 3: if(hasJoined){//player sends a chat to everybody
				CtSChatFrame f3 = (CtSChatFrame) frame;
				server.playerChats(f3, playerID);
				break;
			}

			case 5: if(hasJoined&&!canDraw){// client guesses a word
				CtSGuessFrame f5 = (CtSGuessFrame) frame;
				server.playerGuesses(f5, this);
			}
			break;


			case 7: // player joins?
				server.addPlayer(this);
				server.playerJoins(this);

				CtSJoinGameFrame f7 = (CtSJoinGameFrame) frame;
				playername = f7.getName();
				hasJoined=true;
				break;

			case 11: if(hasJoined)// leave game 
				// this was deprecated because the client side doesn't have it implemented
				// and doing leaving game is hard... we just remove a player once we
				// hit an exception trying to send to them.
				//CtSLeaveGameFrame f2 = (CtSLeaveGameFrame) frame;
				this.kill();
			break;
			}
		}
		try {
			// when the thread is done, close the connection
			this.socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public void upScore(){
		score=score+1;
	}

	/**
	 * Flips the state of drawer/not drawer
	 */
	public void drawSwitch(){
		canDraw=!canDraw;
	}

}



