
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

/*
 * Testing Cases:
 * different sized remainders
 * no remainder
 * overload tie
 * overload no tie
 * remainder tie
 */

public class CPLSystemTest {
    String path = System.getProperty("user.dir");
    ElectionScanner scanner;
    CPLElection election;

    @Test
    public void CompareRemainders() throws FileNotFoundException {
        File file = new File(path + "/testing/ExampleBallotFiles/CPLCompareRemainders.csv");
        scanner = new ElectionScanner(file);
        scanner.getElectionType();
        ArrayList<ElectionScanner> scanners = new ArrayList<ElectionScanner>();
        scanners.add(scanner);
        election = new CPLElection(scanners);
        assertEquals("The candidate(s) receiving seats are Foster, Wallace from the Democratic party.  Green from the Republican party. ", election.runCPL());
    }

    @Test
    public void NoRemainder() throws FileNotFoundException {
        File file = new File(path + "/testing/ExampleBallotFiles/CPLNoRemainder.csv");
        scanner = new ElectionScanner(file);
        scanner.getElectionType();
        ArrayList<ElectionScanner> scanners = new ArrayList<ElectionScanner>();
        scanners.add(scanner);
        election = new CPLElection(scanners);
        assertEquals("The candidate(s) receiving seats are Kanye from the Democratic party.  Bernie from the Republican party. ", election.runCPL());
    }

    @Test
    public void OverloadTie() throws FileNotFoundException {
        File file = new File(path + "/testing/ExampleBallotFiles/CPLOverloadTie.csv");
        scanner = new ElectionScanner(file);
        scanner.getElectionType();
        ArrayList<ElectionScanner> scanners = new ArrayList<ElectionScanner>();
        scanners.add(scanner);
        election = new CPLElection(scanners);
        election.randomCoinToss = false;
        assertEquals("The candidate(s) receiving seats are Bernie from the Democratic party.  Kanye from the Republican party. ", election.runCPL());
    }

    @Test
    public void Overload() throws FileNotFoundException {
        File file = new File(path + "/testing/ExampleBallotFiles/CPLOverload.csv");
        scanner = new ElectionScanner(file);
        scanner.getElectionType();
        ArrayList<ElectionScanner> scanners = new ArrayList<ElectionScanner>();
        scanners.add(scanner);
        election = new CPLElection(scanners);
        assertEquals("The candidate(s) receiving seats are Bernie from the Democratic party.  Kanye from the Republican party. ", election.runCPL());
    }

    @Test
    public void RemainderTie() throws FileNotFoundException {
        File file = new File(path + "/testing/ExampleBallotFiles/CPLRemainderTie.csv");
        scanner = new ElectionScanner(file);
        scanner.getElectionType();
        ArrayList<ElectionScanner> scanners = new ArrayList<ElectionScanner>();
        scanners.add(scanner);
        election = new CPLElection(scanners);
        election.randomCoinToss = false;
        assertEquals("The candidate(s) receiving seats are Bernie, Joe from the Democratic party.  Kanye from the Republican party. ", election.runCPL());
    }
}