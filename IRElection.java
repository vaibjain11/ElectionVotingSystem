/**
* The IR Election class represnts an Instant Runoff (IR) Election, 
* which extends from the base class Election. It provides functionality
* for running the election, checking for a majority winner, determining 
* elimination and popularity, and redistributing votes.
* @author  Patrick Hunner
* @author  Vaibhav Jain
* @author  Cole Lindfors
* @version 1.2
* @since   2023-3-20
*/

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;


public class IRElection extends Election {

    private Integer numCandidates;
    private ArrayList<IRCandidate> candidates;
    private IRCandidate winner;
    private Integer unaccountedVotes;
    private Integer invalidVotes;
    private ArrayList<Integer> exhaustedVotes;


    /**
     * Constructs an IRElection object.
     * 
     * @param scanners the ElectionScanner objects used to read in election data.
     */
    IRElection(ArrayList<ElectionScanner> scanners) {
        super(scanners);
        output = new OutputWriter("IR", scanners.get(0).getDateString());
        // electionScanner = scanner;
        numTotalBallots = 0;
        numCandidates = null;
        invalidVotes = 0;
        candidates = new ArrayList<IRCandidate>();
        exhaustedVotes = new ArrayList<Integer>();
        unaccountedVotes = 0;
    }

    IRElection(ElectionScanner scanner) {
        super(scanner);
        output = new OutputWriter("IR", scanner.getDateString());
        // electionScanner = scanner;
        numTotalBallots = null;
        numCandidates = null;
        invalidVotes = 0;
        candidates = new ArrayList<IRCandidate>();
        unaccountedVotes = 0;
    }


    /**
     * Runs the entire IR election process and returns the winner.
     * 
     * @return a String representing the name of the winner and their final vote count.
     */
    public String runIR() {
        output.makeHeader();
        createCandidates();
        numTotalBallots = electionScanners.get(0).getNumBallots();
        for (int i = 1; i < electionScanners.size(); i++) {
            numTotalBallots += electionScanners.get(i).readIRExtraHeaders();
        }
        tallyInitialVotes();
        invalidateBallots();
        
        int roundNum = 1;
        logRound(roundNum);
        while (true) {
            output.stateRound(roundNum);
            output.showContenders(candidates, unaccountedVotes);
            winner = checkMajority();
            if (winner == null) {
                if (numActive() <= 2) {
                    winner = determinePopularity();
                    break;
                }
                roundNum++;
                redistribute(determineElim());
                exhaustedVotes.add(unaccountedVotes + invalidVotes);
                logRound(roundNum);
            }
            else {
                break;
            }
        }
        output.writeResultsTable(candidates, exhaustedVotes);
        return winner.name + " is the winner with a final " + Integer.toString(winner.getBallots()) + " votes out of " + Integer.toString(numTotalBallots);
    }

    /**
     * Checks if there is a candidate who has a majority of the votes.
     * 
     * @return the IRCandidate object representing the winner, or null if there is no majority winner yet.
     */
    public IRCandidate checkMajority() {
        for (IRCandidate c : candidates) {
            if (c.getBallots() > numTotalBallots / 2) {
                output.writeMajorityWinner(c);
                return c;
            }
        }
        return null;
    }

    /**
     * Updates the candidates ArrayList with the IRCandidate objects.
     */
    public void createCandidates() {
        numCandidates = electionScanners.get(0).getNumContenders();
        ArrayList<String> contenders = electionScanners.get(0).getContenders();
        for (String name : contenders) {
            candidates.add(new IRCandidate(name));
        }
    }

    /**
     * Determines the candidate to eliminate in the current round of the election.
     *
     * @return The candidate to eliminate.
     */
    public IRCandidate determineElim() {
        ArrayList<IRCandidate> lowestVotes = new ArrayList<IRCandidate>();
        Integer lowestVoteCount = numTotalBallots;
        int curCandVotes;
        for (IRCandidate c : candidates) {
            curCandVotes = c.getBallots();
            if (!c.isEliminated() && curCandVotes <= lowestVoteCount) {
                if (curCandVotes < lowestVoteCount) {
                    lowestVoteCount = curCandVotes;
                    lowestVotes.clear();
                    lowestVotes.add(c);
                }
                else {
                    lowestVotes.add(c);
                }
            }
        }
        if (lowestVotes.size() == 1) {
            return lowestVotes.get(0);
        }
        IRCandidate elim = coinToss(lowestVotes);
        output.writeBreakATie(lowestVotes, elim);
        return elim;
    }

    /**
     * Determines the winner of the election in cases where there is no clear majority. Uses a fair coin toss
     * in the case of a tie.
     *
     * @return The winner of the election.
     */
    public IRCandidate determinePopularity() {
        ArrayList<IRCandidate> lastcandidates = new ArrayList<IRCandidate>();
        if (candidates.size() == 1) {
            return candidates.get(0);
        }
        for (IRCandidate c : candidates) {
            if (!c.isEliminated()) {
                lastcandidates.add(c);
            }
        }
        IRCandidate finalCandidate;
        int candOneVotes = lastcandidates.get(0).getBallots();
        int candTwoVotes = lastcandidates.get(1).getBallots();
        if (candOneVotes > candTwoVotes) {
            finalCandidate = lastcandidates.get(0);
        }
        else if (candTwoVotes > candOneVotes) {
            finalCandidate = lastcandidates.get(0);
        }
        else {
            finalCandidate = coinToss(lastcandidates);
            output.writeBreakATie(lastcandidates, finalCandidate);
        }
        output.writePopularityWinner(finalCandidate);
        return finalCandidate;
    }

    /**
    * Redistributes votes from a candidate that has been eliminated in the previous round.
    *
    * @param candidate: the candidate being eliminated
    */
    public void redistribute(IRCandidate candidate) {
        int numBallots = candidate.getBallots();
        IRCandidate toReceiveVote = null;
        Ballot toRedistribute;
        Integer nextRating;
        HashMap <String, Integer> candidatesGivenVotes = new HashMap<>();
        for (int i = numBallots; i > 0; i--) {
            toRedistribute = candidate.popVote();
            do {
                nextRating = toRedistribute.popRanking();
                if (nextRating == 0) {
                    unaccountedVotes++;
                    break;
                }
                toReceiveVote = candidates.get(nextRating - 1);
            } while(toReceiveVote.isEliminated());
            if (toReceiveVote != null) {
                toReceiveVote.pushVote(toRedistribute);
                String receivingName = toReceiveVote.getName();
                int newVotes = candidatesGivenVotes.getOrDefault(receivingName, 0) + 1;
                candidatesGivenVotes.put(receivingName, newVotes);
            }
        }
        candidate.setEliminated();
        output.writeVoteRedistribution(candidatesGivenVotes);
    }

    /**
     * Gets the number of candidates in the election that haven't been eliminated.
     * 
     * @return an int of the non eliminated candidates.
     */
    public int numActive() {
        int active = 0;
        for (IRCandidate cand : candidates) {
            if (!cand.isEliminated()) {
                active++;
            }
        }
        return active;
    }

    /**
     * Tallies the initial votes from all files for an Instant Runoff election.
     */
    public void tallyInitialVotes() {
        for(ElectionScanner sc : electionScanners) {
            sc.tallyIRBallots(candidates);
        }
        // electionScanner.tallyIRBallots(candidates, unaccountedVotes);
    }

    /**
     * invalidates the ballots of voters that do not have >= .5 of the candidates ranked
     */
    public void invalidateBallots() {
        for (IRCandidate cand : candidates) {
            Stack<Ballot> bal = cand.getBallotList();
            int index = 0;
            while (index < bal.size()) {
                // Check if ballot is valid (has >= 1/2 of choices ranked)
                if ((double)(bal.get(index).getNumRankings() + 1) / numCandidates < .5) {
                    bal.get(index).invalidate();
                }
                if (!bal.get(index).isValid()) {
                    invalidVotes++;
                    cand.getBallotList().remove(bal.get(index));
                    // output to file here
                }
                else index++;
            }
        }
        exhaustedVotes.add(numTotalBallots); //adds number of total ballots for the first index for use in table printing
        exhaustedVotes.add(invalidVotes);
        numTotalBallots -= invalidVotes;     
    }

    /**
     * log round votes for each candidate
     * @param round the round
     */
    public void logRound(int round) {
        for (IRCandidate cand : candidates) {
            cand.logRoundVotes(round);
        }
        System.out.println(round);
    }

    /**
     * Gets the number of candidates in the election.
     *
     * @return the number of candidates
     */
    public int getNumCandidates() {
        return numCandidates;
    }

    /**
     * Gets the list of candidates in the election.
     *
     * @return the list of candidates
     */
    public ArrayList<IRCandidate> getCandidates() {
        return candidates;
    }

    /**
     * Gets the winner of an IR election
     * 
     * @return the IRCandidate that won
     */
    public IRCandidate getWinner() {
        return winner;
    }

    /**
     * Gets the number of ballots that have no more viable choices.
     * 
     * @return the number of votes that are no longer counted
     */
    public Integer getUnaccountedVotes() {
        return unaccountedVotes;
    }

    /**
     * Sets the number of candidates in an IR election
     * 
     * @param newNumCandidates: The new number of candidates
     */
    public void setNumCandidates(Integer newNumCandidates) {
        numCandidates = newNumCandidates;
    }

    /**
     * Sets the candidates of an election
     * 
     * @param newCandidates: the ArrayList of candidates
     */
    public void setCandidates(ArrayList<IRCandidate> newCandidates) {
        candidates = newCandidates;
    }

    /**
     * Set the winner of an election
     * 
     * @param newWinner: the IRCandidate that won
     */
    public void setWinner(IRCandidate newWinner) {
        winner = newWinner;
    }
    
    /**
     * Set the number of unaccounted votes
     * 
     * @param votes: Integer value of unaccounted votes
     */
    public void setUnaccountedVotes(Integer votes) {
        unaccountedVotes = votes;
    }

    /**
     * Returns the list of votes for each round for a candidate
     * @param cand the candidate number requested
     * @return the list of rankings if the candidate exists or null if it doesn't
     */
    public Integer[] getCandidateVotes(int cand) {
        if (candidates.size() > cand) {     
            return candidates.get(cand).getRoundVotes();
        }
        else return null;
    }


}