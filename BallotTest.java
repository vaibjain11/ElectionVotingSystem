
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

import java.util.ArrayList;
import java.util.EmptyStackException;

/**
* Tests the Ballot Class
*
* @author  Vaibhav Jain
* @version 1.1
* @since   2023-3-22
*/
public class BallotTest {
    @Test
    public void testPopRanking() {
        ArrayList<Integer> ranks = new ArrayList<Integer>();
        ranks.add(1);
        ranks.add(2);
        Ballot t_ballot = new Ballot(ranks);
        assertEquals(t_ballot.popRanking(), Integer.valueOf(1));
        t_ballot.popRanking();
        assertEquals(t_ballot.popRanking(), null);
    }  
    @Test
    public void testInvalidation() {
        ArrayList<Integer> ranks = new ArrayList<Integer>();
        ranks.add(1);
        ranks.add(2);
        //check intial state
        Ballot t_ballot = new Ballot(ranks);
        assertEquals(true, t_ballot.isValid());
        //check invalidation
        t_ballot.invalidate();
        assertEquals(false, t_ballot.isValid());
    }  
}