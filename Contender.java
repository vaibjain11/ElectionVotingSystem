/**
* The Abstract Contender class acts as an extension for
* multiple different voting styles, adding functionality
* for contenders to store names, the number of ballots.
* It is also useful in allowing modular Tiebraking for 
* different types of contender children
*
* @author  Patrick Hunner
* @author  Ian Paterson
* @version 1.0
* @since   2023-3-20
*/
public abstract class Contender {
    protected String name;
    protected Integer ballots;
    /**
     * Constructor
     * Ceates a new contender with a given name with no ballots assigned
     * @param name
     */
    Contender(String name) {
        this.name = name;
        this.ballots = null;
    }

    /**
     * gives the number of votes the contender currently has
     * @return the number of ballots a contender has
     */
    public abstract Integer getBallots();

    /**
     * shows the contender's name
     * @return the name of the contender
     */
    public String getName() {
        return name;
    }
}
