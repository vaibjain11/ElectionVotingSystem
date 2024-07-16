
import static org.junit.Assert.assertEquals;


import org.junit.Before;
import org.junit.Test;

/**
* Tests the CPLCandidate Class
*
* @author  Ian Paterson
* @version 1.0
* @since   2023-3-21
*/
public class CPLCandidateTest {
    
    private CPLCandidate candidate;
    
    @Before
    public void instantiate() throws Exception{
        candidate = new CPLCandidate("Bob");
    }

    @Test
    public void testhasSeat() {
        assertEquals(candidate.hasSeat(), false);
        candidate.setHasSeat(true);
        assertEquals(candidate.hasSeat(), true);
    }

    @Test
    public void testgetName() {
        assertEquals(candidate.getName(), "Bob");
    }
}
