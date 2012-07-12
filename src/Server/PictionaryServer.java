package Server;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import protocol.*;

import protocol.CtSChatFrame;

import Game.BetweenRoundTimer;
import Game.BlockingQueue;
import Game.DuringRoundTimer;

import Game.PictionaryGame;

final public class PictionaryServer {
	public PictionaryGame game;
	private BlockingQueue<Runnable> runqueue;
	private ServerSocket socket;

	
	public PictionaryServer(int port) {
		try {
			runqueue = new BlockingQueue<Runnable>(200);
			game = new PictionaryGame(this);
			socket = new ServerSocket(port);
			Thread acceptor = new Thread( new Runnable () {
				public void run() {
					while (true) {				
						try {
							new PlayerHandler(socket.accept(), PictionaryServer.this).start();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			});
			acceptor.start();
			Thread dequeueThread = new Thread(new Runnable() {
				public void run() {
					while(true) {
						runqueue.dequeue().run();
					}
				}
			});
			dequeueThread.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Adds a person to the list of playerhandlers in the game
	 * @param playerHandler
	 */
	public void addPlayer(final PlayerHandler playerHandler) {
		runqueue.enqueue(new Runnable() {
			public void run() {
				game.addPlayer(playerHandler);
				}
		});
	}
	/**
	 * sends a frame to everybody
	 * @param f
	 */
	public void sendFrameEverybody(final StCFrame f){
		final ArrayList<PlayerHandler> players = game.getPlayers();
		for(PlayerHandler p : players){
			p.sendFrame(f);
		}
	}

	/**
	 * method that starts the round
	 */
	public void startRound(){
		runqueue.enqueue(new Runnable(){
			public void run(){
				//choose a word
				game.changeWord();

				//choose drawer
				game.changeDrawer();
				PlayerHandler drawer = game.getDrawers();
				//send drawer
				sendDrawers(drawer.getPID());

				//send word
				sendWord(drawer);

				//game in progress
				game.roundInProgress=true;

				//increment round number
				++game.roundNumber;
				//send start
				sendStart();
				new DuringRoundTimer(PictionaryServer.this,60,game.roundNumber).start();
			}
			
		});
		
	}

	/**
	 * Gives response to when round ends due to a correct guess
	 * @param pid  ID of person who guessed correctly
	 */
	private void roundEndsCorrectGuess(final byte pid) {
		runqueue.enqueue( new Runnable() {
			public void run() {
				//game not in progress
				game.roundInProgress=false;
				//send everybody who won
				sendCorrectGuesser(pid);

				//send game state
				sendScores();
		
				new BetweenRoundTimer(PictionaryServer.this,10).start();	
			}
		}
		);
	}

	/**
	 * Gives response to when round ends due to time running out
	 */
	public void roundEndsTimeUp() {
		runqueue.enqueue(new Runnable(){
			public void run(){
				if(game.roundInProgress){
					//round not in progress
					game.roundInProgress = false;
					//send time over
					sendTimeOver();
					//send game state
					sendScores();
					new BetweenRoundTimer(PictionaryServer.this,10).start();
					
				}
			}
			
		});
	}


	/**
	 * Registers point drawn in Game and puts frame in queue that sends it to everybody (frame 2)
	 * @param x
	 * @param y
	 * @param playerNumber
	 */
	public void playerDraws(final int x, final int y, final int playerNumber) {
		runqueue.enqueue(new Runnable() {
			public void run() {
				if(game.roundInProgress){
					StCDrawPointFrame f = new StCDrawPointFrame(x,y,playerNumber);
					sendFrameEverybody(f);
	
				}
			}
		});
	}

	/**
	 * Sends what a person says to everybody (frame 4)
	 */
	public void playerChats(final CtSChatFrame f, final byte playerID) {
		runqueue.enqueue(new Runnable () {
			public void run() {
				StCChatFrame f1 = new StCChatFrame(f.getPayload(), playerID);
				sendFrameEverybody(f1);
			}
		});
	}

	/**
	 * Response to a player's guess: checks if they are correct,
	 * if they are correct, it calls another method to handle what happens
	 * if they are wrong, it just sends a message to them that they are wrong (the game doesn't really need to
	 * know if people make wrong guesses...)  (frame 6)
	 */
	public void playerGuesses(CtSGuessFrame f, PlayerHandler p) {
		if(game.roundInProgress){
			boolean b=game.checkGuess(f.getGuess());
			if(b){
				p.upScore();
				roundEndsCorrectGuess(p.getPID());
			}
			else {
				p.sendFrame(new StCGuessWrongFrame());
			}
		}
	}

	/**
	 * If a player joins, this sends a message to everybody that the person joined (frame 8)
	 * and
	 * Sends all the previous players to the self
	 */
	public void playerJoins(final PlayerHandler p) {
		runqueue.enqueue(new Runnable(){
			public void run(){
				sendFrameEverybody(new StCPlayerJoinedFrame(p.getPID(),p.playername));

				ArrayList<PlayerHandler> playerlist=game.getPlayers();

				for(PlayerHandler p1: playerlist){
					StCPlayerJoinedFrame frame = new StCPlayerJoinedFrame(p1.getPID(),p1.playername);
					p.sendFrame(frame);
				}

				if(game.getPlayers().size()>1 && !game.roundInProgress)
					startRound();	


			}
		});

	}


	/**
	 * sends a message if the server is full to that player (frame 9)
	 */
	public void serverFull(PlayerHandler p){
		p.sendFrame(new StCServerFullFrame()); //not used
	}

	/**
	 * sends canvas size to person (frame 10)
	 */
	public void canvasSize(PlayerHandler p){
		p.sendFrame(new StCCanvasSizeFrame(game.canvasX(),game.canvasY()));
	}

	/**
	 * tells game that player left
	 * sends everybody a frame saying that the person left (frame 12)
	 */
	public void playerLeaves(final PlayerHandler p) {
		runqueue.enqueue(new Runnable() {
			public void run() {
				game.removePlayer(p);
				sendFrameEverybody(new StCPlayerLeftFrame(p.getPID()));
			}
		});
	}

	/**
	 * sends message to everybody that round is starting (frame 13)
	 */
	public void sendStart(){
		sendFrameEverybody(new StCRoundBeginFrame());
	}


	/**
	 * sends everybody the list of drawers (frame 14)
	 */
	public void sendDrawers(byte drawer){
		sendFrameEverybody(new StCDrawersAreFrame(drawer));
	}

	/**
	 * sends all of the drawers the word (frame 15)
	 */
	public void sendWord(final PlayerHandler drawer){
		final StCWordIsFrame f = new StCWordIsFrame(game.getCurrentWord());
		runqueue.enqueue(new Runnable() {
			public void run() {
				drawer.sendFrame(f);
			}
		});
	}

	/**
	 * sends correct guesser and their guess to everybody (frame 16)
	 */
	public void sendCorrectGuesser(byte playerID){
		sendFrameEverybody(new StCRoundEndGuessCorrectFrame(playerID,game.getCurrentWord()));
	}

	/**
	 * sends that time is over to everybody (frame 17)
	 */
	public void sendTimeOver(){
		sendFrameEverybody(new StCRoundEndTimeOutFrame());
	}

	/**
	 * sends scores to everybody (frame 18)
	 */
	public void sendScores(){
		sendFrameEverybody(new StCRoundEndGameStateFrame(game.getScorePayload()));
	}



	/**
	 * @param args - not used
	 */
	public static void main(String[] args) { 
		PictionaryServer ps = new PictionaryServer(3500);
		
		System.out.println("server starting");
	}









}
