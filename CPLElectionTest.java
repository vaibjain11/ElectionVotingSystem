
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;

public class CPLElectionTest {
    CPLElection election;
    ElectionScanner scanner;
    @Before
    public void instantiate() throws FileNotFoundException {
        String location = System.getProperty("user.dir") + "/testing/ExampleBallotFiles/CPLScanner.csv";
        File file = new File(location);
        scanner = new ElectionScanner(file);
        ArrayList<ElectionScanner> scanners = new ArrayList<ElectionScanner>();
        scanners.add(scanner);
        election = new CPLElection(scanners);
        scanner.getElectionType();
    }

    @Test
    public void createPartiesTest() {
        election.createParties();
        ArrayList<CPLParty> actual = election.getPartyList();
        
        assertEquals(actual.get(0).getName(), "Democratic");
        assertEquals(actual.get(1).getName(), "Republican");
        assertEquals(actual.get(2).getName(), "NewWave");
        assertEquals(actual.get(3).getName(), "Reform");
        assertEquals(actual.get(4).getName(), "Green");
        assertEquals(actual.get(5).getName(), "Independent");
    }

    @Test
    public void distributeSeatsTest() {
        election.createParties();
        ArrayList<CPLParty> partyList = election.getPartyList();
        scanner.getCPLCandidates(partyList);  //adds all candidates to party partyList[i]
        int seats = scanner.getSeats();
        election.setNumSeats(seats);
        election.setRemainingSeats(seats);
        election.setSeatsDistributed(0);
        election.setQuota(2);
        // Adds ballots to each party
        scanner.tallyCPLBallots(partyList);
        election.distributeSeats();
        
        assertTrue(partyList.get(0).getSeatsGiven() == 0);
        assertTrue(partyList.get(1).getSeatsGiven() == 0);
        assertTrue(partyList.get(2).getSeatsGiven() == 0);
        assertTrue(partyList.get(3).getSeatsGiven() == 0);
        assertTrue(partyList.get(4).getSeatsGiven() == 0);
        assertEquals(partyList.get(5).getSeatsGiven(), 1);
    }

    @Test
    public void distributeRemaindersTest() {
        election.createParties();
        election.randomCoinToss = false;
        ArrayList<CPLParty> partyList = election.getPartyList();
        scanner.getCPLCandidates(partyList);  //adds all candidates to party partyList[i]
        int seats = scanner.getSeats();
        election.setNumSeats(seats);
        election.setRemainingSeats(seats);
        election.setSeatsDistributed(0);
        election.setQuota(2);
        // Adds ballots to each party
        scanner.tallyCPLBallots(partyList);
        election.distributeSeats();
        election.distributeRemainders();

        assertEquals(partyList.get(0).getSeatsGiven(), 1);
        assertEquals(partyList.get(1).getSeatsGiven() + partyList.get(4).getSeatsGiven(), 0);
        assertEquals(partyList.get(2).getSeatsGiven(), 0);
        assertEquals(partyList.get(3).getSeatsGiven(), 0);
        assertEquals(partyList.get(5).getSeatsGiven(), 1);
    }


    @Test
    public void partyOverloadTest() {
        election.createParties();
        ArrayList<CPLParty> partyList = election.getPartyList();
        scanner.getCPLCandidates(partyList);  //adds all candidates to party partyList[i]
        int seats = scanner.getSeats();
        election.setNumSeats(seats);
        election.setRemainingSeats(seats);
        election.setSeatsDistributed(0);
        election.setQuota(2);
        // Adds ballots to each party
        scanner.tallyCPLBallots(partyList);
        election.distributeSeats();
        election.distributeRemainders();
        election.partyOverload();
        assertEquals(partyList.get(0).getSeatsGiven() + partyList.get(1).getSeatsGiven() + partyList.get(2).getSeatsGiven() + partyList.get(3).getSeatsGiven() +partyList.get(4).getSeatsGiven() + partyList.get(5).getSeatsGiven(), 3);
    }

    @Test
    public void testCalcQuota() {
        election.setNumSeats(10);
        election.setNumTotalBallots(101);
        assertEquals(election.calcQuota() +0, 11);
    }
}
