package Game;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import com.sun.org.apache.bcel.internal.generic.RETURN;

import Server.PictionaryServer;
import Server.PlayerHandler;



public class PictionaryGame {
	
	short CANVASX=500;
	short CANVASY=500;	
	String[] wordUniverse = { "wheel", "island", "turtle", "chair", "ear", "shoe", "basketball", 
			"octopus", "bed", "flag", "castle", "paint", "car", "horse", "pinwheel", "kite",
			"safetypin", "submarine", "watermelon", "tea", "telephone", "whistle", "piano", "clam",
			"ring", "frog", "olive", "mailman", "mountain", "camel", "wind", "summer", "green",
			"surfboard", "cow", "pencil", "shower", "glasses", "stove", "chimney", "window",
			"rainbow", "moon", "peacock", "sky", "ocean", "volcano", "dinosaur", "whale", 
			"elephant", "flea", "snail", "fireplace", "forest", "spoon", "lace", "gasoline", 
			"rice", "honeybee", "shoulderpad" };
	String currentWord;
	public boolean roundInProgress;
	PlayerHandler drawer;
	ArrayList<PlayerHandler> players;
	public int roundNumber;
	private int i;
	
	BufferedReader br;
	
	
	PictionaryServer s;
	
	// should know:
		// what words we have
		// check a guess against the current word
		// register a point drawn
		// who the drawers are 
		// who to pick to draw
	public PictionaryGame(PictionaryServer s) {
		this.s=s;
		i = 0;
		players = new ArrayList<PlayerHandler>(0);
		roundInProgress = false;
		//wordUniverse = createUniverse("words");
		currentWord = wordUniverse[(int) (Math.random()*wordUniverse.length)];

	}
	
	public short canvasX(){
		return CANVASX;
	}
	
	public short canvasY(){
		return CANVASY;
	}
	
	
	/**
	 * Returns current word
	 */
	public String getCurrentWord(){
		return currentWord;
	}
	
	public int numberOfPlayers(){
		return players.size();
	}
	/**
	 * gets the universe of words that can be guessed
	 */
	public String[] getWordUniverse() {
		return wordUniverse;
	}
	
	/**
	 * changes the current word to another randomly selected word in the word universe
	 */
	public void changeWord() {
		currentWord = wordUniverse[(int) (Math.random()*wordUniverse.length)];
	}
	
	/**
	 * returns whether or not the guess is the correct word
	 * @param guess 
	 * @return
	 */
	public boolean checkGuess(String guess) {
		return guess.equals(currentWord);
	}
	
	/**
	 * adds player p to the game
	 * @param p
	 */
	public void addPlayer(PlayerHandler p) {
		players.add(players.size(), p);	
	}
	
	/**
	 * removes player p from the game
	 * @param p
	 */
	public void removePlayer(PlayerHandler p) {
		players.remove(p);
	}
	
	
	/**
	 * gets the array of players currently playing
	 * @return
	 */
	public ArrayList<PlayerHandler> getPlayers() {
		return players;
	}
	/** 
	 * for now just returns one player randomly chosen from the player list
	 * @return
	 */
	public PlayerHandler getDrawers() {
		return drawer;
	}
	
	public void changeDrawer() {
		drawer = players.get(i % players.size());		
		++i;
	}
	
	/**
	 * returns the player IDs and scores needed for frame 18 payload
	 * @return
	 */
	public byte[] getScorePayload() {
		byte[] ans = new byte[players.size() * 2];
		for (int i = 0; i < ans.length/2; ++i) {
				ans[i*2] = players.get(i).getPID();
				ans[i*2+1] = (byte) players.get(i).score;
		}
		return ans;
	}
	
	public String[] createUniverse(String filename) {
		try {
			br = new BufferedReader(
					new InputStreamReader(
							new FileInputStream(filename)
					)
			);
			String line;
			LinkedList<String> wordList = new LinkedList<String>();
			while ((line = br.readLine()) != null) {
				wordList.add(line);
			}
			return (String[]) wordList.toArray();
		} catch (FileNotFoundException e) {
			System.out.println("File cannot be found.");
			e.printStackTrace();
		} catch (IOException z) {
			System.out.println("We are illiterate. Forgive us!");
			z.printStackTrace();
		}
		return new String[] {"failure", "unemplotment", "living with your parents at age 35"};
	}
	
}
