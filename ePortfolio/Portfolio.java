package ePortfolio;

import java.util.Scanner;
import java.util.HashMap;
import java.util.ListIterator;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.File;
import java.io.PrintWriter;

/**
 *  Portfolio class responsible for keeping records of different type of investments into an ArrayList.
 *  Records can be saved and loaded from files and investments can be searched for based on user criteria.
 */
public class Portfolio {
    /**
     * Arraylist of Investment objects owned.
     */
    private ArrayList<Investment> investments = new ArrayList<>();

    private HashMap<String, ArrayList<Integer>> relevantInvestments = new HashMap<String, ArrayList<Integer>>();

    /**
     * Constructor declares a new Portfolio object and initializes its investments attribute.
     * The investments attributes is initialized with a copy of investments parameter.
     * @param investments An ArrayList containing all the investments owned to initialize the investments attribute.
     */
    public Portfolio(ArrayList<Investment> investments) {
        this.investments = new ArrayList<>(investments);
    }

    /**
     * Copy Constructor declares a new Portfolio object and initializes its investments and.
     * relevantInvestments attributes with a copy of of other objects parameter.
     * @param other An Object class object containing the attributes which will be copied.
     */
    public Portfolio (Object other) {
        if (this.getClass() != other.getClass()) {
            return; 
        }

        this.investments = new ArrayList<>(((Portfolio)other).investments);
        this.relevantInvestments = new HashMap<>(((Portfolio)other).relevantInvestments);
    }

    /**
     * Default constructor that declares a new Portfolio object.
     */
    public Portfolio() {

    }

    public ArrayList<Investment> getArray () {
        return new ArrayList<Investment>(investments);
    }


    /**
     * Method performs a purchase operation on the calling Portfolio object.
     * A new Stock or MutualFund is added to the ArrayLists if the investment 
     * didn't previously exist. 
     * Quantity, price and bookvalue are updated for ane existing investment object with a matching symbol.
     * @param type String object containing the type for investment to be purchased
     * @param name String object containing the name for investment to be purchased
     * @param symbol String object containing the symbol for investment to be purchased
     * @param quantity String object containing the integer quantity value for investment to be purchased
     * @param price String object containing the double price value for investment to be purchased
     * @return String object containing the purchased investment information.
     */
    public String buy(String type, String symbol, String name, String quantity, String price) throws Exception {
        validateNums(quantity, price);

        if (isNull(symbol) || isNull(name)) {
            throw new Exception("Symbol and name can't be empty, please specify the " + type + ".");
        }

        int investmentIndex;
        // branch performs operation using a class based on type parameter
        if (type.equalsIgnoreCase("stock") || type.equalsIgnoreCase("s")) {
            // branch appends a new stock if symbol doesn't exist in 
            investmentIndex = investmentExists(investments, symbol);
            if (investmentIndex == -1) {
                investments.add(new Stock());
                investmentIndex = investments.size() - 1;

                investments.get(investmentIndex).setSymbol(symbol);
                investments.get(investmentIndex).setName(name);
            }
            else if (!(investments.get(investmentIndex) instanceof Stock)) {
                throw new Exception("ERROR: Symbol entered belongs to another investment type \"" +
                investments.get(investmentIndex).getClass().getSimpleName() + "\"");
            }
        }
        else if (type.equalsIgnoreCase("mutual") || type.equalsIgnoreCase("m")
        || type.equalsIgnoreCase("mutualfund") || type.equalsIgnoreCase("mutual fund")) {
            // branch appends a new fund and sets symbol 
            // if fund doesn't exist
            investmentIndex = investmentExists(investments, symbol);
            if (investmentIndex == -1) {
                investments.add(new MutualFund());
                investmentIndex = investments.size() - 1;

                investments.get(investmentIndex).setSymbol(symbol);
                investments.get(investmentIndex).setName(name);
            }
            else if (!(investments.get(investmentIndex) instanceof MutualFund)) {
                throw new Exception("WARNING: Symbol entered belongs to another investment type \"" +
                investments.get(investmentIndex).getClass().getSimpleName() + "\"");
            }
        }
        else {
            throw new Exception("ERROR: Invalid investment type input, try again.");
        }

        mapName(investments.get(investmentIndex), investmentIndex);
        investments.get(investmentIndex).buy(Double.parseDouble(price), Integer.parseInt(quantity));
        return investments.get(investmentIndex).toString();
    }
    
    /**
     * Method performs a sell operation on the calling Portfolio object.
     * Stock or MutualFund is removed from the ArrayLists if the quantity reaches 0.
     * Error Exception generated when investment doesn't exist in Portfolio object.
     * @param symbol String object containing the symbol for investment to be sold
     * @param quantity String object containing the integer quantity value for investment to be sold
     * @param price String object containing the double price value for investment to be sold
     * @return String object containing the transaction information.
     */
    public String sell(String symbol, String quantity, String price) throws Exception {
        // transaction information is validated (Exceptions thrown if failed)    
        validateNums(quantity, price);

        String transaction = "";
        int investmentIndex;
        // method terminates and Exception thrown in case the object wasn't found
        investmentIndex = investmentExists(investments, symbol);
        if (investmentIndex == -1) {
            throw new Exception("ERROR: Invalid investment symbol, investment doesn't" + 
            " exist in portfolio.");
        }
        investments.get(investmentIndex).sell(Integer.parseInt(quantity), Double.parseDouble(price));
        
        transaction += "Sold "+ quantity + " @ " + price + "$. New book value: "
        + investments.get(investmentIndex).getBookValue() + "$\n";

        // branch deletes the object at investmentIndex if quantity owned reaches 0 
        if (investments.get(investmentIndex).getQuantity() == 0) {
            removeMappedName(investments.get(investmentIndex), investmentIndex);
            investments.remove(investmentIndex);
            return transaction; // only transaction information is returned
        }

        // transaction and Investment information is returned
        transaction += investments.get(investmentIndex).toString();
        return transaction;
    }

    /** 
     * Method prompts the user to enter new prices for all investments.
     * It then updates all the prices in the calling object's investment ArrayLists.
     */ 
    public String updatePortfolio(String price, int investmentIndex) throws Exception {
        try {
            investments.get(investmentIndex).setPrice(Double.parseDouble(price));
            return investments.get(investmentIndex).toString();
        }
        catch (Exception e) {
            throw new Exception("Invalid price. Try again");
        }
    } 

    /**
     * Method computes the total gain on investments.
     * It adds the gain value of all Stock and MutualFund objects and returns the sum.
     * @return A double value containing the total gain on investments.
     */
    public double getGain() {
        double sum = 0;
        
        // add gain of all Investment objects
        for (Investment currentInvestment : investments) {
            sum += currentInvestment.getGain();
        }

        return sum;
    }

    /**
     * Method returns gain on all investments.
     * It adds returns a String of each symbol followed by the gain on investment.
     * @return A String object containing individual gains on investment.
     */
    public String getIndividualGains() {
        String gains = "";
        
        // add gain of all Investment objects
        for (Investment currentInvestment : investments) {
            gains += currentInvestment.getSymbol() + ": " + currentInvestment.getGain() + "\n";
        }

        return gains;
    }

    /**
     * Method searches for investments based on user defined criteria.
     * User is prompted for search criteria (symbol, name keywords and price range).
     * The user can skip any of the fields during entry.
     * Method then outputs all Stock and MutualFund objects that match the criteria to the screen.
     */
    public String search (String symbol, String keywords, String lowerPrice, String higherPrice) throws Exception{
        // initialized low and high prices to lowest and highest doubles in java
        double lowPrice = Double.MIN_VALUE, highPrice = Double.MAX_VALUE;
        String [] subStrings;
        String priceRange = "";
        String results = "\n";

        // loop makes sure that user enters valid price range without any letters
        if (higherPrice.contains("([A-Za-z]+") || lowerPrice.contains("([A-Za-z]+")) { 
            throw new Exception("Invalid input, please enter a valid price range.");
        }
        
        // branch checks if priceRange is a single number and parses it to double
        if (!isNull(lowerPrice)) {
            priceRange = lowerPrice + "-"; 
            try {
                lowPrice = Double.parseDouble(lowerPrice);
            }
            catch (Exception e) {
                throw new Exception ("Invalid low price.");
            }
        }
        else {
            priceRange = "-";
        }

        if (!isNull(higherPrice)) {
            priceRange += higherPrice;
            try {
                highPrice = Double.parseDouble(higherPrice);
            }
            catch (Exception e) {
                throw new Exception ("Invalid high price.");
            }
        }
        // Note: branch isn't entered if user doesn't enter a range at all 
        
        // if not null, all substrings should be present in objects at validIndices of investments
        // otherwise return from method when validIndices is reduced to 0 size
        if (!isNull(keywords)) {
            ListIterator<Integer> validIndices = null;
            ArrayList<Integer> clonedIndices = new ArrayList<>();
            subStrings = keywords.split("[ \n/,]+");

            for (String subString : subStrings) {
                // exit if a subString doesn't exist in the hashmap
                if (relevantInvestments.get(subString.toLowerCase()) == null) {
                    return results;
                }
                else if (validIndices == null || (!validIndices.hasPrevious() && !validIndices.hasNext())) {
                    // initialize validIndices with the first indices values from the first subString in the hashMap
                    if (relevantInvestments.get(subString.toLowerCase()) != null) {
                        validIndices = (ListIterator<Integer>)relevantInvestments.get(subString.toLowerCase()).listIterator();

                        // copy the indices into a cloned ArrayList to avoid modifying the original List
                        while (validIndices.hasNext()) {
                            clonedIndices.add(validIndices.next());
                        }

                        validIndices = clonedIndices.listIterator();
                    }
                    else {
                        return results;
                    }
                }
                else {
                    int temp;
                    while (validIndices.hasNext()) {
                        temp = validIndices.next();
                        
                        // removes any indices that don't contain the subString in current main loop iteration 
                        if(!relevantInvestments.get(subString.toLowerCase()).contains(temp)) {
                            validIndices.remove();
                            
                            // exit method to when indices dont match in all keywords
                            if (validIndices == null || (!validIndices.hasPrevious() && !validIndices.hasNext())) {
                                return results;
                            }
                        }
                    }
                }

                // reset iterator to the start of array
                while (validIndices.hasPrevious()) {
                    validIndices.previous();
                }
            }
    

            // loop through all investment objects with valid keys
            while (validIndices.hasNext()) {
                // updates keywords to the name of Investment to pass the equals method 
                // (we established that keys are valid and were only checking objects with valid keys)
                int index = validIndices.next();;
                keywords = null;
                
                if (isNull(symbol)) {
                    symbol = null;
                }

                // prints all investments within range (if applicable) and with valid symbol/substrings
                if (investments.get(index).equals(symbol, keywords) && isNull(priceRange)) {
                    results += investments.get(index).toString() + "\n"; 
                }
                else if (investments.get(index).equals(symbol, keywords) && !isNull(priceRange) 
                && investments.get(index).getPrice() >= lowPrice && 
                investments.get(index).getPrice() <= highPrice) {
                    results += investments.get(index).toString() + "\n"; 
                }
            }

            return results;
        }
        
        // loop only used when keywords were not entered by the user
        for (Investment curr : investments) {
            // updates keywords to null to pass the equals method 
            // (we established that keys are valid and were only checking objects with valid keys)
            keywords = null;
              
            // in case the symbol is empty or consists of spaces only set it to null
            if (isNull(symbol)) {
                symbol = null;
            }
            
            // prints all investments within range (if applicable) and with valid symbol/substrings
            if (isNull(priceRange) && curr.equals(symbol, keywords)) {
                results += curr.toString() + "\n"; 
            }
            else if (curr.equals(symbol, keywords) && !isNull(priceRange) 
            && curr.getPrice() >= lowPrice && 
            curr.getPrice() <= highPrice) {
                results += curr.toString() + "\n"; 
            }
        }
        return results;
    }

    
    /**
     * Method initializes the investments ArrayList attribute of the calling object from a file.
     * @param fileName String object containing the name of the file to load the investments from.
     */
    public void loadInvestments(String fileName) throws Exception {
        if (isNull(fileName)) {
            throw new Exception ("ERROR: Invalid file name.");
        }

        try {
            File file = new File(fileName.strip());

            Scanner input = new Scanner(file);
            String [] tokens;
            int investmentIndex = -1;
            
            // loop adds investment information in the file to the investments ArrayList
            while (input.hasNextLine()) {
                // first token is the type of value in the second token
                tokens = input.nextLine().split("\"");
                
                // based on token[0], token[1] is used to set attributes of an investment or declare a new investment
                if(tokens[0].contains("type = ")) {
                    if(tokens[1].toLowerCase().contains("stock")) {
                        investments.add(new Stock());
                    }
                    else if (tokens[1].toLowerCase().contains("mutualfund")) {
                        investments.add(new MutualFund());
                    }
                    else {
                        input.close();
                        return;
                    }
                    investmentIndex = investments.size() - 1;
                }
                else if(tokens[0].toLowerCase().contains("symbol = ")) {
                    investments.get(investmentIndex).setSymbol(tokens[1]);
                }
                else if(tokens[0].toLowerCase().contains("name = ")) {
                    investments.get(investmentIndex).setName(tokens[1]);
                    mapName(investments.get(investmentIndex), investmentIndex);
                }
                else if(tokens[0].toLowerCase().contains("quantity = ")) {
                    investments.get(investmentIndex).setQuantity(Integer.parseInt(tokens[1].strip()));
                }
                else if(tokens[0].toLowerCase().contains("price = ")) {
                    investments.get(investmentIndex).updatePrice(Double.parseDouble(tokens[1].strip()));
                }
                else if(tokens[0].toLowerCase().contains("bookvalue = ")) {
                    investments.get(investmentIndex).setBookValue(Double.parseDouble(tokens[1].strip()));
                }
            }
            input.close();
        }
        catch (Exception e) {
            throw new Exception("Failed to open file for reading.");
        }
        
    }

    /**
     * Method saves the investments ArrayList attribute of the calling object to a file.
     * @param fileName String object containing the name of the file to save the investments to.
     */
    public void saveInvestments(String fileName) throws Exception {
        // check for illegal file name
        if (isNull(fileName)) {
            throw new Exception ("ERROR: failed to open file for reading.");
        }
        
        try {
            PrintWriter file = new PrintWriter(fileName, "UTF-8");
            
            // print all investments to the file
            for (Investment curreInvestment : investments) {
                file.print(curreInvestment.toString());
            }
            
            file.close();
        }
        catch (Exception e) {
            throw new Exception("Failed to open file for reading.");
        }
    }

    /**
     * Private method used as criteria for other methods in class.
     * It check if a String is null, empty or consists of spaces only.
     * If it matches the criteria above, true is returned.
     * If String contains other chars then false is returned.
     * @param string String object passed from another method to be checked.
     * @return Boolean value indicating String criteria match.
     */
    private static boolean isNull (String string) {
        if (string == null || string.equals("[\n ]*") || string.equals("")) {
            return true;
        }

        return false;
    }

    /**
     * Private method checks if a Strings are null, empty or consists of spaces only.
     * It also checks if the quantiy and price values are lower than or equal 0 
     * If it matches the criteria above, appropriate exceptions are thrown that can be handled by the caller.
     * Nothing is returned and execution continues in case criteria doesn't match.
     * @param quantity String object containing the quantity of the investment.
     * @param price String object containing the price of the investment.
     */
    private void validateNums(String quantity, String price) throws Exception {
        try {
            if (Double.parseDouble(price) <= 0) {
                throw new Exception("Price needs to be a number higher than 0. Try again.");
            }
        }
        catch(Exception e) {
            throw new Exception("Price needs to be a number higher than 0. Try again.");
        }

        try {
            if (Double.parseDouble(quantity) <= 0) {
                throw new Exception("Quantity needs to be a number higher than 0. Try again.");
            }
        }
        catch(Exception e) {
            throw new Exception("Quanity needs to be a number higher than 0. Try again.");
        }
    }

    /**
     * Method searches for an Investment object in ArrayList of investment objects.
     * The symbol is the used  criteria to find the desired object.
     * @param investments An ArrayList of Investment objects to be searched.
     * @param symbol A String object containing the symbol used as criteria for the search algorithm.
     * @return An integer value containing the index of the Investment object in the ArrayList if found. -1 is returned otherwise.
     */
    public int investmentExists(ArrayList<Investment> investments, String symbol) {
        int i = 0;
        // checks if symbol parameter matches any of the Investment objects
        for (Investment currentInvestment : investments) {
            if (currentInvestment.equalSymbol(symbol)) {
                return i; // returns the index
            }

            i++; // incremented each iteration
        }

        return -1; // failure to find Investment object
    }
    
    /**
     * Method adds index of an object in ArrayList investments to values in the hashMap
     * The name keywords of the Investment object is used as the keys in the hashMap
     * @param object An Investment object with the required name to be tokenized and added to the hashMap.
     * @param investmentIndex the index of the object parameter in the ArrayList investments attribute of the calling portfolio object.
     */
    private void mapName(Investment object, int investmentIndex) {
        String [] tokens = object.getName().split("[ ]+");
        ArrayList<Integer> tempArray;

        // for each name tokens index of investment is added to the value of the token in hashMap relevantInvestments  
        for (String token : tokens) {
            // initialize the hashmap when its empty
            if (relevantInvestments.get(token.toLowerCase()) == null) {
                relevantInvestments.put(token.toLowerCase(), new ArrayList<Integer>(Arrays.asList(investmentIndex)));
            }
            else {
                tempArray = relevantInvestments.get(token.toLowerCase());
                // only adds the index if same investment doesn't exist
                if (!tempArray.contains(investmentIndex)) {
                    tempArray.add(investmentIndex);
                }
            }
        }

    }

    /**
     * Method removes index of an object in ArrayList investments from values in the hashMap
     * The name keywords of the Investment object is used as the keys in the hashMap
     * @param object An Investment object with the required name to be tokenized for keys in the hashMap.
     * @param investmentIndex the index of the object parameter in the ArrayList investments to be removed from the values of each key.
     */
    private void removeMappedName (Investment object, int investmentIndex) {
        String [] tokens = object.getName().split("[ ]+");
        ArrayList<Integer> tempArray;
        
        // loops through all name keywords and removes the investmentIndex from the index list
        for (String token : tokens) {
            tempArray = relevantInvestments.get(token.toLowerCase());
            
            // branch removes the hash map key if the only value is removed element index
            if (tempArray.size() == 1) {
                relevantInvestments.remove(token.toLowerCase());
                continue;
            }
            
            // otherwise the single index is removed from the index list 
            tempArray.remove(investmentIndex);
        }
    }

    /**
     * Method adds all the attributes of all Stock/MutualFund objects stored in 
     * the Portfolio calling object's ArrayLists to a String object and returns it.
     * @return A String object containing all the attribute values of all objects in the ArrayLists. 
     */
    public String toString() {
        String portfolio = ""; // empty String 

        // adds all Investment objects attributes
        for (Investment currentInvestment : investments) {
            portfolio += currentInvestment.toString();
        }

        return portfolio; // return final String
    }

    /**
     * Method checks if another Portfolio object is equal to calling Portfolio object.
     * It ignores cases in the comparison between Strings.
     * It returns true if objects are equal.
     * Returns false if objects aren't equal.
     * @param other Another Portfolio object to be compared with the calling object.
     * @return A boolean value that indicates the parameter is equal to the calling object.
    */
    public boolean equals (Object other) {
        if (this.getClass() != other.getClass()) {
            return false;
        }

        Portfolio temp = (Portfolio)other;
        return this.toString().equalsIgnoreCase(temp.toString());
    }
    
}
