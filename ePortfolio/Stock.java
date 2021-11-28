package ePortfolio;

/**
 *  Stock class responsible for generating Stock investment objects, this class extends the Investment class.
 * Gain on stock investment can be calculated independantly from other investment types.
 */
public class Stock extends Investment {
    /**
     * This constructor declares a new Stock object and initializes its symbol and name.
     * The Stock's symbol is initialized with the symbol argument.
     * The Stock's name is initialized with the name argument.
     * @param symbol a String object containing the symbol of the stock
     * @param name   a String object containing the name of the stock
     */
    public Stock(String symbol, String name) throws Exception {
        super(symbol, name, 0);
    }
    
    /**
     * This constructor declares a new Stock object and initializes its symbol, name and price.
     * The Stock's symbol is initialized with the symbol argument.
     * The Stock's name is initialized with the name argument.
     * @param symbol a String object containing the symbol of the Stock
     * @param name   a String object containing the name of the Stock
     * @param price  a double variable containing the initial price of the Stock
     */
    public Stock(String symbol, String name, double price) throws Exception {
        super(symbol, name, price);
    }

    /**
     * Copy Constructor declares a new Stock object and initializes its attributes with
     * a copy of the other Stock object attributes.
     * @param other A Stock object containing the attributes which will be copied.
     */
    public Stock (Stock other) throws Exception{
        super(other);
    }

    /**
     * Default constructor that declares a new Stock object.
     * Attributes are initialized with the default java values.
     */
    public Stock() {
    
    }

    /**
     * This method computes the gain on investment of the calling Stock object.
     * It assigns the value to gain attribute and returns it.
     * @return A double value containing the updated gain of the Stock.
     */
    public double getGain() {
        double payment = getPrice() * getQuantity() - 9.99;
        double gain = payment - getBookValue();

        setGain(gain); 
        return gain;
    }

}
