// import java.util.Arrays;
import java.io.File;
import java.io.FileWriter;   // Import the FileWriter class
import java.io.IOException;  // Import the IOException class to handle errors
import java.util.ArrayList;
import java.util.HashMap;

/**
* OutputWriter writes election results to the audit file
*
* @author  Cole Lindfors
* @version 23.0
* @since   2023-3-22
*/
public class OutputWriter {

    private String fileName;
    
    /**
     * 
     * @param electionType type of election, IR or CPL
     * @param date date of running the election algorithm
     */
    OutputWriter(String electionType, String date) {
        fileName = "audit/" + electionType + "_" + date + ".txt";
        makeAuditDirectory();
        createAuditFile(fileName);
    }

    /**
     * Makes the audit file directory if needed
     */
    private void makeAuditDirectory() {
        File directory = new File("audit");
        // Create the directory if it doesn't exist
        if (!directory.exists()) {
            boolean isCreated = directory.mkdirs();
            if (!isCreated) {
                System.out.println("Failed to create the directory!");
            }
        }
    }

    /**
     * Creates file with name fileName
     * @param fileName fileName to create
     */
    private void createAuditFile(String fileName) {
        File file = new File(fileName);
        try {
            if (file.createNewFile()) {
              System.out.println("Audit file created: " + file.getName());
            } else {
              System.out.println("File already exists.");
            }
          } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
          }
    }

    /**
     * Helper function to write to audit file using FileWriter
     * @param text
     */
    public void writeString(String text) {
        try {
            FileWriter writer = new FileWriter(fileName, true);
            writer.write(text);
            writer.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
    /**
     * Write to the audit file the header for the election
     */
    public void makeHeader() {
        writeString(fileName + "\n");
    }

    /**
     * Write to the audit file how many rounds of IR have been completed
     * @param roundNumber current round number
     */
    public void stateRound(int roundNumber) {
        writeString("Round " + roundNumber + '\n');
    }
    /**
    * Write to the audit file the current contenters each round (and unaccounted votes)
    * @param candidates current candidates list
    * @param unaccountedVotes number of unnaccounted votes
    */
    public void showContenders(ArrayList<IRCandidate> candidates, int unaccountedVotes) {
        String output = "";
        for (IRCandidate c : candidates) {
            output += "Candidate " + c.getName() + ": " + c.getBallots() + " Vote(s)\n";
        }
        if (unaccountedVotes != 0) {
            output += "Unaccounted: " + unaccountedVotes + " Vote(s)";
        }
        writeString(output);
    }

    /**
     * Write to the audit file the candidate details of the majority winner
     * @param candidate candidate that won the election by majority
     */
    public void writeMajorityWinner(IRCandidate candidate) {
        String output = candidate.getName() + " wins by majority vote";
        writeString(output);
    }

    /**
     * Write to the audit file the candidate details of the popularity winner
     * @param candidate candidate that won the election by popularity
     */
    public void writePopularityWinner(IRCandidate candidate) {
        String output = candidate.getName() + " wins by majority vote";
        writeString(output);
    }

    /**
     * Write to the audit file the result after each run of the coin toss 

     * @param tiedContenders list of contenders who could be picked
     * @param winner contender which was picked
     */
    public <T extends Contender> void writeBreakATie(ArrayList<T> tiedContenders, T winner) {
        String output = "";
        if (fileName.startsWith("IR")) {
            // Election type is IR
            output += "Tie between " + tiedContenders.get(0).getName();
            for (int i = 1; i < tiedContenders.size(); i++) {
                output += ", " + tiedContenders.get(i).getName();
            }
            output += "\n" + winner.getName() + " Eliminated via coin toss";
        } else {
            // Election type is CPL
            output += "The next highest remainder is a tie between " + tiedContenders.size() + " parties, ";
            output += "random party will be chosen\n";
            output += "Tie between " + tiedContenders.get(0).getName() + " party";
            for (int i = 1; i < tiedContenders.size(); i++) {
                output += ", " + tiedContenders.get(i).getName() + " party";
            }
            output += "\n" + winner.getName() + " was awarded a seat via coin toss\n";
        }
        writeString(output);
    }

    /**
     * Writes candidates who recieved new votes and how many, which were
     * given votes via a vote redistribution
     * @param candidateGivenVotes Hashmap with key = candidateName, value = 
     */
    public void writeVoteRedistribution(HashMap<String, Integer> candidateGivenVotes) {
        String output = "";
        for (String candidateName : candidateGivenVotes.keySet()) {
            int numVotes = candidateGivenVotes.get(candidateName);
            output += numVotes + " Vote(s) distributed to Candidate " + candidateName + "\n";
        }
        writeString(output);
    }
    
    /**
     * Write the quota for getting a seat
     * @param quota quota for getting a vote
     */
    public void writeQuota(Integer quota) {
        writeString("Quota for a seat is " + 3 + "\n");
    }

    /**
     * Writes to the audit file the initial votes for each party in a CPL election.
     * @param parties An ArrayList of CPLParty objects representing the parties in the election.
     */
    public void writeCPLInitialVotes(ArrayList<CPLParty> parties) {
        String output = "";
        for (CPLParty party : parties) {
            output += party.getName() + " party has " + party.getBallots() + " vote(s)\n";
        }
        writeString(output);
    }


    /**
     * writes the first allocation of seats. 
     * @param parties in the election
     * @param overloadedSeats number of seats that could not be distributed
     * @param remainingSeats number of undistributed steats
     * @param seatsDistributed number of seats distributed in this allocation
     */
    public void writeCPLInitialAlloc(ArrayList<CPLParty> parties, int overloadedSeats, int remainingSeats, int seatsDistributed) {
        String output = "";
        for (CPLParty party : parties) {
            output += party.getName() + " party has " + party.getNumSeats() + " seat(s), " + party.getBallots() + " vote(s) remaining, " + party.getNumCandidates() + " candidate(s) remaining\n";
        }
        output += seatsDistributed + " seat(s) has been distributed, there are " + remainingSeats + " seat(s) left\n";
        writeString(output);
    }
    
    /**
     * Writes the final seat standings of the parties in CPL
     * @param parties in the election
     */
    public void writeCPLFinalStandings(ArrayList<CPLParty> parties) {
        String output = "\nFinal Seats:\n";
        for (CPLParty party : parties) {
            output += party.getName() + " has " + party.getNumSeats() + " seat(s).\n";
        }
        writeString(output);
    }
    
    /**
     * Writes the results table to the audit file
     * @param candidates the candidates to include
     * @param exhaustedList the collection of exhausted votes for the election
     */
    public void writeResultsTable(ArrayList<IRCandidate> candidates, ArrayList<Integer> exhaustedList) {
        int width = candidates.get(0).getRoundVotes().length;
        System.out.println();
        System.out.printf("%-40s", "Candidates");
        for (int i = 1; i < width; i++) {
            System.out.printf("%-20s", ("Round " + i)); 
        }
        System.out.println();
        System.out.printf("%-20s%-20s", "Candidates", "Party");
        for (int i = 1; i < width; i++) {
            System.out.printf("%-10s", "Votes");
            System.out.printf("%-10s", "+/-"); 
        }
        System.out.println();

        for (IRCandidate cand : candidates) {
            String row[] = cand.getTableRow();
            System.out.printf("%-20s%-20s", row[0], row[1]);
            for (int i = 1; i < width; i++) {
                System.out.printf("%-10s%-10s", row[2*i], row[2*i + 1]);
            }
            System.out.println();
        }
        String totals = "" + exhaustedList.get(0); //extracts first index which must be the totals
        exhaustedList.set(0, 0);
        System.out.printf("%-20s", "EXHAUSTED PILE");
        System.out.printf("%-20s", "");

        for (int i = 1; i < exhaustedList.size(); i++) {
           System.out.printf("%-10s%-10s", exhaustedList.get(i), (exhaustedList.get(i)-exhaustedList.get(i-1))); 
        }
        System.out.println();
       
        System.out.printf("%-40s%-10s%-10s", "Totals", totals, "+" + totals);
        System.out.println();
    }
    


    /**
     * 
     * @return name of audit file
     */
    public String getFileName() {
        return fileName;
    }

}