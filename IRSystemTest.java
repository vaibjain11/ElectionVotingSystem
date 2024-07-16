
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Array;
import java.util.ArrayList;

public class IRSystemTest {
    String path = System.getProperty("user.dir");
    ElectionScanner scanner;
    IRElection election;

    @Test
    public void MultipleFiles() throws FileNotFoundException {
        File file = new File(path + "/testing/ExampleBallotFiles/IRMultiple1.csv");
        File file2 = new File(path + "/testing/ExampleBallotFiles/IRMultiple2.csv");
        ArrayList<File> files = new ArrayList<File>();
        files.add(file);
        files.add(file2);
        scanner = new ElectionScanner(file);
        scanner.getElectionType();
        ArrayList<ElectionScanner> scanners = new ArrayList<ElectionScanner>();
        scanners.add(scanner);
        scanners.add(new ElectionScanner(file2));
        election = new IRElection(scanners);
        election.randomCoinToss = false;
        assertEquals("Patrick is the winner with a final 4 votes out of 14", election.runIR());
    }

    @Test
    public void Majority() throws FileNotFoundException {
        File file = new File(path + "/testing/ExampleBallotFiles/IRMajority.csv");
        scanner = new ElectionScanner(file);
        scanner.getElectionType();
        ArrayList<ElectionScanner> scanners = new ArrayList<ElectionScanner>();
        scanners.add(scanner);
        election = new IRElection(scanners);
        assertEquals("Rosen(D) is the winner with a final 2 votes out of 3", election.runIR());
    }

    @Test
    public void OneCandidate() throws FileNotFoundException {
        File file = new File(path + "/testing/ExampleBallotFiles/IROneCandidate.csv");
        scanner = new ElectionScanner(file);
        scanner.getElectionType();
        ArrayList<ElectionScanner> scanners = new ArrayList<ElectionScanner>();
        scanners.add(scanner);
        election = new IRElection(scanners);
        assertEquals("Rosen(D) is the winner with a final 0 votes out of 2", election.runIR());
    }

    @Test
    public void OneCandidateNoVotes() throws FileNotFoundException {
        File file = new File(path + "/testing/ExampleBallotFiles/IROneCandidateNoVotes.csv");
        scanner = new ElectionScanner(file);
        scanner.getElectionType();
        ArrayList<ElectionScanner> scanners = new ArrayList<ElectionScanner>();
        scanners.add(scanner);
        election = new IRElection(scanners);
        election.randomCoinToss = false;
        assertEquals("Rosen(D) is the winner with a final 0 votes out of 0", election.runIR());
    }

    @Test
    public void Popularity() throws FileNotFoundException {
        File file = new File(path + "/testing/ExampleBallotFiles/IRPopularity.csv");
        scanner = new ElectionScanner(file);
        scanner.getElectionType();
        ArrayList<ElectionScanner> scanners = new ArrayList<ElectionScanner>();
        scanners.add(scanner);
        election = new IRElection(scanners);
        election.randomCoinToss = false;
        assertEquals("Kleinberg(R) is the winner with a final 0 votes out of 1", election.runIR());
    }

    @Test
    public void TwoCandidatesNoVotes() throws FileNotFoundException {
        File file = new File(path + "/testing/ExampleBallotFiles/IRTwoCandidatesNoVotes.csv");
        scanner = new ElectionScanner(file);
        scanner.getElectionType();
        ArrayList<ElectionScanner> scanners = new ArrayList<ElectionScanner>();
        scanners.add(scanner);
        election = new IRElection(scanners);
        election.randomCoinToss = false;
        assertEquals("Rosen(D) is the winner with a final 0 votes out of 0", election.runIR());
    }

    @Test
    public void UnfilledVotes() throws FileNotFoundException {
        File file = new File(path + "/testing/ExampleBallotFiles/IRUnfilledVotes.csv");
        scanner = new ElectionScanner(file);
        scanner.getElectionType();
        ArrayList<ElectionScanner> scanners = new ArrayList<ElectionScanner>();
        scanners.add(scanner);
        election = new IRElection(scanners);
        election.randomCoinToss = false;
        assertEquals("Kleinberg(R) is the winner with a final 0 votes out of 1", election.runIR());
    }

    @Test
    public void MultipleRedistributions() throws FileNotFoundException {
        File file = new File(path + "/testing/ExampleBallotFiles/IRManyRedistributions.csv");
        scanner = new ElectionScanner(file);
        scanner.getElectionType();
        ArrayList<ElectionScanner> scanners = new ArrayList<ElectionScanner>();
        scanners.add(scanner);
        election = new IRElection(scanners);
        election.randomCoinToss = false;
        assertEquals("Cole is the winner with a final 4 votes out of 8", election.runIR());
    }

    @Test
    public void testloggingRoundVotes() throws FileNotFoundException {
        File file = new File(path + "testing/ExampleBallotFiles/IRManyRedistributions.csv");
        scanner = new ElectionScanner(file);
        scanner.getElectionType();
        ArrayList<ElectionScanner> scanners = new ArrayList<ElectionScanner>();
        scanners.add(scanner);
        election = new IRElection(scanners);
        election.randomCoinToss = false;
        election.runIR();
        assertEquals(2, (int) election.getCandidateVotes(0)[1]);
        assertEquals(2, (int) election.getCandidateVotes(0)[1]);
        assertEquals(2, (int) election.getCandidateVotes(0)[1]);
        assertEquals(2, (int) election.getCandidateVotes(1)[1]);
        assertEquals(4, (int) election.getCandidateVotes(1)[2]);
        assertEquals(4, (int) election.getCandidateVotes(1)[3]);
        assertEquals(2, (int) election.getCandidateVotes(2)[1]);
        assertEquals(2, (int) election.getCandidateVotes(2)[2]);
        assertEquals(0, (int) election.getCandidateVotes(2)[3]);
    }
    

}