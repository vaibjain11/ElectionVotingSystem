import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;

/**
* The Election Scanner class extends the functionality of Java's
* built in scanner, specifically adding functions for reading from
* the ballot file
*
* @author  Patrick Hunner
* @author  Ian Paterson
* @version 1.0
* @since   2023-3-20
*/
public class ElectionScanner {
    private Scanner scanner;
    private int numContenders;
    
    /**
     * Creates the election scanner instance with the specified file name
     * @param filename the file name to be imported
     * @throws FileNotFoundException if the file does not exist
     */
    ElectionScanner(File filename) throws FileNotFoundException {
        scanner = new Scanner(filename);
        scanner.useDelimiter(",");
    }
    
    
    /**
     * returns the current date for election purposes
     * @return the current date
     */
    public String getDateString() {
        LocalDateTime date = LocalDateTime.now();
        return date.toString();
    }

    /**
     * returns number of ballots in IR when there are multiple ballot files.
     * @return number of ballots in file.
     */
    public int readIRExtraHeaders() {
        scanner.nextLine();
        numContenders = Integer.parseInt(scanner.nextLine());
        scanner.nextLine();
        return Integer.parseInt(scanner.nextLine());
    }
    
    /**
     * returns number of ballots in CPL when there are multiple ballot files.
     * @return number of ballots in file.
     */
    public int readCPLExtraHeaders() {
        scanner.nextLine();
        numContenders = Integer.parseInt(scanner.nextLine());
        for (int i = 0; i < 2 + numContenders; i++) {
            System.out.println(scanner.nextLine());
        }
        return Integer.parseInt(scanner.nextLine());
    }

    /**
     * returns number of ballots in PO when there are multiple ballot files.
     * @return number of ballots in file.
     */
    public int readPOExtraHeaders() {
        scanner.nextLine();
        numContenders = Integer.parseInt(scanner.nextLine());
        scanner.nextLine();
        return Integer.parseInt(scanner.nextLine());
    }

    /**
     * gets the type of the election from the file.
     * must be run at the first line of the file
     * @return the election type
     */
    public String getElectionType() {
        return scanner.nextLine();
    } 
    
    /**
     * gets the number of contenders from the file
     * must be run at the first line of the file
     * @return 
     */
    public int getNumContenders() {
        numContenders = Integer.parseInt(scanner.nextLine());
        return numContenders;
    }
    
    /**
     * gets the number of ballots from the file
     * @return integer number of ballots
     */
    public int getNumBallots() {
        return Integer.parseInt(scanner.nextLine());
    }
    
    /**
     * gets the contenders and puts them in a string list.
     * @return an Arraylist of Strings including each of the contenders
     */
    public ArrayList<String> getContenders() {
        String line = scanner.nextLine();
        line = line.replaceAll("\\s", "");
        ArrayList<String> contenders = new ArrayList<String>(Arrays.asList(line.strip().split(",")));
        return contenders;
    }

    /**
     * reads the candidates into a list of from the ballot file
     * @return an ArrayList of Candidate names as Strings
     */
    public ArrayList<String> getPOCandidates() {
        String line = scanner.nextLine();
        // makes a list of contenders such as "Pike, D" from this string: [Pike, D], [Foster, D], [Deutsch, R], [Borg, R], [Jones, R], [Smith, I]
        ArrayList<String> contenders = new ArrayList<String>(Arrays.asList(line.strip().split("], \\[")));
        // removes the brackets from the first and last contenders
        contenders.set(0, contenders.get(0).substring(1));
        int lastContenderInd = contenders.size() - 1;
        int lastContenderLength = contenders.get(lastContenderInd).length();
        // removes the last bracket from the last contender
        contenders.set(lastContenderInd, contenders.get(lastContenderInd).substring(0, lastContenderLength - 1));
        return contenders;
    }
    
    /**
     * Tallies the candidates for the IR Election type
     * @param candidate the list of candidate objects
     * @param unaccounted ???
     */
    public void tallyIRBallots(ArrayList<IRCandidate> candidate) { 
        IRCandidate curCandidate;
        while(scanner.hasNextLine()) {
            ArrayList<Integer> ballots = getNextIRBallot(numContenders);
            Ballot tempBal = new Ballot(ballots);
            Integer nextRank = tempBal.popRanking();

            if (nextRank == null) { // ballot has no candidates selected
                continue;
            }
            else {
                curCandidate = candidate.get(nextRank - 1);
                curCandidate.pushVote(tempBal);
            }
        }
        
    }
    

    /**
     * creates a ballot object from the ballot file for IR elections
     * @param numContenders the numbwer of contenders to be added
     * @return the generated ballot
     */
    public ArrayList<Integer> getNextIRBallot(int numContenders) {     
        int index = 1;
        ArrayList<Integer> rankings = new ArrayList<Integer>();
        // for (int i = 0; i < numContenders; i++) {
        //     rankings.add(0);
        // }
        String ballot = scanner.nextLine();
        for (String votes : ballot.split("[,]", 0)) {
            if (!votes.equals("")) {
                while (rankings.size() < Integer.parseInt(votes)) {
                    rankings.add(0);
                }
                rankings.set(Integer.parseInt(votes) - 1, index);
                index++;
            }
            else {
                index++;
            }
        }
        return rankings;
    }
    
    /**
     * tallies the ballots for CPL elections. adds their votes to the
     * corresponding party
     */
    public void tallyCPLBallots(ArrayList<CPLParty> partyList) {
        while(scanner.hasNextLine()) { // checks if end of file
            int index = 0; // index for scanning csv choices
            String ballot = scanner.nextLine();
            for (String votes : ballot.split("[,]", 0)) {
                if (!votes.equals("")) {
                    partyList.get(index).giveVote();
                    index++;
                }
                else {
                    index++;
                }
            }
        }
    }

    /**
     * tallies the ballots for PO elections. adds their votes to the
     * corresponding candidate
     */
    public void tallyPOBallots(ArrayList<POCandidate> candidates) {
        while(scanner.hasNextLine()) {
            int index = 0;
            String ballot = scanner.nextLine();
            for (String votes : ballot.split("[,]", 0)) {
                if (!votes.equals("")) {
                    candidates.get(index).giveVote();
                    index++;
                }
                else {
                    index++;
                }
            }
        }
    }
    
    /**
     * gets the list of candidates and adds them to their corresponding party
     * @param partyList the list of parties to add to
     */
    public void getCPLCandidates(ArrayList<CPLParty> partyList) {
        for(int i = 0; i < partyList.size(); i++) {
            String candidates = scanner.nextLine();
            for (String candidate : candidates.split("[,]", 0)) {
                partyList.get(i).addCandidate(new CPLCandidate(candidate)); //add the candidate to the correct party
            }
        }
    }
    
    /**
     * gets the number of seats to be distributed in a CPL election.
     * @return the number of seats
     */
    public int getSeats() {
        return Integer.valueOf(scanner.nextLine());
    }
}