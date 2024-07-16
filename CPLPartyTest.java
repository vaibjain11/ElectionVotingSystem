
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;
public class CPLPartyTest {
    private CPLParty testParty;
    @Before
    public void initialize() {
        testParty = new CPLParty("KanyeCore");
    }

    @Test
    public void addCandidateTest() {
        testParty.addCandidate(new CPLCandidate("Kanye"));
        assertEquals(testParty.getCandidateNames(), "Kanye");
    }

    @Test
    public void giveVoteTest() {
        testParty.giveVote();
        assertEquals(testParty.getBallots() + 0, 1);
    }

    @Test
    public void getNextCandidateTest() {
        CPLCandidate kanye = new CPLCandidate("Kanye");
        testParty.addCandidate(kanye);
        assertEquals(kanye, testParty.getNextCandidate());
    }

    @Test
    public void giveSeatTest() {
        CPLCandidate kanye = new CPLCandidate("Kanye");
        CPLCandidate ye = new CPLCandidate("ye");
        testParty.addCandidate(kanye);
        testParty.addCandidate(ye);
        testParty.giveSeat(0);
        testParty.giveSeat(0);
        assertTrue(kanye.hasSeat());
        assertTrue(ye.hasSeat());
        assertFalse(testParty.hasAvailableSeats());
    }

    @Test
    public void compareToTest() {
        CPLCandidate kanye = new CPLCandidate("Kanye");
        CPLCandidate ye = new CPLCandidate("ye");
        CPLParty testParty1 = new CPLParty("Kanye1");
        CPLParty testParty2 = new CPLParty("Kanye2");
        testParty1.addCandidate(kanye);
        testParty2.addCandidate(ye);
        testParty1.giveVote();
        testParty2.giveVote();
        //case they are the same
        assertEquals(testParty1.compareTo(testParty2), 0);
        //case they testParty1 has more votes
        testParty1.giveVote();
        // assertTrue(testParty1.compareTo(testParty2) > 0);
        assertTrue(testParty1.getBallots() > testParty2.getBallots());
        //case testparty2 has more votes
        testParty2.giveVote();
        testParty2.giveVote();
        assertTrue(testParty1.getBallots() < testParty2.getBallots());
    }

    @Test
    public void getCandidateNamesTest() {
        CPLCandidate kanye = new CPLCandidate("Kanye");
        CPLCandidate ye = new CPLCandidate("ye");
        testParty.addCandidate(kanye);
        testParty.addCandidate(ye);
        assertEquals(testParty.getCandidateNames(), "Kanyeye");
    }
}
