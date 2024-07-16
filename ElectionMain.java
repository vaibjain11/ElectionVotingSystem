/**
* The main Election class used to run the Election
*
* @author  Patrick Hunner
* @author  Ian Paterson
* @version 1.0
* @since   2023-3-20
*/
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class ElectionMain {

    /**
     * Asks the user for the Ballot File names if they have not been 
     * specified previously in the command line arguments.
     * @return an ArrayList of all the valid ballot files.
     */
    public static ArrayList<File> promptFileName() {
        ArrayList<File> ballotFiles = new ArrayList<File>();
        Scanner scanner = new Scanner(System.in);
        String fname;
        File f;
        while (true) {
            System.out.print("Please input a file name ('done' when you have entered all files): ");
            fname = scanner.nextLine();
            f = new File(fname);
            if (f.exists()) {
                if (ballotFiles.contains(f)) {
                    System.out.println("'" + fname + "'" + " was already added.");
                }
                else {
                    ballotFiles.add(f);
                }
            }
            else if (fname.equals("done")) {
                break;
            }
            else {
                System.out.println("'" + fname + "'" + " was not found, check spelling and location.");
            }
        } 
        scanner.close();
        if (ballotFiles.size() > 0) {
            return ballotFiles;
        }
        return null;
    }

    /**
     * main procedural run line to starting elections
     * @param file the file to read ballots from
     * @return the election type run method
     * @throws FileNotFoundException if the file does not exist
     */
    public static String startElection(ArrayList<File> files) throws FileNotFoundException {
        ArrayList<ElectionScanner> scanners = new ArrayList<ElectionScanner>();
        for (File f : files) {
            scanners.add(new ElectionScanner(f));
        }
        String electionType = scanners.get(0).getElectionType();
        if (electionType.equals("IR")) {
            IRElection election = new IRElection(scanners);
            return election.runIR();
        }
        else if (electionType.equals("PO")) {
            POElection election = new POElection(scanners);
            return election.runPO();
        }
        else {
            CPLElection election = new CPLElection(scanners);
            return election.runCPL();
        }
    }
}