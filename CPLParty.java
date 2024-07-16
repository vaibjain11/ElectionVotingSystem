import java.util.ArrayList;

/**
* CPLParty Class used for storing party information and
* used for handing out seats.
*
* @author  Ian Paterson
* @author  Cole Lindfors
* @version 1.1
* @since   2023-3-21
*/
public class CPLParty extends Contender implements Comparable<CPLParty> {
    private Integer seatsGiven;
    private Integer numVotes;
    private boolean seatsAvailable;
    private ArrayList<CPLCandidate> candidateList;

    /**
     * Constructor.
     * Creates and initializes new CPLParty instance
     */
    CPLParty(String partyName) {
        super(partyName);
        this.candidateList = new ArrayList<CPLCandidate>(1);
        this.seatsGiven = 0;
        this.numVotes = 0;
        this.seatsAvailable = true;
        this.seatsGiven = 0;
    }
    
    /**
     * adds a candidate to the party 
     * @param cand the candidate to be added
     */
    public void addCandidate(CPLCandidate cand) {
        candidateList.add(cand);
    }
    
    /**
     * gives a vote to the party
     */
    public void giveVote() {
        numVotes++;
    }
    
    /**
     * gets the number of votes the party has
     * @return number of seats the party has
     */
    public Integer getBallots() {
        return numVotes;
    }

    /**
     * gets the number of candidates the party has
     * @return number of candidates the party has
     */
    public Integer getNumCandidates() {
        return candidateList.size();
    }

    /**
     * gets the number of votes the party has
     * @return number of seats the party has
     */
    public Integer getNumSeats() {
        return seatsGiven;
    }

    /**
     * gets the next candidate in line for recieving a seat
     * @return the candidate to be given a seat.
     */
    public CPLCandidate getNextCandidate() {
        return candidateList.get(seatsGiven);
    }
    
    /**
     * sets whether there are seats available in the party
     * @param bool whether there are seats available.
     */
    public void setSeatsAvailable(boolean bool) {
        this.seatsAvailable = bool;
    }
    
    /**
     * checks if there are available seats to be distributed for a party
     * @return true if there are seats available in a party, false otherwise
     */
    public boolean hasAvailableSeats() {
        return seatsAvailable;
    }
    
    /**
     * gives a seat to a Candidate if available
     * @return true if a seat has been given, false otherwise
     */
    public boolean giveSeat(Integer quota) {
        if(seatsAvailable) {
            candidateList.get(seatsGiven).setHasSeat(true); //gives candidate a seat
            seatsGiven++; // tallies the seats distributed to the party
            if(seatsGiven == candidateList.size()) { //flags for no longer having available candidates - overloads if necessary
                seatsAvailable = false;
            }
            numVotes = Math.max(0, numVotes - quota); //TODO: how do we want to pass this through
            return true;
        }
        numVotes = Math.max(0, numVotes - quota);
        return false;
    }

    /**
     * getter for candidate names
     * @return all the candidate names in a one-line string 
     */
    public String getCandidateNames() {
        String tempString = "";
        for (CPLCandidate candidate : candidateList) {
            tempString += candidate.getName();
        }
        return tempString;
    }

    /**
     * compares parties based on their number of votes
     * @param other the other party to compare to
     * @return 0 if they have the same votes, >0 if this party has less votes than
     * the other party, <0 if this party has more votes
     */
    @Override
    public int compareTo(CPLParty other) {
        return other.getBallots() - this.numVotes;
    }
    
    /**
     * compares the candidates in two different parties
     * @param o other party to compare
     * @return true if they contain the same candidates, false otherwise
     */
    public boolean containsSameCandidates(CPLParty o) {
        // Compare the data members and return accordingly
        String candidateNames1 = this.getCandidateNames();
        String candidateNames2 = o.getCandidateNames();
        return candidateNames1.equals(candidateNames2);
    }

    /**
     * gives the list of candidates
     * @return the list of candidates
     */
    public ArrayList<CPLCandidate> getCandidateList() {
        return candidateList;
    }

    /**
     * Getter for seatsGiven
     * @return the amount of seats awarded thusfar to a party
     */
    public int getSeatsGiven() {
        return seatsGiven;
    }
    
}
