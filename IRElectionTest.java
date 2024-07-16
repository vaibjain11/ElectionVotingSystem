
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.io.File;
import java.io.FileNotFoundException;

public class IRElectionTest {
    String path = System.getProperty("user.dir");
    IRElection election;
    ElectionScanner scanner;
    ArrayList<IRCandidate> testCandidates = new ArrayList<IRCandidate>();
    ArrayList<IRCandidate> candidates = new ArrayList<IRCandidate>();
    Ballot ballot1;
    Ballot ballot2;
    Ballot ballot3;

    @Test
    public void createdCandidates() throws FileNotFoundException {
        String location = path + "/testing/ExampleBallotFiles/IRScanner.csv";
        File file = new File(location);
        scanner = new ElectionScanner(file);
        ArrayList<ElectionScanner> scanners = new ArrayList<ElectionScanner>();
        scanners.add(scanner);
        election = new IRElection(scanners);
        scanner.getElectionType();
        election.createCandidates();
        assertEquals(4, election.getNumCandidates());
        ArrayList<IRCandidate> testCandidates = new ArrayList<IRCandidate>();
        testCandidates.add(new IRCandidate("Patrick"));
        testCandidates.add(new IRCandidate("Cole"));
        testCandidates.add(new IRCandidate("Ian"));
        testCandidates.add(new IRCandidate("VJ"));
        assertTrue(equalCandidateList(testCandidates, election.getCandidates()));
    }

    public boolean equalCandidateList(ArrayList<IRCandidate> list1, ArrayList<IRCandidate> list2) {
        if (list1.size() != list2.size()) {
            return false;
        }
        for (int i = 0; i < list1.size(); i++) {
            if (!list1.get(i).getName().equals(list2.get(i).getName())) {
                return false;
            }
        }
        return true;
    }

    @Test
    public void testVoteRemoval() throws FileNotFoundException {
        candidates.add(new IRCandidate("Ian"));
        election.setCandidates(candidates);
        election.setNumCandidates(3);
        election.setNumTotalBallots(2);
        ballot1.popRanking();
        ballot2.popRanking();
        candidates.get(2).pushVote(ballot1);
        candidates.get(1).pushVote(ballot2);
        election.invalidateBallots();
        assertEquals(0, (int)candidates.get(1).getBallots());
        assertEquals(0, (int)candidates.get(0).getBallots());
        assertEquals(1, (int)candidates.get(2).getBallots());
        assertEquals(1, (int)election.getNumTotalBallots());
    }


    @Before
    public void initialize() throws FileNotFoundException {
        File file = new File(path + "/testing/ExampleBallotFiles/IRMajority.csv");
        scanner = new ElectionScanner(file);
        ArrayList<ElectionScanner> scanners = new ArrayList<ElectionScanner>();
        scanners.add(scanner);
        election = new IRElection(scanners);
        election.setNumCandidates(2);
        IRCandidate cand1 = new IRCandidate("Patrick");
        IRCandidate cand2 = new IRCandidate("VJ");
        candidates.add(cand1);
        candidates.add(cand2);
        election.setCandidates(candidates);
        ArrayList<Integer> array1 = new ArrayList<Integer>(Arrays.asList(2, 1, 0));
        ArrayList<Integer> array2 = new ArrayList<Integer>(Arrays.asList(1));
        ballot1 = new Ballot(array1);
        ballot2 = new Ballot(array2);
        ballot3 = new Ballot(array2);
    }

    @Test
    public void noMajority() {
        election.numTotalBallots = 2;
        candidates.get(0).pushVote(ballot1);
        candidates.get(1).pushVote(ballot2);
        assertTrue(election.checkMajority() == null);
    }

    @Test
    public void elimNoCoinToss() {
        election.numTotalBallots = 2;
        candidates.get(0).pushVote(ballot1);
        candidates.get(1).pushVote(ballot2);
        candidates.get(1).pushVote(ballot2);
        assertTrue(election.determineElim() == candidates.get(0));
    }

    @Test
    public void hasMajority() {
        election.numTotalBallots = 3;
        candidates.get(0).pushVote(ballot1);
        candidates.get(1).pushVote(ballot2);
        candidates.get(0).pushVote(ballot1);
        assertTrue(election.checkMajority() == candidates.get(0));
    }

    @Test
    public void singlePopularity() {
        election.numTotalBallots = 10;
        for (int i = 0; i < 4; i++) {
            candidates.get(0).pushVote(ballot1);
        }
        for (int i = 0; i < 3; i++) {
            candidates.get(1).pushVote(ballot2);
        }
        assertTrue(election.determinePopularity() == candidates.get(0));
    }

    @Test
    public void redistributeNoTie() {
        election.numTotalBallots = 3;
        candidates.get(0).pushVote(ballot1);
        candidates.get(1).pushVote(ballot2);
        candidates.get(1).pushVote(ballot3);
        System.out.println(candidates.size());

        election.redistribute(candidates.get(0));
        assertTrue(candidates.get(0).isEliminated() == true);
        assertTrue(candidates.get(1).getBallotList().size() == 3);
    }

    @Test
    public void testNumActive() {
        assertTrue(election.numActive() == 2);
        candidates.get(0).setEliminated();
        assertTrue(election.numActive() == 1);
        candidates.get(1).setEliminated();
        assertTrue(election.numActive() == 0);
        candidates.clear();
        assertTrue(election.numActive() == 0);
    }   
}