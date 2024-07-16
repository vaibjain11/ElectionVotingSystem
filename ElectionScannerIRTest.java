import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

public class ElectionScannerIRTest {

    File file;
    ElectionScanner scanner;
    ArrayList<String> contenderList;
    ArrayList<IRCandidate> candidateList; 

    @Before
    public void instantiate() throws FileNotFoundException {
        String location = System.getProperty("user.dir") + "/testing/ExampleBallotFiles/IRScanner.csv";
        file = new File(location);
        scanner = new ElectionScanner(file);
        candidateList = new ArrayList<IRCandidate>();
    }

    @Test 
    public void allScanning() throws FileNotFoundException {
        
        assertEquals(scanner.getElectionType(), "IR");

        assertEquals(scanner.getNumContenders(), 4);

        contenderList = new ArrayList<String>();
        contenderList.add("Patrick");
        contenderList.add("Cole");
        contenderList.add("Ian");
        contenderList.add("VJ");
        assertEquals(scanner.getContenders(), contenderList);

        assertEquals(scanner.getNumBallots(), 7);

        assertEquals(scanner.getNextIRBallot(4), new ArrayList<>());
        assertEquals(scanner.getNextIRBallot(4), new ArrayList<>(Arrays.asList(1)));
        assertEquals(scanner.getNextIRBallot(4), new ArrayList<>(Arrays.asList(4)));
        assertEquals(scanner.getNextIRBallot(4), new ArrayList<>(Arrays.asList(1,2,3,4)));
        assertEquals(scanner.getNextIRBallot(4), new ArrayList<>(Arrays.asList(1,3)));
        assertEquals(scanner.getNextIRBallot(4), new ArrayList<>(Arrays.asList(2,3,4)));
        assertEquals(scanner.getNextIRBallot(4), new ArrayList<>(Arrays.asList(1,4,2,3)));
    }

    @Test
    public void testTallyBallots() throws FileNotFoundException {
        scanner.getElectionType();
        scanner.getNumContenders();
        contenderList = scanner.getContenders();
        for (String name : contenderList) {
            candidateList.add(new IRCandidate(name));
        }
        scanner.getNumBallots();
        scanner.tallyIRBallots(candidateList);
        assertTrue(candidateList.get(0).getBallots() == 4);
        assertTrue(candidateList.get(1).getBallots() == 1);
        assertTrue(candidateList.get(2).getBallots() == 0);
        assertTrue(candidateList.get(3).getBallots() == 1);
    }
}