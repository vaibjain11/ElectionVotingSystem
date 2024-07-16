import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.io.File;

public class POElectionTest {
    POElection election;
    ArrayList<ElectionScanner> scanners;

    @Before
    public void instantiate() throws Exception{
        String path = System.getProperty("user.dir");
        ElectionScanner scanner = new ElectionScanner(new File(path + "/testing/ExampleBallotFiles/POBasic.csv"));
        scanners = new ArrayList<ElectionScanner>();
        scanners.add(scanner);
        election = new POElection(scanners);
        scanners.get(0).getElectionType();
    }

    @Test
    public void runPOTest() {
        String result = election.runPO();
        ArrayList<POCandidate> candidates = election.getCandidates();
        assertEquals(candidates.get(0).getName(), "Pike, D");
        assertEquals(candidates.get(1).getName(), "Foster, D");
        assertEquals(candidates.get(2).getName(), "Deutsch, R");
        assertEquals(candidates.get(3).getName(), "Borg, R");
        assertEquals(candidates.get(4).getName(), "Jones, R");
        assertEquals(candidates.get(5).getName(), "Smith, I");
        assertEquals(result, "Pike, D won with 3 votes.");
    }
    
    @Test
    public void createCandidatesTest() {
        election.createCandidates();
        ArrayList<POCandidate> candidates = election.getCandidates();
        assertEquals(candidates.get(0).getName(), "Pike, D");
        assertEquals(candidates.get(1).getName(), "Foster, D");
        assertEquals(candidates.get(2).getName(), "Deutsch, R");
        assertEquals(candidates.get(3).getName(), "Borg, R");
        assertEquals(candidates.get(4).getName(), "Jones, R");
        assertEquals(candidates.get(5).getName(), "Smith, I");
    }

    @Test
    public void tallyVotesTest() {
        election.createCandidates();
        election.tallyVotes();
        ArrayList<POCandidate> candidates = election.getCandidates();
        assertTrue(candidates.get(0).getBallots() == 4);
        assertTrue(candidates.get(1).getBallots() == 2);
        assertTrue(candidates.get(2).getBallots() == 0);
        assertTrue(candidates.get(3).getBallots() == 2);
        assertTrue(candidates.get(4).getBallots() == 1);
        assertTrue(candidates.get(5).getBallots() == 1);
    }

    @Test
    public void findMostVotesTest() {
        election.createCandidates();
        election.setNumTotalBallots(scanners.get(0).getNumBallots());
        election.tallyVotes();
        ArrayList<POCandidate> mostVotes = election.findMostVotes();
        assertEquals(mostVotes.get(0).getName(), "Pike, D");
        assertTrue(mostVotes.size() == 1);
    }
}
