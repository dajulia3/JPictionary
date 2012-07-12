/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package client;

/**
 *
 * @author jdl3
 */
public class Player {
    public final int playerId;
    public String name;
    public boolean drawer;
    
    public int score;

    public Player(int playerId, String name, boolean drawer) {
        this.playerId = playerId;
        this.name = name;
        this.drawer = drawer;
    }

    public Player() {
        this(-1, "", false);
    }

    public String toString() {
        return name;
    }

    public boolean equals(Player other) {
        return playerId == other.playerId;
    }

    public int hashCode() {
        return playerId;
    }

}
