package ePortfolio;

/**
 *  MutualFund class responsible for generating MutualFund investment objects, this class extends the Investment class.
 *  Gain on MutualFund investment can be calculated independantly from other investment types.
 */
public class MutualFund extends Investment {
    /**
     * This constructor declares a new MutualFund object and initializes its symbol and name.
     * The MutualFund's symbol is initialized with the symbol argument.
     * The MutualFund's name is initialized with the name argument.
     * @param symbol a String object containing the symbol of the MutualFund
     * @param name   a String object containing the name of the MutualFund
     */
    public MutualFund(String symbol, String name) throws Exception {
        super(symbol, name, 0);
    }

    /**
     * This constructor declares a new MutualFund object and initializes its symbol, name and price.
     * The MutualFund's symbol is initialized with the symbol argument.
     * The MutualFund's name is initialized with the name argument.
     * @param symbol a String object containing the symbol of the MutualFund
     * @param name   a String object containing the name of the MutualFund
     * @param price  a double variable containing the initial price of the MutualFund
     */
    public MutualFund(String symbol, String name, double price) throws Exception {
        super(symbol, name, price);
    }

    /**
     * Copy Constructor declares a new MutalFund object and initializes its attributes with
     * a copy of the other MuyualFund object attributes.
     * @param other A MutualFund object containing the attributes which will be copied.
     */
    public MutualFund (MutualFund other) throws Exception{
        super(other);
    }

    /**
     * Default constructor that declares a new MutualFund object.
     * Attributes are initialized with the default java values.
     */
    public MutualFund() {
    
    }

    /**
     * This method computes the gain on investment of the calling MutualFund object.
     * It assigns the value to gain attribute and returns it.
     * @return A double variable containing the updated gain of the MutualFund.
     */
    public double getGain() {
        // reduces 45$ from the payment for the commision
        double payment = getPrice() * getQuantity() - 45; 
        double gain = payment - getBookValue();
        setGain(gain); 

        return gain;
    }

}
