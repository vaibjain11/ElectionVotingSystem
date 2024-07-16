/**
* The Abstract Election class acts as an extension for
* multiple different election types, adding functionality
* for elections to store the number of total ballots, the
* object used for writing to the terminal and audit file,
* and the ElectionScanner Object used for processing the
* ballot file.
*
* @author  Patrick Hunner
* @author  Ian Paterson
* @author  Cole Lindfors
* @version 1.1
* @since   2023-3-20
*/
import java.util.ArrayList;
import java.util.Random;

public abstract class Election {
    protected Integer numTotalBallots = 0;
    protected OutputWriter output; // TODO: is this stored here?
    protected ArrayList<ElectionScanner> electionScanners = new ArrayList<ElectionScanner>();
    protected boolean randomCoinToss = true;

    /**
     * Constructor
     * @param electionScanner the electionScanner to be used for the election
     * @param output the output writer being used in this election
     */
    // Election(ElectionScanner electionScanner, OutputWriter output) {
    //     this.electionScanner = electionScanner;
    //     this.output = output;

    // }

    /** 
     * Constructor
     * @param electionScanner the electionScanner to be used for the election
     */
    Election(ArrayList<ElectionScanner> electionScanners) {
        this.electionScanners = electionScanners;
    }

    /** 
     * Constructor
     * @param electionScanner the electionScanner to be used for the election
     */
    Election(ElectionScanner electionScanner) {
        this.electionScanners.add(electionScanner);
    }

    /**
     * sets the number of total ballots
     * @param num number to set
     */
    public void setNumTotalBallots(int num) {
        numTotalBallots = num;
    }

    /**
     * @return number of total ballots
     */
    public int getNumTotalBallots() {
        return numTotalBallots;
    }

    /**
     * Breaks ties between two or more contenders
     * @param <T> any object that extends contender
     * @param contenders the contenders to decide between
     * @return the contender that won
     */
    protected <T extends Contender> T coinToss(ArrayList<T> contenders) {
        if (randomCoinToss) {
            int randomWinner = 0;
            int size = contenders.size();
            Random rand = new Random();
            for (int i = 0; i < 1000; i++) { //run 1000x times before getting the answer to ensure more acurately random
                randomWinner = rand.nextInt(size);
            }
            return contenders.get(randomWinner); // actual winner decided on 1000th run
        }
        else {      // determinant answer for testing
            return contenders.get(0);    
        }
        
        
    }
}






