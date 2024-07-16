
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

/**
* Tests the IRCandidate Class
*
* @author  Vaibhav Jain
* @version 1.1
* @since   2023-3-22
*/

public class IRCandidateTest {
    IRCandidate candidate;
    IRCandidate candidate2;

    @Before
    public void instantiate() throws Exception{
        candidate = new IRCandidate("Joe");
        candidate.setEliminated();
        candidate2 = new IRCandidate("Mark");
    }

    @Test
    public void testIsEliminated() {
        assertTrue(candidate.isEliminated());
        assertFalse(candidate2.isEliminated());
    }
    
    @Test
    public void testPopVote() {
        ArrayList<Integer> ranks = new ArrayList<Integer>();
        ranks.add(1);
        Ballot t_ballot = new Ballot(ranks);
        candidate.getBallotList().push(t_ballot);
        ArrayList<Integer> ranks2 = new ArrayList<Integer>();
        ranks2.add(8);
        Ballot t_ballot2 = new Ballot(ranks2);
        candidate.getBallotList().push(t_ballot2);
        assertEquals(candidate.popVote(), t_ballot2);
        assertEquals(candidate.popVote(), t_ballot);
    }    

    @Test
    public void testPushVote() {
        ArrayList<Integer> ranks = new ArrayList<Integer>(5);
        ranks.add(1);
        ranks.add(6);
        ranks.add(2);
        Ballot t_ballot = new Ballot(ranks);
        candidate.pushVote(t_ballot);
        ArrayList<Integer> ranks2 = new ArrayList<Integer>(5);
        ranks2.add(8);
        ranks2.add(4);
        ranks2.add(3);
        Ballot t_ballot2 = new Ballot(ranks2);
        candidate.pushVote(t_ballot2);
        assertEquals(candidate.getBallotList().peek(), t_ballot2);
        candidate.getBallotList().pop();
        assertEquals(candidate.getBallotList().peek(), t_ballot);
    }   

    @Test
    public void testGetName() {
        assertEquals("Joe", candidate.getName());
        assertEquals("Mark", candidate2.getName());
    }

    @Test
    public void testGetBallots() {
        assertTrue(candidate.getBallots() == 0);
        ArrayList<Integer> ranks = new ArrayList<>();
        ranks.add(1);
        candidate.pushVote(new Ballot(ranks));
        assertEquals(Integer.valueOf(1), candidate.getBallots());
    }

    public void testStoreRowInfo() {
        ArrayList<Integer> rankings = new ArrayList<Integer>();
        rankings.add(1);
        Ballot ballot1 = new Ballot(rankings);
        Ballot ballot2 = new Ballot(rankings);
        Ballot ballot3 = new Ballot(rankings);
        candidate2.logRoundVotes(0);
        candidate2.pushVote(ballot1);
        candidate2.pushVote(ballot3);        
        candidate2.logRoundVotes(1);
        candidate2.pushVote(ballot3);
        candidate2.logRoundVotes(2);
        candidate2.popVote();
        candidate2.popVote();
        candidate2.popVote();
        candidate2.logRoundVotes(3);
        assertEquals("Mark", candidate2.getTableRow()[0]);
        assertEquals("N/A", candidate2.getTableRow()[1]);
        assertEquals("2", candidate2.getTableRow()[2]);
        assertEquals("2", candidate2.getTableRow()[3]);
        assertEquals("3", candidate2.getTableRow()[4]);
        assertEquals("1", candidate2.getTableRow()[5]);
        assertEquals("0", candidate2.getTableRow()[6]);
        assertEquals("-3", candidate2.getTableRow()[7]);
    }
    
    @Test
    public void testStorePartyinRoundInfo() {
        ArrayList<Integer> rankings = new ArrayList<Integer>();
        candidate2 = new IRCandidate("among (sus)");
        rankings.add(1);
        Ballot ballot1 = new Ballot(rankings);
        Ballot ballot2 = new Ballot(rankings);
        Ballot ballot3 = new Ballot(rankings);
        candidate2.logRoundVotes(0);
        candidate2.pushVote(ballot1);
        candidate2.pushVote(ballot3);        
        candidate2.logRoundVotes(1);
        assertEquals("among", candidate2.getTableRow()[0]);
        assertEquals("sus", candidate2.getTableRow()[1]);
    }



}