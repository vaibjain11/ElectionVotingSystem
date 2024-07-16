import java.util.ArrayList;
import java.util.Collections;

/**
* CPLElection class
* Runs CPL elections. 
*
* @author  Ian Paterson
* @author  Cole Lindfors
* @version 1.2
* @since   2023-3-22
*/
public class CPLElection extends Election {
    private ArrayList<CPLParty> partyList;
    private Integer numSeats;
    private Integer remainingSeats;
    private Integer overloadSeats;
    private Integer seatsDistributed;
    private Integer quota;

    /**
     * Constructor. Initializes the CPL instance
     * @param scanner the scanner to be used for this instance
     */
    CPLElection(ArrayList<ElectionScanner> scanners) {
        super(scanners);
        output = new OutputWriter("CPL", scanners.get(0).getDateString());
        partyList = null;
        numSeats = null;
        remainingSeats = null;
        overloadSeats = 0;
    }
    
    /**
     * the main code for running CPL elctions. Uses the output writer to output to audit file.
     */
    public String runCPL() {
        createParties();
        electionScanners.get(0).getCPLCandidates(partyList);  //adds all candidates to party partyList[i]
        numSeats = electionScanners.get(0).getSeats();
        remainingSeats = numSeats;
        numTotalBallots = electionScanners.get(0).getNumBallots(); // POSSIBLY UNUSED?
        for (int i = 1; i < electionScanners.size(); i++) {
            numTotalBallots += electionScanners.get(i).readCPLExtraHeaders();
        }
        seatsDistributed = 0;
        quota = calcQuota();
        // Adds ballots to each party
        for (ElectionScanner s : electionScanners) {
            s.tallyCPLBallots(partyList);
        }
        output.makeHeader();
        output.writeQuota(quota);
        output.writeCPLInitialVotes(partyList);
        distributeSeats();
        distributeRemainders();
        if(overloadSeats>0){ 
            partyOverload();
        }
        boolean printParty = false;
        String results = "The candidate(s) receiving seats are";
        for (CPLParty party : partyList) {
            for (CPLCandidate cand : party.getCandidateList()) {
                if (cand.hasSeat()) {
                    results += " " + cand.getName() + ",";
                    printParty = true;
                }
            }
            if (printParty) {
                results = results.substring(0,results.length() - 1);
                results += " from the " + party.getName() + " party. ";
                printParty = false;
            }
        }
        output.writeCPLFinalStandings(partyList);
        output.writeString(results);
        return results;
    }
    
    /**
     * creates the parties for the election from the ballot file
     */
    public void createParties() {
        electionScanners.get(0).getNumContenders();
        ArrayList<String> names = electionScanners.get(0).getContenders();
        partyList = new ArrayList<CPLParty>();
        for (String name: names) {
            partyList.add(new CPLParty(name));
        }
    }
    
    public ArrayList<CPLParty> getPartyList() {
        return partyList;
    }

    public void setNumSeats(int numSeats) {
        this.numSeats = numSeats;
    }

    public void setRemainingSeats(int remainingSeats) {
        this.remainingSeats = remainingSeats;
    }

    public void setSeatsDistributed(int seats) {
        seatsDistributed = seats;
    }
    /**
     * distributes the seats in the first round of the CPL election.
     */
    public void distributeSeats() { // distributing seats from the initial quota
        for (CPLParty party : partyList) {
            for(int i = 0; i < (party.getBallots() / quota); i++) {
                boolean seatGiven = party.giveSeat(quota); //gives a seat to the party
                if (!seatGiven) {
                    overloadSeats++; //tallies the overloaded seats for later processing
                }
                remainingSeats--;
            }
        }
        output.writeString("\nFirst allocation of seats:\n");
        output.writeCPLInitialAlloc(partyList, overloadSeats, remainingSeats, seatsDistributed);
    }
    
    /**
     * distributes the remainders in the second round of the CPLElection. 
     * picks the parties with the most amount of votes and breaks ties if 
     * necessary to give them extra seats based on their remainder.
     */
    public void distributeRemainders() {

        output.writeString("\nSecond allocation of seats:\n");

        ArrayList<CPLParty> sortedList = new ArrayList<CPLParty>(partyList);
        Collections.sort(sortedList); //sort partyList by number of votes

        while(remainingSeats>0) {
            // create a new arraylist to keep track of parties with maximum votes:
            ArrayList<CPLParty> MaxVotes = new ArrayList<CPLParty>(); 
            MaxVotes.add(sortedList.get(0)); // add first party object to the list of parties with max votes
            int Max = sortedList.get(0).getBallots(); // keep track of the number of votes required to be considered
            int i = 0;
            // Find all the parties with the same amount of max votes
            while (i < sortedList.size() && sortedList.get(i).getBallots() == Max) {
                MaxVotes.add(sortedList.get(i)); // adds to a list: all of the parties with the same maximum remainder
                i++;
            }
            if (MaxVotes.size() > 1) { // if there is a tie
                CPLParty tempParty = coinToss(MaxVotes);
                output.writeBreakATie(MaxVotes, tempParty);
                int index = sortedList.indexOf(tempParty); //index of the winning party
                boolean seatGiven = sortedList.get(index).giveSeat(quota); // try to give a seat to a party selected 
                if (!seatGiven) { // no more candidates for tempParty
                    overloadSeats++; //tallies the overloaded seats for later processing
                }
                sortedList.remove(index);
            }
            else { // not a tie, has max remainder
                boolean seatGiven = sortedList.get(0).giveSeat(quota);
                if (!seatGiven) { // no more candidates for selected party
                    overloadSeats++; //tallies the overloaded seats for later processing
                    output.writeString(sortedList.get(0) + " party has no more candidates, random party must be chosen\n");
                }
                else { // has candidates
                    output.writeString(sortedList.get(0) + " party has the next highest remainder, earning them a seat.\n");
                }
                sortedList.remove(0);
            }
            remainingSeats--;
        }	
    }
    
    /**
     * the final stage of CPL Elections, distributes votes randomly
     * to the remaining parties given the amount of overloaded seats
     */
    public void partyOverload() {
        output.writeString("Distributing Seats not distributed due to lack of candidates:\n");
        while (overloadSeats > 0) {
            CPLParty selectedParty = coinToss(partyList);
            boolean seatGiven = selectedParty.giveSeat(quota);
            if (seatGiven) {
                output.writeString(selectedParty.getName() + "was awarded a seat via coin toss\n");
                overloadSeats--;
            }
            //otherwise leave the same and retry the coin flip
        }
    }
    

    public void setQuota(int i) {
        quota = i;
    }

    /**
     * Calculates the quota. number of total ballots / number of seats
     * @return the quota value
     */
    public Integer calcQuota() {
        return (int) Math.ceil((double) numTotalBallots / numSeats);
    }
    
    /**
     * getter for remainingSeats
     * @return the number of remaining seats.
     */
    public Integer getRemainingSeats() {
        return remainingSeats;
    }
    
}