
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
* Tests the ElectionMain Class
*
* @author  Patrick Hunner
* @version 1.4
* @since   2023-3-23
*/
public class ElectionMainTest {
    
    @Test
    public void invalidFilePrompt() {
        ByteArrayInputStream in = new ByteArrayInputStream("invalid\ninvalid\ninvalid\ninvalid\ninvalid\ndone".getBytes());
        System.setIn(in);
        assertEquals(ElectionMain.promptFileName(), null);
    }

    @Test
    public void validFilePrompt() {
        String location = System.getProperty("user.dir") + "/testing/ExampleBallotFiles/IRMajority.csv\ndone";
        ByteArrayInputStream in = new ByteArrayInputStream(location.getBytes());
        System.setIn(in);
        assertTrue(ElectionMain.promptFileName().getClass() == ArrayList.class);
    }

    @Test
    public void multipleFiles() {
        String location;
        String location1 = System.getProperty("user.dir") + "/testing/ExampleBallotFiles/IRNoVotes.csv" + System.lineSeparator();
        String location2 = System.getProperty("user.dir") + "/testing/ExampleBallotFiles/IRUnfilledVotes.csv" + System.lineSeparator();
        String location3 = System.getProperty("user.dir") + "/testing/ExampleBallotFiles/doesntexist.csv" + System.lineSeparator() + "done";
        location = location1 + location1 + location2 + location3;
        ByteArrayInputStream in = new ByteArrayInputStream(location.getBytes());
        System.setIn(in);
        ArrayList<File> files = new ArrayList<File>();
        files.add(new File(location1.trim()));
        files.add(new File(location2.trim()));

        // Redirect the standard output to a ByteArrayOutputStream
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream stdout = System.out;
        System.setOut(new PrintStream(out));

        // Call promptFileName() and check the output
        ArrayList<File> files2 = ElectionMain.promptFileName();
        try {
            assertEquals("Please input a file name ('done' when you have entered all files): Please input a file name ('done' when you have entered all files): '/home/hunne007/csci5801/repo-Team6/Project2/testing/ExampleBallotFiles/IRNoVotes.csv' was already added.\nPlease input a file name ('done' when you have entered all files): Please input a file name ('done' when you have entered all files): '/home/hunne007/csci5801/repo-Team6/Project2/testing/ExampleBallotFiles/doesntexist.csv' was not found, check spelling and location.\nPlease input a file name ('done' when you have entered all files): " + System.lineSeparator(), out.toString(StandardCharsets.UTF_8.name()) + "\n");
        } catch (UnsupportedEncodingException e) {
            fail("UnsupportedEncodingException occurred");
        }

        // Restore the standard output
        System.setOut(stdout);

        // Assert that the ArrayLists of Files are equal
        assertEquals(files2, files);
    }

    @Test
    public void testStartElection() throws FileNotFoundException {
        String location = System.getProperty("user.dir") + "/testing/ExampleBallotFiles/IRManyRedistributions.csv";
        File file = new File(location);
        ArrayList<File> lst = new ArrayList<File>();
        lst.add(file);
        assertTrue(ElectionMain.startElection(lst).getClass() == String.class);
    }
        
    /**
     * Assert that if a coin was tossed between 5 candidates 100000 times, no candidate gets chosen
     * more than 21% of the time or less than 19% of the time.
     * @throws FileNotFoundException
     */
    @Test
    public void CoinTossRandom() throws FileNotFoundException {
        IRElection election;
        ArrayList<IRCandidate> candidates;
        ElectionScanner scanner;
        Map<IRCandidate, Integer> dict = new HashMap<>();
        String location = System.getProperty("user.dir") + "/testing/ExampleBallotFiles/IRMajority.csv";
        File file = new File(location);
        scanner = new ElectionScanner(file);
        ArrayList<ElectionScanner> scanners = new ArrayList<ElectionScanner>();
        scanners.add(scanner);
        election = new IRElection(scanners);
        candidates = new ArrayList<IRCandidate>();
        candidates.add(new IRCandidate("Patrick"));
        candidates.add(new IRCandidate("Cole"));
        candidates.add(new IRCandidate("Vaibhav"));
        candidates.add(new IRCandidate("Ian"));
        candidates.add(new IRCandidate("Shanna"));
        for (int i = 0; i < candidates.size(); i++) {
            dict.put(candidates.get(i), i);
        }
        IRCandidate chosen;
        for (int i = 0; i < 100000; i++) {
            chosen = election.coinToss(candidates);
            dict.put(chosen, dict.get(chosen) + 1);
        }
        for (int i = 0; i < candidates.size(); i++) {
            assertTrue(dict.get(candidates.get(i)) >= 19000 && dict.get(candidates.get(i)) <= 21000);
        }
    }
}