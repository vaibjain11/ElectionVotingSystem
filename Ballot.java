/**
* The Ballot class acts as a data structure storing Instant Runoff
* Ballots, keeping track of the order of their vote choices
*
* @author  Patrick Hunner
* @author  Ian Paterson
* @version 1.0
* @since   2023-3-20
*/
import java.util.ArrayList;

public class Ballot {
    private ArrayList<Integer> rankings;
    private boolean isValid;

    public Ballot(ArrayList<Integer> rankings) {
        this.rankings = rankings;
        this.isValid = true;
    }

    public Integer popRanking() {
        if (rankings.size() > 0) {
            return rankings.remove(0);
        }
        return null;
    }

    /**
     * sets a Ballot to be eliminated
     */
    public void invalidate() {
        isValid = false;
    }

    /**
     * returns if the ballot is valid or not
     * @return true if the ballot is valid, false otherwise
     */
    public boolean isValid() {
        return isValid;
    }

    /**
     * Return the number of rankings a ballot has
     * @return number of candidates ranked
     */
    public int getNumRankings () {
        return rankings.size();
    }
}