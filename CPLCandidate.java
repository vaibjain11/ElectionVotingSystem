/**
* CPLCandidate class is used for storing candidate names and whether
* they have a seat or not.
*
* @author  Ian Paterson
* @version 1.0
* @since   2023-3-21
*/
public class CPLCandidate {
    private boolean hasSeat;
    private String name;
    
    /**
     * Constructor
     * Sets the name of the candidate and sets their seat status to false.
     */
    CPLCandidate(String name) {
        this.name = name;
        this.hasSeat = false;
    }
    
    /**
     * sets whether the candidate has been given a seat or not
     * @param bool the boolean setting if they have a seat
     */
    public void setHasSeat(boolean bool) {
        hasSeat = bool;
    }

    /**
     * checks if a candidate has a seat
     * @return true if so, false otherwise
     */
    public boolean hasSeat() {
        return hasSeat;
    }

    /**
     * returns the name of the candidate
     */
    public String getName() {
        return name;
    }



    
}
