import java.util.ArrayList;

/**
 * The PO Election class represents an election using the Popularity Only method.
 * It extends from the base class Election. It provides functionality for running
 * the election, checking for a popularity winner, outputting vote percentages,
 * breaking ties, and determining the winner.
 * @author  Cole Lindfors
 * @version 1.0
 * @since   2023-4-26
 */
public class POElection extends Election {
    
    private ArrayList<POCandidate> candidates;
    private POCandidate winner;

    /**
     * Constructor
     * @param electionScanner the electionScanner to be used for the election
     */
    POElection(ArrayList<ElectionScanner> electionScanners) {
        super(electionScanners);
        numTotalBallots = null;
        candidates = new ArrayList<POCandidate>();
    }

    /**
     * Runs the entire PO election process and returns the winner.
     * @return a String representing the name of the winner and their final vote count.
     */
    public String runPO() {
        createCandidates();
        numTotalBallots = electionScanners.get(0).getNumBallots();
        for (int i = 1; i < electionScanners.size(); i++) {
            numTotalBallots += electionScanners.get(i).readPOExtraHeaders();
        }
        tallyVotes();
        ArrayList<POCandidate> potentialWinners = findMostVotes();
        if (potentialWinners.size() == 1) { // no tie
            winner = potentialWinners.get(0);
            System.out.println(winner.getName() + " won with the most votes.");
        }
        else { // tie
            System.out.println("Tie between " + potentialWinners.size() + " candidates.");
            winner = coinToss(potentialWinners);
        }
        return winner.getName() + " won with " + winner.getBallots() + " votes.";
    }

    /**
     * Creates the candidate objects for the PO election based on the candidate names,
     * and adds them to the candidates ArrayList.
     */
    public void createCandidates() {
        electionScanners.get(0).getNumContenders(); // Don't need this value, but it moves the scanner to the right place
        ArrayList<String> candidateNames = electionScanners.get(0).getPOCandidates();
        for (String name : candidateNames) {
            candidates.add(new POCandidate(name));
        }
    }

    /**
     * Tallies the ballots for PO elections.
     * Tallies for all of the ballot files.
     */
    public void tallyVotes() {
        for (ElectionScanner s : electionScanners) {
            s.tallyPOBallots(candidates);
        }
    }

    /**
     * finds a list of the top vote getters in the election, 
     * and also prints out the vote percentages for each candidate
     * @return an ArrayList of the top vote getters
     */
    public ArrayList<POCandidate> findMostVotes() {
        ArrayList<POCandidate> winners = new ArrayList<POCandidate>();
        int maxVotes = 0;

        for (POCandidate c : candidates) {
            float percent = ((float) c.getBallots() / (float) numTotalBallots) * 10;
            System.out.println(c.getName() + " received " + c.getBallots() + " votes, or " + percent + "% of the vote.");
            if (c.getBallots() == maxVotes) { // tie between vote leaders
                winners.add(c);
            }
            else if (c.getBallots() > maxVotes) { // new maxVotes
                maxVotes = c.getBallots();
                winners.clear();
                winners.add(c);
            }
        }
        return winners;
    }

    /**
     * Gets the list of candidates
     */
    public ArrayList<POCandidate> getCandidates() {
        return candidates;
    }

    /**
     * Gets the winner of the election
     */
    public POCandidate getWinner() {
        return winner;
    }

    /**
     * set numTotalVotes
     */
    public void setNumTotalBallots(int num) {
        numTotalBallots = num;
    }
    
}
