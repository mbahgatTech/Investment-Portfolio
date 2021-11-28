package ePortfolio;

/**
 *  Represents an abstract investment.
 */
public abstract class Investment {
    /**
     * A String object attribute containing the Investment symbol.
    */
    protected String symbol;
    /**
     * A String object attribute containing the name of the Investment
     */
    protected String name;
    /**
     * An integer variable attribute containing the quantity owned of the Investment
     */
    protected int quantity;
    /**
     * A double variable attribute containing the last updted price of the Investment
    */
    protected double price;
    /**
     *  A double variable attribute containing the last updated bookvalue of the Investment.
     */
    protected double bookValue;
    /**
     * A double variable attribute containing the last updated gain of the Investment.
     */
    protected double gain;  

    /**
     * This constructor declares a new Investment object and initializes its symbol, name and price.
     * The Investment's symbol is initialized with the symbol argument.
     * The Investment's name is initialized with the name argument.
     * @param symbol a String object containing the symbol of the Investment
     * @param name   a String object containing the name of the Investment
     * @param price  a double variable containing the initial price of the Investment
     */
    public Investment(String symbol, String name, double price) throws Exception {
        try {
            this.name = name;
            this.symbol = symbol;
            this.price = price; 
            quantity = 0;
            bookValue = 0.0;
            gain = 0.0;
        }
        catch (Exception e) {
            throw new Exception ("Unable to declare investment: " + e.getMessage());
        }
    }

    /**
     * This constructor declares a new Investment object and initializes its symbol and name.
     * The Investment's symbol is initialized with the symbol argument.
     * The Investment's name is initialized with the name argument.
     * @param symbol a String object containing the symbol of the Investment
     * @param name   a String object containing the name of the Investment
     */
    public Investment(String symbol, String name) throws Exception{
        this(symbol, name, 0);
    }

    /**
     * Copy Constructor declares a new Investment object and initializes its attributes with
     * a copy of the other Investment object attributes.
     * @param other An Investment object containing the attributes which will be copied.
     */
    public Investment (Investment other) throws Exception{
        this(((Investment)other).symbol, ((Investment)other).name, ((Investment)other).price);
        this.quantity = ((Investment)other).quantity;
        this.bookValue = ((Investment)other).bookValue;
        this.gain = ((Investment)other).gain;
    }

    /**
     * Default constructor that declares a new Investment object.
     * Attributes are initialized with the default java values.
     */
    public Investment() {
        
    }
    
    /**
     * A mutator method updating the symbol of the Investment.
     * @param symbol a String object containing the new symbol of the Investment object.
     */
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    } 

    /**
     * A mutator method updating the name of the Investment.
     * @param name A String object containing the new name of the Investment.
     */
    public void setName (String name) {
        this.name = name;
    }

    /**
     * A mutator method updating the bookValue of the Investment.
     * @param bookValue A double value containing the new bookValue of the Investment.
     */
    public void setBookValue (double bookValue) {
        this.bookValue = bookValue;
    }

    /**
     * A mutator method updating the price of the Investment and calculating the gain on investment.
     * @param price A double value containing the new price of the Investment.
     */
    public void setPrice (double price) throws Exception {  
        try {
            updatePrice(price);
            this.getGain();
        }
        catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    /**
     * A mutator method updating the price of the Investment.
     * @param price A double value containing the new price of the Investment.
     */
    public void updatePrice(double price) throws Exception {
        if (price >= 0) {
            this.price = price;
        }
        else {
            throw new Exception("Invalid price.");
        }
    }
    
    /**
     * A mutator method updating the gain of the Investment.
     * @param gain A double value containing the new gain of the Investment.
     */
    public void setGain (double gain) {
        this.gain = gain;
    }

    /**
     * A mutator method updating the quantity of the Investment.
     * @param quantity A double value containing the new quantity of the Investment.
     */
    public void setQuantity (int quantity) throws Exception {
        if (quantity >= 0) {
            this.quantity = quantity;
        }
        else {
            throw new Exception("Invalid quantity.");
        }
    }

    /**
     * An accessor method that reads the name of the Investment.
     * @return The name String attribute of the calling Investment object.
     */
    public String getName() {
        return name;
    }
    
    /**
     * An accessor method that reads the symbol of the Investment.
     * @return The symbol String attribute of the calling Investment object.
     */
    public String getSymbol() {
        return symbol;
    }
    
    /**
     * An accessor method that reads the price of the Investment.
     * @return The double price attribute of the calling Investment object.
     */ 
    public double getPrice() {
        return price;
    }

    /**
     * An accessor method that reads the quantity owned of the Investment.
     * @return The integer quantity attribute of the calling Investment object.
     */ 
    public int getQuantity() {
        return quantity;
    }

    /**
    * An accessor method that reads the book value of the Investment.
    * @return The double bookValue attribute of the calling Investment object.
    */ 
    public double getBookValue() {
        return bookValue;
    }

    /**
     * Abstract method computes the value of gain attribute of the calling Investment object.
     * @return A double variable containing the updated gain of the Investment.
     */
    public abstract double getGain();

    /**
     * This method performs a purchase operation on the calling Investment object.
     * The quantity is added to the existing quantity attribute.
     * The bookValue attribute is updated by adding the book value of the purchase.
     * The gain on investment attribute is updated using the new quantity.  
     * @param quantity An intger variable containing the required purchase quantity of the Investment. 
     */
    public void buy(int quantity) throws Exception {
        try {
            this.buy(price, quantity);
        }
        catch (Exception e) { // pass exception to the calling method
            throw new Exception(e.getMessage());
        }
    }

    /**
     * This method performs a purchase operation on the calling Investment object.
     * A new price is set to the Investment object.
     * The quantity is added to the existing quantity attribute.
     * The bookValue attribute is updated by adding the book value of the purchase.
     * The gain on investment attribute is updated using the new quantity.  
     * @param price A double varianle containing the required purchase price of the Investment.
     * @param quantity An intger variable containing the required purchase quantity of the Investment. 
     */
    public void buy(double price, int quantity) throws Exception {
        // braanch checks if the quantity is not negative and terminates function if entered
        if (!isValidQuantity(quantity)) {
            return;
        }

        this.quantity += quantity;
        this.bookValue += (price * quantity);
        
        if (this instanceof Stock) {
            this.bookValue += 9.99;
        }
        
        try {
            this.setPrice(price);
        }
        catch (Exception e) {
            throw new Exception(e.getMessage());
        }

    }

    /**
     * This method performs a sell operation on the calling Investment object.
     * A new price is set to the Investment object.
     * The quantity is deducted from the existing quantity attribute.
     * The bookValue attribute is updated by deducting the book value of the quantity sold.
     * The gain on investment attribute is updated using the new quantity.  
     * @param quantity An intger variable containing the required purchase quantity of the Investment. 
     * @param price A double variable containing the selling price.
     */
    public void sell(int quantity, double price) throws Exception {
        // branch checks if quantity is valid and there is a sufficient quantity to sell
        if (!isValidQuantity(quantity)) {
            return;
        }
        else if (quantity > this.quantity) { // insufficient owned amount exception
            throw new Exception("ERROR: Can't sell a higher quantity than owned. Try again.");
        }

        // update bookvalue to the value of remaining quantity
        this.setBookValue(bookValue * (this.quantity - quantity) / this.quantity);
        try {
            this.setQuantity(this.quantity - quantity);
            this.setPrice(price);
        }
        catch (Exception e) {
            // exception should be handled by the calling method
            throw new Exception(e.getMessage());
        }
    }

    /**
     * This method performs a sell operation on the calling Investment object.
     * The method uses the current Investment price attribute as the selling price.
     * The quantity is deducted from the existing quantity attribute.
     * The bookValue attribute is updated by deducting the book value of the quantity sold.
     * The gain on investment attribute is updated using the new quantity.  
     * @param quantity An intger variable containing the required purchase quantity of the Investment. 
     */
    public void sell(int quantity) throws Exception {
        try {
            this.sell(quantity, price);
        }
        catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    /**
     * Method checks if quantity is valid.
     * It returns true if quantity is positive or 0
     * Returns false if quantity is negative
     * @param quantity An integer variable containing the quantity to be checked.
     * @return A boolean value that indicates the validity of the quantity parameter.
    */    
    public boolean isValidQuantity (int quantity) throws Exception {
        if (quantity < 0) {
            return false; 
        }
        
        return true; // valid quantity
    }

    /**
     * Method checks if another Investment object is equal to calling Investment object.
     * It ignores cases in the comparison between Strings.
     * It returns true if objects are equal.
     * Returns false if objects aren't equal.
     * @param other Another Investment object to be compared with the calling object.
     * @return A boolean value that indicates the parameter is equal to the calling object.
    */
    public boolean equals (Object other) {
        if (this.getClass() != other.getClass()) {
            return false;
        }
        
        Investment otherInv = (Investment)other;
        return this.name.equalsIgnoreCase(otherInv.name) && this.price == otherInv.price &&
               this.symbol.equalsIgnoreCase(otherInv.symbol) && 
               this.quantity == otherInv.quantity;
    }

    /**
     * Method checks if symbol and name parameters are equivalent to the symbol and name of the calling Investment object. 
     * It ignores cases in the comparison between the Strings.
     * Parameters don't have to be assigned a valid String.
     * In case the parameters meet the criteria of isNull method, they get skipped in the compaarison. 
     * It returns true if Strings are equal.
     * Returns false if Strings aren't equal.
     * @param symbol A String object containing the symbol to be checked.
     * @param name A String containing the name to be checked.
     * @return A boolean value that indicates the parameters equal the attributes.
    */
    public boolean equals (String symbol, String name) {
        boolean equals = true;
        
        if (!isNull(symbol) && !this.symbol.equalsIgnoreCase(symbol)) {
            equals = false;
        }

        if (!isNull(name) && !this.name.equalsIgnoreCase(name)) {
            equals = false;
        }
        return equals;
    }

    /**
     * Method checks if symbol parameter is equal to symbol attribute of the Investment object.
     * It ignores cases in the comparison between the 2 Strings
     * It returns true if Strings are equal.
     * Returns false if Strings aren't equal.
     * @param symbol A String object containing the symbol to be checked.
     * @return A boolean value that indicates the parameter is equal to the symbol attribute.
    */
    public boolean equalSymbol(String symbol) {
        return this.symbol.equalsIgnoreCase(symbol);
    }

    /**
     * Protected method used as criteria for other methods in class/subclasses.
     * It checks if a String is null, empty or consists of spaces only.
     * If it matches the criteria above, true is returned.
     * If String contains other chars then false is returned.
     * @param A String object passed from another method.
     * @return Boolean value indicating String criteria match.
     */
    protected static boolean isNull (String string) {
        if (string == null || string.equals("[\n ]*") || string.equals("")) {
            return true;
        }

        return false;
    }

    /**
     * Method adds all the attributes of the calling Investment object to a String object and returns it.
     * @return A String object containing all the attribute values of the calling object. 
     */
    public String toString() {
        String className = new String();
        className = this.getClass().getSimpleName();

        return "type = \"" + className + "\"\nsymbol = \"" + getSymbol() + "\"\nname = \"" + 
        getName() + "\"\nquantity = \"" + getQuantity() + "\"\nprice = \"" +getPrice() +
        "\"\nbookValue = \"" + getBookValue() + "\"\n";
    }
}
