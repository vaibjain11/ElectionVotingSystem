
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.io.File;
import java.io.FileNotFoundException;

/**
* Tests the POCandidate Class
*
* @author  Vaibhav Jain
* @version 1.0
* @since   2023-04-28
*/

public class POCandidateTest {
    
    POCandidate candidateA;
    POCandidate candidateB;
    
    @Before
    public void instantiate() throws Exception{
        candidateA = new POCandidate("Bob");
        candidateB = new POCandidate("Alice");
        candidateB.giveVote();
        candidateB.giveVote();
    }

    @Test
    public void testgiveVote() {
        candidateA.giveVote();
        assertTrue(candidateA.getBallots() == 1);
        candidateA.giveVote();
        assertTrue(candidateA.getBallots() == 2);
    }

    @Test
    public void testgetBallots() {
        assertTrue(candidateA.getBallots() == 0);
        assertTrue(candidateB.getBallots() == 2);
    }

    @Test
    public void testgetName() {
        assertEquals(candidateA.getName(), "Bob");
        assertEquals(candidateB.getName(), "Alice");
    }
    
}
