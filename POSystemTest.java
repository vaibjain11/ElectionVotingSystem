import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

public class POSystemTest {
    String path = System.getProperty("user.dir");
    ElectionScanner scanner;
    POElection election;
    ByteArrayOutputStream outContent;

    @Before
    public void instantiate() {
        // outContent = new ByteArrayOutputStream();
        // System.setOut(new PrintStream(outContent));
    }

    @Test
    public void MultipleFiles() throws FileNotFoundException {
        File file = new File(path + "/testing/ExampleBallotFiles/POMultiple1.csv");
        File file2 = new File(path + "/testing/ExampleBallotFiles/POMultiple2.csv");
        ArrayList<File> files = new ArrayList<File>();
        files.add(file);
        files.add(file2);
        scanner = new ElectionScanner(file);
        scanner.getElectionType();
        ArrayList<ElectionScanner> scanners = new ArrayList<ElectionScanner>();
        scanners.add(scanner);
        scanners.add(new ElectionScanner(file2));
        election = new POElection(scanners);
        election.randomCoinToss = false;
        // assertEquals(outContent.toString(), "other");
        assertEquals("Foster, D won with 6 votes.", election.runPO());
    }

    @Test
    public void POTie() throws FileNotFoundException {
        File file = new File(path + "/testing/ExampleBallotFiles/PO2WayTie.csv");
        scanner = new ElectionScanner(file);
        scanner.getElectionType();
        ArrayList<ElectionScanner> scanners = new ArrayList<ElectionScanner>();
        scanners.add(scanner);
        election = new POElection(scanners);
        election.randomCoinToss = false;
        assertEquals("Pike, D won with 3 votes.", election.runPO());
    }

    @Test
    public void POBasic() throws FileNotFoundException {
        File file = new File(path + "/testing/ExampleBallotFiles/POBasic.csv");
        scanner = new ElectionScanner(file);
        scanner.getElectionType();
        ArrayList<ElectionScanner> scanners = new ArrayList<ElectionScanner>();
        scanners.add(scanner);
        election = new POElection(scanners);
        election.randomCoinToss = false;
        assertEquals("Pike, D won with 3 votes.", election.runPO());
    }

    @Test
    public void POOneCandidate() throws FileNotFoundException {
        File file = new File(path + "/testing/ExampleBallotFiles/POOneCandidate.csv");
        scanner = new ElectionScanner(file);
        scanner.getElectionType();
        ArrayList<ElectionScanner> scanners = new ArrayList<ElectionScanner>();
        scanners.add(scanner);
        election = new POElection(scanners);
        election.randomCoinToss = false;
        assertEquals("Pike, D won with 3 votes.", election.runPO());
    }
}
