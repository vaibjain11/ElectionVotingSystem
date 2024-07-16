/**
 * POCandidate is a subclass of Contender. It represents a candidate for the
 * PO election.
 * It has a name and a number of votes.
 * @author Cole Lindfors
 * @version 1.0
 * @since 2023-4-26
 */
public class POCandidate extends Contender{
    private Integer numVotes;


    /**
     * Constructor for POCandidate.
     * @param name The name of the candidate.
     */
    public POCandidate(String name) {
        super(name);
        numVotes = 0;
    }

    /**
     * Gives a vote to the candidate.
     */
    public void giveVote() {
        numVotes++;
    }

    /**
     * Returns the number of votes the candidate has.
     */
    public Integer getBallots() {
        return numVotes;
    }

}
