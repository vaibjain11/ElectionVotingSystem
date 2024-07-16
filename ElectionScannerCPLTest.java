
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.theories.suppliers.TestedOn;
public class ElectionScannerCPLTest {
    ElectionScanner scanner;
    ArrayList<String> contenderList;
    ArrayList<CPLParty> partyList1; //Fabricated party
    ArrayList<CPLParty> partyList2; //Test party

    @Before
    public void instantiate() throws FileNotFoundException {
        String location = System.getProperty("user.dir") + "/testing/ExampleBallotFiles/CPLScanner.csv";
        File file = new File(location);
        scanner = new ElectionScanner(file);
    }

    
    @Test public void CPLElectionScannerTest() {
        getElectionTypeTest();
        getNumContendersTest();
        getContendersTest();
        getCPLCandidatesTest();
        getSeatsTest();
        getNumBallotsTest();
        tallyCPLBallotsTest();
    }

    /*Helper functions to enable sequential reading of data from the CPL file*/
    public void getElectionTypeTest() {
        assertEquals(scanner.getElectionType(), "CPL");
    }

    
    public void getNumContendersTest() {
        assertEquals(scanner.getNumContenders(), 6);
    }

    
    public void getContendersTest() {
        contenderList = new ArrayList<String>();
        contenderList.add("Democratic");
        contenderList.add("Republican");
        contenderList.add("NewWave");
        contenderList.add("Reform");
        contenderList.add("Green");
        contenderList.add("Independent");
        assertEquals(scanner.getContenders(), contenderList);
    }

    public void getCPLCandidatesTest() {
        partyList1 = new ArrayList<CPLParty>();
        partyList2 = new ArrayList<CPLParty>();
        for (String name: contenderList) {
            partyList1.add(new CPLParty(name));
        }
        partyList1.get(0).addCandidate(new CPLCandidate("Foster"));
        partyList1.get(1).addCandidate(new CPLCandidate("Green"));
        partyList1.get(2).addCandidate(new CPLCandidate("Jacks"));
        partyList1.get(2).addCandidate(new CPLCandidate(" Rosen"));
        partyList1.get(3).addCandidate(new CPLCandidate("McClure"));
        partyList1.get(3).addCandidate(new CPLCandidate(" Berg"));
        partyList1.get(4).addCandidate(new CPLCandidate("Zheng"));
        partyList1.get(5).addCandidate(new CPLCandidate("Peters"));
        for (String name: contenderList) {
            partyList2.add(new CPLParty(name));
        }
        scanner.getCPLCandidates(partyList2);
        assertTrue(partyListEquals(partyList1, partyList2));
    }
    
    public void getSeatsTest(){
        assertEquals(scanner.getSeats(), 3);
    }

    
    public void getNumBallotsTest() {
        assertEquals(scanner.getNumBallots(), 6);
    }

    public void tallyCPLBallotsTest() {
        partyList1.get(5).giveVote(); // gives a vote to the party chosen
        partyList1.get(5).giveVote(); 
        partyList1.get(1).giveVote(); 
        partyList1.get(4).giveVote(); 
        partyList1.get(5).giveVote(); 
        partyList1.get(5).giveVote();
        scanner.tallyCPLBallots(partyList2);
        assertTrue(partyListEquals(partyList1, partyList2));
    }

    public boolean partyListEquals(ArrayList<CPLParty> partyList1, ArrayList<CPLParty> partyList2) {
        //if (partyList1.size() != partyList2.size()) return false;
        for(int i = 0; i < partyList1.size(); i++) {
            CPLParty TempParty1 = partyList1.get(i);
            CPLParty TempParty2 = partyList2.get(i);
            //if (TempParty1.getBallots() != TempParty2.getBallots()) return false;
            if (!TempParty1.containsSameCandidates(TempParty2)) return false;
        }
        return true;
    }
}
