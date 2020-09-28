/**
 * creates battleship of given length and alignment
 * @author -Nikhil yadav
 */

import javafx.util.Pair;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BattleShip implements Serializable {
    int shipLength;
    boolean horizontal=true;
    private int life;

    List<Pair<Integer,Integer>> pairList = new ArrayList<Pair<Integer,Integer>>();

    /**
     * creates battleship of given length and alignment
     * @param shipType
     * @param horizontal
     */
    public BattleShip(int shipType, boolean horizontal){
        this.shipLength=shipType;
        this.life=shipType;
        this.horizontal=horizontal;
    }


    /**
     * checks if ship is alive
     * @return
     */
    public boolean alive(){
        return life>0;
    }

    /**
     * reduces life of ship when dealt with a blow
     */
    public void blow(){
        life--;
    }

}
