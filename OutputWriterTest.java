
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.time.LocalDateTime;
import java.util.Scanner;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;



/**
* Tests the OutputWriter Class
*
* @author  Cole Lindfors
* @version 1.0
* @since   2023-3-23
*/
public class OutputWriterTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @After
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    @Test
    public void testMakeHeader() {
        String date1 = LocalDateTime.now().toString();
        OutputWriter writer1 = new OutputWriter("IR", date1);
        writer1.makeHeader();
        String expected1 = "audit/IR_" + date1 + ".txt\n";
        String actual1 = readWholeFile(writer1.getFileName());
        assertEquals(expected1, actual1);

        String date2 = LocalDateTime.now().toString();
        OutputWriter writer2 = new OutputWriter("CPL", date2);
        writer2.makeHeader();
        String expected2 = "audit/CPL_" + date2 + ".txt\n";
        String actual2 = readWholeFile(writer2.getFileName());
        assertEquals(expected2, actual2);
    }

    /**
     * Reads the whole file, used for testing
     * @param fileName file to be read
     * @return the entire file contents as a string
     */
    private String readWholeFile(String fileName) {
        String text = "";
        try {
            File file = new File(fileName);
            FileReader reader = new FileReader(file);
            int character;
            while ((character = reader.read()) != -1) {
                text += (char) character;
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return text;
    }       

    @Test
    public void IR() throws FileNotFoundException {        
        String location = System.getProperty("user.dir") + "/testing/ExampleBallotFiles/";
        File file = new File(location + "IR_2023-03-26T10:46:47.983298.txt");
        Scanner scanner = new Scanner(file);
        String line = scanner.nextLine();
        assertEquals(line, "audit/IR_2023-03-26T10:46:47.983298.txt");
        line = scanner.nextLine();
        assertEquals(line, "Round 1");
        line = scanner.nextLine();
        assertEquals(line, "Candidate Rosen(D): 0 Vote(s)");
        line = scanner.nextLine();
        assertEquals(line, "Candidate Kleinberg(R): 0 Vote(s)");
        line = scanner.nextLine();
        assertEquals(line, "Candidate Chou(I): 0 Vote(s)");
        line = scanner.nextLine();
        assertEquals(line, "The next highest remainder is a tie between 3 parties, random party will be chosen");
        line = scanner.nextLine();
        assertEquals(line, "Tie between Rosen(D) party, Kleinberg(R) party, Chou(I) party");
        line = scanner.nextLine();
        assertEquals(line, "Rosen(D) was awarded a seat via coin toss");
        line = scanner.nextLine();
        assertEquals(line, "Round 2");
        line = scanner.nextLine();
        assertEquals(line, "Candidate Rosen(D): 0 Vote(s)");
        line = scanner.nextLine();
        assertEquals(line, "Candidate Kleinberg(R): 0 Vote(s)");
        line = scanner.nextLine();
        assertEquals(line, "Candidate Chou(I): 0 Vote(s)");
        line = scanner.nextLine();
        assertEquals(line, "The next highest remainder is a tie between 2 parties, random party will be chosen");
        line = scanner.nextLine();
        assertEquals(line, "Tie between Kleinberg(R) party, Chou(I) party");
        line = scanner.nextLine();
        assertEquals(line, "Kleinberg(R) was awarded a seat via coin toss");
        line = scanner.nextLine();
        assertEquals(line, "Kleinberg(R) wins by majority vote");
        assertTrue(!scanner.hasNextLine());
        scanner.close();
    }

    @Test
    public void CPL() throws FileNotFoundException {
        String location = System.getProperty("user.dir") + "/testing/ExampleBallotFiles/";
        File file = new File(location + "CPL_2023-03-26T10:48:29.997465.txt");
        Scanner scanner = new Scanner(file);
        String line = scanner.nextLine();
        assertEquals(line, "audit/CPL_2023-03-26T10:48:29.997465.txt");
        line = scanner.nextLine();
        assertEquals(line, "Quota for a seat is 3");
        line = scanner.nextLine();
        assertEquals(line, "Democratic party has 0 vote(s)");
        line = scanner.nextLine();
        assertEquals(line, "Republican party has 4 vote(s)");
        line = scanner.nextLine();
        assertEquals(line, "Independent party has 0 vote(s)");
        line = scanner.nextLine();
        assertEquals(line, "");
        line = scanner.nextLine();
        assertEquals(line, "First allocation of seats:");
        line = scanner.nextLine();
        assertEquals(line, "Democratic party has 0 seat(s), 0 vote(s) remaining, 1 candidate(s) remaining");
        line = scanner.nextLine();
        assertEquals(line, "Republican party has 1 seat(s), 2 vote(s) remaining, 1 candidate(s) remaining");
        line = scanner.nextLine();
        assertEquals(line, "Independent party has 0 seat(s), 0 vote(s) remaining, 1 candidate(s) remaining");
        line = scanner.nextLine();
        assertEquals(line, "0 seat(s) has been distributed, there are 1 seat(s) left");
        line = scanner.nextLine();
        assertEquals(line, "");
        line = scanner.nextLine();
        assertEquals(line, "Second allocation of seats:");
        line = scanner.nextLine();
        assertEquals(line, "The next highest remainder is a tie between 2 parties, random party will be chosen");
        line = scanner.nextLine();
        assertEquals(line, "Tie between Republican party, Republican party");
        line = scanner.nextLine();
        assertEquals(line, "Republican was awarded a seat via coin toss");
        line = scanner.nextLine();
        assertEquals(line, "Distributing Seats not distributed due to lack of candidates:");
        line = scanner.nextLine();
        assertEquals(line, "Democraticwas awarded a seat via coin toss");
        line = scanner.nextLine();
        assertEquals(line, "");
        line = scanner.nextLine();
        assertEquals(line, "Final Seats:");
        line = scanner.nextLine();
        assertEquals(line, "Democratic has 1 seat(s).");
        line = scanner.nextLine();
        assertEquals(line, "Republican has 1 seat(s).");
        line = scanner.nextLine();
        assertEquals(line, "Independent has 0 seat(s).");
        line = scanner.nextLine();
        assertEquals(line, "The candidate(s) receiving seats are Bernie from the Democratic party.  Kanye from the Republican party. ");
        assertTrue(!scanner.hasNextLine());
        scanner.close();
        
    }
}
