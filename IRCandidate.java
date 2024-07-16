
import java.util.ArrayList;
import java.util.Stack;


/**
 * IRCandidate represents a candidate in an instant runoff election.
 * It extends the Contender class which has the name of the candidate
 * and no. of ballots assigned.
 * @author  Patrick Hunner
 * @author  Vaibhav Jain
 * @author  Cole Lindfors
 * @version 1.0
 * @since   2023-3-20
*/
public class IRCandidate extends Contender {
    private Stack<Ballot> ballotList;
    private boolean eliminated;
    private ArrayList<Integer> roundVotes;

    /**
     * Constructor for IRCandidate.
     * @param name The name of the candidate.
    */
    public IRCandidate(String name) {
        super(name);
        eliminated = false;
        ballotList = new Stack<Ballot>();
        roundVotes = new ArrayList<Integer>();
        roundVotes.add(0);
    }

    /**
    * Returns whether the candidate has been eliminated from the election.
    * @return True if the candidate has been eliminated, false otherwise.
    */
    public boolean isEliminated() {
        return eliminated;
    }

    /**
    * Removes and returns the next ballot from the candidate's list of received ballots.
    * @return The next ballot from the candidate's list of received ballots.
    */
    public Ballot popVote() {
        return ballotList.pop();
    }

    /**
    * Adds a ballot to the candidate's list of received ballots.
    * @param vote: The ballot to add to the candidate's list of received ballots.
    */
    public void pushVote(Ballot vote) {
        ballotList.add(vote);
    }

    /**
     * Log the current amount of votes under this round.
     * @param round the round to log to
     */
    public void logRoundVotes(int round) {
        while (round >= roundVotes.size()) {
            roundVotes.add(0);
        }
        roundVotes.set(round, ballotList.size());
    }

    public Stack<Ballot> getBallotList() {
        return ballotList;
    }
    
    public Integer getBallots() {
        return ballotList.size();
    }

    public void setEliminated() {
        eliminated = true;
    }

    /**
     * return a string list containing the votes logged in each round
     * @return list containing vote count
     */
    public Integer[] getRoundVotes() {
        Integer[] temp = new Integer[roundVotes.size()];
        int i = 0;
        for (int votes : roundVotes) {
            temp[i++] = votes;
        }
        return temp;
    }

    /**
     * provides functionality to give data necessary for a table print for round statistic
     * @return the candidate's information on the election
     */
    public String[] getTableRow() {
        String tempString[] = new String[(roundVotes.size() +1)*2];
        String nameAndParty[] = name.split("\\(");
        if (nameAndParty.length >= 2) {
            tempString[0] = nameAndParty[0].substring(0, nameAndParty[0].length()-1);
            tempString[1] = nameAndParty[1].split("\\)")[0];
        }
        else {
            tempString[0] = nameAndParty[0];
            tempString[1] = "N/A";
        }
        for (int i = 1; i < roundVotes.size(); i++) {
            tempString[i*2] = "" + roundVotes.get(i);
            tempString[i*2 + 1] = (roundVotes.get(i) - roundVotes.get(i-1)) + "";
        }
        return tempString;
    }
}